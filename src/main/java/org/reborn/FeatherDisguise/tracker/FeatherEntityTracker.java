package org.reborn.FeatherDisguise.tracker;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import lombok.extern.log4j.Log4j2;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.reborn.FeatherDisguise.FeatherDisguise;
import org.reborn.FeatherDisguise.metadata.EntityType;
import org.reborn.FeatherDisguise.util.ITeardown;
import org.spigotmc.AsyncCatcher;
import org.spigotmc.TrackingRange;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Log4j2
public class FeatherEntityTracker extends EntityTracker implements ITeardown {

    @ApiStatus.Internal
    @NotNull private final FeatherDisguise featherDisguise;

    @ApiStatus.Internal
    @NotNull private final WorldServer nmsWorld; // todo this might technically be un-safe given the terrible nature of worlds in spigot (WeakReference<> my good man?)

    @NotNull private final Int2ObjectOpenHashMap<FeatherEntityTrackerEntry> trackedObjects;

    private final int preCalculatedMaxEntityViewingDistance;

    @ApiStatus.Internal
    private static final int MAX_ALLOWED_VIEWING_DISTANCE = 512;

    public FeatherEntityTracker(@NotNull final FeatherDisguise featherDisguise, @NotNull final WorldServer nmsServerWorld) {
        super(nmsServerWorld);
        this.featherDisguise = featherDisguise;
        this.nmsWorld = nmsServerWorld; // superclass privatised WorldServer, what a dog. we need quick access to this, so let's just store our own )):
        this.trackedObjects = new Int2ObjectOpenHashMap<>();
        this.preCalculatedMaxEntityViewingDistance = Math.min(MAX_ALLOWED_VIEWING_DISTANCE, nmsServerWorld.getMinecraftServer().getPlayerList().d()); // (viewDistance * 16 - 16) --> precalculate this OR use 512 always as the MAX
    }

    // called by WorldManager method onEntityAdded() to begin sending packets for the entity
    @Override
    public void track(@NotNull final Entity entityToTrack) {

        // taken from modern ChunkMap<> tracking methods. preliminary optimisation on the method call
        if (entityToTrack.getWorld() != nmsWorld || trackedObjects.containsKey(entityToTrack.getId())) {
            log.warn("Entity (ID: {}) is either already being tracked or its current world does not match the trackers world reference", entityToTrack.getId());
            return;
        }

        final EntityType<?> entityType = featherDisguise.getCachedEntityTypes().getEntityTypeViaReference(entityToTrack);
        if (entityType == null) {
            log.warn("Unable to fetch valid entity type from cache. Reference invalid or null. Aborting entity tracking for (ID: {}, Name: {})", entityToTrack.getId(), entityToTrack.getName());
            return;
        }

        final int trackingRange = TrackingRange.getEntityTrackingRange(entityToTrack, 0); // defaultRange is never actually used lmfao

        if (entityType.getBukkitEntityType() == org.bukkit.entity.EntityType.PLAYER) { // use our already stored bukkit api entityType (easier instead of a static call)
            this.addEntity(entityToTrack, trackingRange, entityType.getTrackerUpdateFrequency()); // in PLAYER instance situation, updateFrequency = 2

            assert entityToTrack instanceof EntityPlayer;
            final EntityPlayer nmsPlayerEntity = (EntityPlayer) entityToTrack; // stinky cast but whatever this should be safe

            // [!] now we are going to iterate over all current entities being tracked by the server.
            //     if the entity being tracked IS NOT the player, we need to send relevant packets so the client can see those entities.
            final ObjectIterator<Int2ObjectMap.Entry<FeatherEntityTrackerEntry>> objectIter = trackedObjects.int2ObjectEntrySet().fastIterator();

            while (objectIter.hasNext()) {
                final FeatherEntityTrackerEntry trackerEntry = objectIter.next().getValue();

                if (trackerEntry.tracker == nmsPlayerEntity) continue;
                trackerEntry.updatePlayer(nmsPlayerEntity);
            }
        }

        // assume is a non-player entity, handle like normal
        else this.addEntity(entityToTrack, trackingRange, entityType.getTrackerUpdateFrequency(), entityType.isTrackerAllowVelocityUpdates());
    }

    @ApiStatus.Internal
    @Override
    public void addEntity(@NotNull final Entity entityToTrack, final int trackingRange, final int updateFrequency) {
        this.addEntity(entityToTrack, trackingRange, updateFrequency, false);
    }

    @ApiStatus.Internal
    @Override
    public void addEntity(@NotNull final Entity entityToTrack, final int trackingRange, final int updateFrequency, final boolean sendVelocityPackets) {
        AsyncCatcher.catchOp("attempt to track entity"); // copied from decomp, don't allow async tracking of entities, this breaks shit, and we are trying to parody 1.8!
        Preconditions.checkNotNull(trackedObjects, "Tracked hashmap for feather entity tracker is invalid or null. Unable to begin tracking entity");

        try {

            // do not allow duplicate entities to be tracked. we want to abort the moment this flag is tripped
            if (trackedObjects.containsKey(entityToTrack.getId())) {
                throw new IllegalEntityTrackingException("Entity (ID: " + entityToTrack.getId() + ") is already being tracked!");
            }

            final FeatherEntityTrackerEntry newTrackerEntry = new FeatherEntityTrackerEntry(
                    entityToTrack,
                    Math.min(trackingRange, preCalculatedMaxEntityViewingDistance),     // can never be higher than 512 (which is overkill any-ways)
                    updateFrequency, sendVelocityPackets, featherDisguise);

            trackedObjects.put(entityToTrack.getId(), newTrackerEntry);

            // annoying but enforced due to spigot shit:
            // to ensure safety with external class method calls, also store data from
            // the superclass trackedHashTable. it's never actually used internally here
            // or ever updated by other calls, its only ever referenced by CraftPlayer
            // for the shitty vanish-API. to avoid doing un-necessary patching, we'll just
            // maintain parody with it and then properly clear when we don't need it.
            // the methods it calls we override anyway, so it should be safe.
            trackedEntities.a(entityToTrack.getId(), newTrackerEntry);

        } catch (Exception ex) {
           log.error("Failed to add entity (ID: {}, Name: {}) to the tracker", entityToTrack.getId(), entityToTrack.getName(), ex);
        }
    }

    // called by WorldManager method onEntityRemoved() to stop tracking & sending any further packets
    @Override
    public void untrackEntity(@NotNull final Entity entityToUntrack) {
        AsyncCatcher.catchOp("attempt to untrack entity"); // copied from decomp, don't allow async untracking of entities, this breaks shit, and we are trying to parody 1.8!
        Preconditions.checkNotNull(trackedObjects, "Tracked hashmap for feather entity tracker is invalid or null. Unable to untrack entity");

        if (entityToUntrack instanceof EntityPlayer) {
            final EntityPlayer nmsEntityPlayer = (EntityPlayer) entityToUntrack;

            // [!] now we are going to iterate over all current entities being tracked by the server.
            //     we need to ensure every single entry no longer continues to track the player.
            final ObjectIterator<Int2ObjectMap.Entry<FeatherEntityTrackerEntry>> objectIter = trackedObjects.int2ObjectEntrySet().fastIterator();

            while (objectIter.hasNext()) {
                final FeatherEntityTrackerEntry trackerEntry = objectIter.next().getValue();
                trackerEntry.a(nmsEntityPlayer); // removeFromTrackedPlayers()
            }
        }

        if (!trackedObjects.containsKey(entityToUntrack.getId())) return; // early exit if for whatever reason the entity we are trying to untrack doesn't exist within the data

        // yeet from the object map, this entity is no longer tracked!
        trackedEntities.d(entityToUntrack.getId()); // also remove from the shitty superclass data!
        final FeatherEntityTrackerEntry trackerEntry = trackedObjects.remove(entityToUntrack.getId());
        trackerEntry.a(); // sendDestroyEntityPacketToTrackedPlayers()

        // final method call to a() will send a destroy packet to all viewing clients.
        // causing the untracked entity to be removed from their client.
    }

    @Override
    public void untrackPlayer(@NotNull final EntityPlayer playerToUntrack) {
        Preconditions.checkNotNull(trackedObjects, "Tracked hashmap for feather entity tracker is invalid or null. Unable to untrack player");

        // optimisation: don't bother calculating packets & player updates if we have nothing to update, just a massive waste of performance
        if (trackedObjects.isEmpty()) return;

        final ObjectIterator<Int2ObjectMap.Entry<FeatherEntityTrackerEntry>> objectIter = trackedObjects.int2ObjectEntrySet().fastIterator();
        while (objectIter.hasNext()) {
            objectIter.next().getValue().clear(playerToUntrack);
        }
    }

    // called every single tick by MinecraftServer (main thread handling of entity tracking)
    @Override
    public void updatePlayers() {

        // optimisation: don't bother calculating packets & player updates if no-ones online, just a massive waste of performance
        if (nmsWorld.getMinecraftServer().getPlayerList().getPlayerCount() <= 0) return;

        Preconditions.checkNotNull(trackedObjects, "Tracked hashmap for feather entity tracker is invalid or null. Unable to update viewing players");

        final Set<EntityPlayer> playersToUpdate = new HashSet<>();
        ObjectIterator<Int2ObjectMap.Entry<FeatherEntityTrackerEntry>> entryObjectIterator = trackedObjects.int2ObjectEntrySet().fastIterator();

        while (entryObjectIterator.hasNext()) {
            final FeatherEntityTrackerEntry trackerEntry = entryObjectIterator.next().getValue();
            trackerEntry.track(nmsWorld.players);

            if (trackerEntry.n && trackerEntry.tracker instanceof EntityPlayer) { // todo this is a really cringe instanceof we could probably optimise out. inherited from superclass
                playersToUpdate.add((EntityPlayer) trackerEntry.tracker);
            }
        }

        // if we have valid players to loop over and send updates too, do so here
        if (!playersToUpdate.isEmpty()) {
            for (final EntityPlayer nmsPlayer : playersToUpdate) {
                if (nmsPlayer.getWorld() != nmsWorld) continue; // must be in the same world

                entryObjectIterator = trackedObjects.int2ObjectEntrySet().fastIterator();

                while (entryObjectIterator.hasNext()) {
                    final FeatherEntityTrackerEntry trackerEntry = entryObjectIterator.next().getValue();

                    if (trackerEntry.tracker == nmsPlayer) continue;
                    trackerEntry.updatePlayer(nmsPlayer);
                }
            }
        }
    }

    // called by EntityPlayer#updatePotionMetadata()
    // handles either updating a singular players entry or updating all players within the world
    @Override
    public void a(@NotNull final EntityPlayer nmsPlayer) {

        // optimisation: don't bother calculating packets & player updates if no-ones online, or we have nothing to update, just a massive waste of performance
        if (nmsWorld.getMinecraftServer().getPlayerList().getPlayerCount() <= 0 || trackedObjects.isEmpty()) return;

        final ObjectIterator<Int2ObjectMap.Entry<FeatherEntityTrackerEntry>> objectIter = trackedObjects.int2ObjectEntrySet().fastIterator();

        while (objectIter.hasNext()) {
            final FeatherEntityTrackerEntry trackerEntry = objectIter.next().getValue();

            // if the entries tracked entity is the provided player, update all players within the world
            // else just update the singular player within the constructor.
            // (not 100% sure why they do it this way, this method is only called in one method, so it's not too important)
            if (trackerEntry.tracker == nmsPlayer) trackerEntry.scanPlayers(nmsWorld.players);
            else trackerEntry.updatePlayer(nmsPlayer);
        }
    }

    // sendToAllTrackingEntity()
    // takes the provided packet and sends it to all player clients viewing the entity within the constructor (EXCLUDING THE TRACKED ENTITY ITSELF IF IT'S ALSO A PLAYER)
    @Override
    public void a(@NotNull final Entity entity, @NotNull final Packet packetToBroadcast) {
        this.broadcastPacketForTrackedEntity(entity, packetToBroadcast, false);
    }

    // takes the provided packet and sends it to all player clients viewing the entity within the constructor (INCLUDING THE TRACKED ENTITY ITSELF IF IT'S A PLAYER)
    @Override
    public void sendPacketToEntity(@NotNull final Entity entity, @NotNull final Packet packetToBroadcast) {
        this.broadcastPacketForTrackedEntity(entity, packetToBroadcast, true);
    }

    @SuppressWarnings("rawtypes") // mojank & spigot devs r dumb, they use raw Packet instead of Packet<?> for their tracker methods. idk take it up with them...
    @ApiStatus.Internal
    private void broadcastPacketForTrackedEntity(@NotNull final Entity entity, @NotNull final Packet packetToBroadcast, final boolean sendAlsoToOwningClient) {

        // optimisation: don't bother calculating packets & player updates if no-ones online, or we have nothing to update, just a massive waste of performance
        if (nmsWorld.getMinecraftServer().getPlayerList().getPlayerCount() <= 0 || trackedObjects.isEmpty()) return;

        // taken from modern ChunkMap<> tracking methods. preliminary optimisation on the method call
        if (entity.getWorld() != nmsWorld || !trackedObjects.containsKey(entity.getId())) {
            log.warn("Entity (ID: {}) is either not being tracked or its current world does not match the trackers world reference", entity.getId());
            return;
        }

        final FeatherEntityTrackerEntry trackerEntry = trackedObjects.get(entity.getId());
        if (trackerEntry == null) {
            log.warn("Tracker entry for entity (ID: {}) has returned null. This is very bad", entity.getId());
            throw new IllegalEntityTrackingException("Null tracker entry for entity. Aborting packet broadcast");
        }

        if (sendAlsoToOwningClient) trackerEntry.broadcastIncludingSelf(packetToBroadcast);
        else trackerEntry.broadcast(packetToBroadcast);
    }

    @Override
    public void a(@NotNull final EntityPlayer entityPlayer, @NotNull final Chunk chunk) {

        // optimisation: don't bother calculating packets & player updates if no-ones online, or we have nothing to update, just a massive waste of performance
        if (nmsWorld.getMinecraftServer().getPlayerList().getPlayerCount() <= 0 || trackedObjects.isEmpty()) return;

        final ObjectIterator<Int2ObjectMap.Entry<FeatherEntityTrackerEntry>> objectIter = trackedObjects.int2ObjectEntrySet().fastIterator();

        while (objectIter.hasNext()) {
            final FeatherEntityTrackerEntry trackerEntry = objectIter.next().getValue();

            if (trackerEntry.tracker == entityPlayer) continue; // skip over the owning tracked player

            if (trackerEntry.tracker.ae == chunk.locX && trackerEntry.tracker.ag == chunk.locZ) {
                trackerEntry.updatePlayer(entityPlayer);
            }
        }
    }

    public void checkForTrackedEntityAndRemoveBaseEntity(@NotNull final Entity trackedEntity, @NotNull final List<Player> players) {
        Preconditions.checkNotNull(trackedObjects, "Tracked hashmap for feather entity tracker is invalid or null. Unable to check for tracked entity");
        if (!trackedObjects.containsKey(trackedEntity.getId())) return;
        trackedObjects.get(trackedEntity.getId()).removeBaseEntityFromTrackedPlayers(players);
        trackedObjects.get(trackedEntity.getId()).disguiseHandled_ScanPlayers(players, false);
        // [1] specifically destroys the player entity and removes all provided players from the viewers set
        // [2] calls scan() which loops through the provided list of players and attempts to show specifically the disguise entities (& adds the viewers back into the set)
    }

    public void checkForTrackedEntityAndDestroyAllRelevantEntities(@NotNull final Entity trackedEntity) {
        Preconditions.checkNotNull(trackedObjects, "Tracked hashmap for feather entity tracker is invalid or null. Unable to check and handle updates/adds");
        if (!trackedObjects.containsKey(trackedEntity.getId())) return;
        trackedObjects.get(trackedEntity.getId()).sendDestroyAndClearTrackedPlayers();
        // destroys either the player tracked entity OR the disguise tracked entities (also removed from the viewers set)
    }

    public void checkForTrackedEntityAndRescanForDisguises(@NotNull final Entity trackedEntity, @NotNull final List<Player> players, final boolean isShowingBaseEntity) {
        Preconditions.checkNotNull(trackedObjects, "Tracked hashmap for feather entity tracker is invalid or null. Unable to check and handle updates/adds");
        if (!trackedObjects.containsKey(trackedEntity.getId())) return;
        trackedObjects.get(trackedEntity.getId()).disguiseHandled_ScanPlayers(players, isShowingBaseEntity);
        // loops through all provided players and will call updatePlayer() individually (which will either sent spawning packets for the player entity OR disguise entities)
    }

    @Override
    public void teardown() {
        trackedObjects.clear();
        trackedEntities.c();
        // teardown is called only when the world is being unloaded,
        // so we don't have to care about any packets or entities, just straight clear the data
    }
}
