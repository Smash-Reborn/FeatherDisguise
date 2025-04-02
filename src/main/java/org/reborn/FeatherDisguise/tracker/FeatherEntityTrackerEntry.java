package org.reborn.FeatherDisguise.tracker;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.reborn.FeatherDisguise.FeatherDisguise;
import org.reborn.FeatherDisguise.metadata.EntityType;
import org.reborn.FeatherDisguise.types.AbstractDisguise;
import org.spigotmc.AsyncCatcher;

import java.util.*;

public class FeatherEntityTrackerEntry extends EntityTrackerEntry {

    /*
     * ## ACCESSIBLE FIELDS FROM SUPERCLASS ##
     *
     * Entity entityBeingTracked -> [tracker]
     * int trackingDistanceThreshold -> [b]
     * int updateFrequency -> [c]
     *
     * int xPosition -> [xLoc]
     * int yPosition -> [yLoc]
     * int zPosition -> [zLoc]
     *
     * int yawRotation -> [yRot]
     * int pitchRotation -> [xRot]
     * int lastHeadRotation -> [i]
     *
     * double xMotion -> [j]
     * double yMotion -> [k]
     * double zMotion -> [l]
     *
     * int updateCounter -> [m]
     * boolean havePlayersBeenUpdated -> [n]
     * Set<EntityPlayer> playersWhoCanSeeTheTrackedEntity -> [trackedPlayers]
     */

    // ## NON-ACCESSIBlE FIELDS (implemented locally as public) ##
    public double lastTrackedXPosition;
    public double lastTrackedYPosition;
    public double lastTrackedZPosition;

    public boolean hasMovedFromInitialPosition;
    public boolean isSendingVelocityPackets;

    public int ticksSinceLastForcedTeleportSynchronisation;
    public int maximumAllowedTicksUntilForcedTeleportSynchronisation;
    public boolean isFlaggedOnGround;

    @Nullable public Entity passengerEntity;
    public boolean isRidingAnotherEntity;

    @ApiStatus.Internal
    @NotNull private final FeatherDisguise featherDisguise;

    public FeatherEntityTrackerEntry(@NotNull final Entity entityBeingTracked, final int trackingDistanceThreshold, final int updateFrequency, final boolean allowVelocityPacketUpdates,
                                     final int maximumAllowedTicksUntilSynchronisation, @NotNull final FeatherDisguise featherDisguise) {

        super(entityBeingTracked, trackingDistanceThreshold, updateFrequency, allowVelocityPacketUpdates);
        this.featherDisguise = featherDisguise;

        this.hasMovedFromInitialPosition = false;
        this.isSendingVelocityPackets = allowVelocityPacketUpdates;
        this.ticksSinceLastForcedTeleportSynchronisation = 0;
        this.maximumAllowedTicksUntilForcedTeleportSynchronisation = maximumAllowedTicksUntilSynchronisation;
        this.isFlaggedOnGround = entityBeingTracked.onGround;
        this.passengerEntity = entityBeingTracked.passenger != null ? entityBeingTracked.passenger : null;
    }

    public FeatherEntityTrackerEntry(@NotNull final Entity entityBeingTracked, final int trackingDistanceThreshold, final int updateFrequency, final boolean allowVelocityPacketUpdates,
                                     @NotNull final FeatherDisguise featherDisguise) {

        this(entityBeingTracked, trackingDistanceThreshold, updateFrequency, allowVelocityPacketUpdates, 400, featherDisguise);
        // default constructor, although in our case we can accommodate for custom synchronisation times
    }

    @Override
    public void track(@NotNull final List<EntityHuman> list) {
        this.n = false;     // havePlayersBeenUpdated

        // tracked entity hasn't initially moved or the euclidean dist is further than 16 units
        if (!hasMovedFromInitialPosition || tracker.e(lastTrackedXPosition, lastTrackedYPosition, lastTrackedZPosition) > 16.0d) {  // tracker.getDistSqrd()
            lastTrackedXPosition = tracker.locX;
            lastTrackedYPosition = tracker.locY;
            lastTrackedZPosition = tracker.locZ;
            hasMovedFromInitialPosition = true;
            this.n = true; // havePlayersBeenUpdated
            this.scanPlayers(list);
        }

        // tracked entity has a valid vehicle (riding something)
        // ( (this.m % 60 == 0) is basically a modulo designed to ensure the flag
        //  check succeeds every 3 seconds. so every 3 seconds it'll send out a packet in
        //  an attempt to ensure the tracked entity is properly riding the vehicle to all viewing clients )     // stupid ik
        if (passengerEntity != tracker.vehicle || (tracker.vehicle != null && this.m % 60 == 0)) {
            passengerEntity = tracker.vehicle;
            // todo ensure disguises correctly handle vehicle packets
            this.broadcast(new PacketPlayOutAttachEntity(0, tracker, tracker.vehicle));
        }

        // logic here specifically to handle jank item frame stuff
        if (tracker instanceof EntityItemFrame) {
            final EntityItemFrame entityItemFrame = (EntityItemFrame) tracker;
            final ItemStack itemStack = entityItemFrame.getItem();

            if (this.m % 10 == 0 && itemStack != null && itemStack.getItem() instanceof ItemWorldMap) {
                if (!trackedPlayers.isEmpty()) { // optimisation: if no players are currently being handled, don't bother iterating
                    final Iterator<EntityPlayer> playerIter = trackedPlayers.iterator();
                    final WorldMap worldMap = Items.FILLED_MAP.getSavedMap(itemStack, tracker.world);

                    while (playerIter.hasNext()) {
                        final EntityPlayer entityPlayer = playerIter.next();
                        worldMap.a(entityPlayer, itemStack);

                        @SuppressWarnings("rawtypes") // 2014 mojang employees try to type good code challenge, difficulty impossible
                        final Packet mapPacket = Items.FILLED_MAP.c(itemStack, tracker.world, entityPlayer);
                        if (mapPacket != null) {entityPlayer.playerConnection.sendPacket(mapPacket);}
                    }
                }
            }

            // regardless of what happens in the code block above
            // always send metadata to players if it's an item-frame
            this.sendDataUpdatesToAllViewersIncludingSelf();
        }

        // "this is where the fun begins"

        // this.m % this.c == 0 --> updateCounter % updateFrequency == 0
        // tracker.ai --> tracker.isAirBorne
        // tracker.getDataWatcher().a() --> tracker.getDataWatcher().hasObjectChanged()

        if (this.m % this.c == 0 || tracker.ai || tracker.getDataWatcher().a()) {

            // re-using these to save on heap (spigot does similar)
            int a;
            int b;

            // dynamically fetch their disguise (but only if the tracked entity is a player w/ valid disguise, else is null)
            final AbstractDisguise<?> disguisedEntity = this.getTrackedEntityDisguise();

            // entity being tracked isn't riding anything (so either controlled by serverAI or is a player and therefore controlled by client)
            if (tracker.vehicle == null) {
                ++ticksSinceLastForcedTeleportSynchronisation;

                // set vars for de-coded position
                a = MathHelper.floor(tracker.locX * 32.0d);
                b = MathHelper.floor(tracker.locY * 32.0d);
                final int decodedLocZ = MathHelper.floor(tracker.locZ * 32.0d);

                // set vars for de-coded rotations
                final int decodedYaw = MathHelper.d(tracker.yaw * 256.0f / 360.0f);
                final int decodedPitch = MathHelper.d(tracker.pitch * 256.0f / 360.0f);

                // get absolute position
                final int absoluteLocX = a - this.xLoc;
                final int absoluteLocY = b - this.yLoc;
                final int absoluteLocZ = decodedLocZ - this.zLoc;

                // originally was a singular packet, but because we are including our disguise
                // related packets, we need to instead have a list because there are potentially more than 1 we are sending
                final List<Packet<?>> packetsToSend = new ArrayList<>();

                // update fields only if either flag is true
                final boolean isRelativeMovement = Math.abs(absoluteLocX) >= 4 || Math.abs(absoluteLocY) >= 4 || Math.abs(absoluteLocZ) >= 4 || this.m % 60 == 0;   // changing this makes packets get updated more frequently
                final boolean isRelativeRotation = Math.abs(decodedYaw - this.yRot) >= 4 || Math.abs(decodedPitch - this.xRot) >= 4;

                if (isRelativeMovement) {
                    this.xLoc = a;
                    this.yLoc = b;
                    this.zLoc = decodedLocZ;
                }

                if (isRelativeRotation) {
                    this.yRot = decodedYaw;
                    this.xRot = decodedPitch;
                }

                // we will now attempt to calculate and generate packets for rel pos, rot, teleport and velocity
                if (this.m > 0 || tracker instanceof EntityArrow) {

                    // if absolute values are between -128 and 127 bytes,
                    // and we aren't teleport synchronising and other flags are passed,
                    // we will assume we can handle the packets we want!
                    if (
                            absoluteLocX >= -128 && absoluteLocX < 128 &&
                            absoluteLocY >= -128 && absoluteLocY < 128 &&
                            absoluteLocZ >= -128 && absoluteLocZ < 128 &&
                            ticksSinceLastForcedTeleportSynchronisation <= maximumAllowedTicksUntilForcedTeleportSynchronisation &&
                            !isRidingAnotherEntity && isFlaggedOnGround == tracker.onGround) {

                        // determine only ONE type of packet (either relative pos or rot)
                        // if it's both, there is a separate packet that will handle both.
                        // (arrows can never be either packet, they must exclusively be handled as either relPosRot or teleports)
                        if ((!isRelativeMovement || !isRelativeRotation) && !(tracker instanceof EntityArrow)) {

                            // relative pos update --> posXYZ is between the byte values
                            if (isRelativeMovement) {

                                // more common outcome it's likely not a disguised player (save cycles?)
                                if (disguisedEntity == null) {
                                    packetsToSend.add(new PacketPlayOutEntity.PacketPlayOutRelEntityMove(tracker.getId(),
                                            (byte) absoluteLocX, (byte) absoluteLocY, (byte) absoluteLocZ, tracker.onGround));
                                }

                                // they are a disguised player, handle packets accordingly
                                else {
                                    packetsToSend.add(new PacketPlayOutEntity.PacketPlayOutRelEntityMove(disguisedEntity.getRelatedEntitiesWrapper().getBaseDisguiseEntity().getVirtualID(),
                                            (byte) absoluteLocX, (byte) absoluteLocY, (byte) absoluteLocZ, tracker.onGround));
                                    packetsToSend.add(new PacketPlayOutEntity.PacketPlayOutRelEntityMove(disguisedEntity.getRelatedEntitiesWrapper().getHittableSquidEntity().getVirtualID(),
                                            (byte) absoluteLocX, (byte) absoluteLocY, (byte) absoluteLocZ, false));
                                }
                            }

                            // relative rot update --> yaw/pitch is between the int values
                            else if (isRelativeRotation) {

                                if (disguisedEntity == null) {
                                    packetsToSend.add(new PacketPlayOutEntity.PacketPlayOutEntityLook(tracker.getId(),
                                            (byte) decodedYaw, (byte) decodedPitch, tracker.onGround));
                                }

                                else {
                                    packetsToSend.add(new PacketPlayOutEntity.PacketPlayOutEntityLook(disguisedEntity.getRelatedEntitiesWrapper().getBaseDisguiseEntity().getVirtualID(),
                                            disguisedEntity.isHeadRotationYawLocked() ? (byte) 0 : (byte) decodedYaw,
                                            disguisedEntity.isHeadRotationPitchLocked() ? (byte) 0 : (byte) decodedPitch, tracker.onGround));
                                    // for relative rot packets, we only need to handle for the base disguise entity
                                    // (other related disguise entities do not care about handling rotations since they are always invisible)
                                }
                            }
                        }

                        // determined that we are sending BOTH relative pos & rot.
                        else {

                            if (disguisedEntity == null) {
                                packetsToSend.add(new PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook(tracker.getId(),
                                        (byte) absoluteLocX, (byte) absoluteLocY, (byte) absoluteLocZ, (byte) decodedYaw, (byte) decodedPitch, tracker.onGround));
                            }

                            else {
                                packetsToSend.add(new PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook(disguisedEntity.getRelatedEntitiesWrapper().getBaseDisguiseEntity().getVirtualID(),
                                        (byte) absoluteLocX, (byte) absoluteLocY, (byte) absoluteLocZ,
                                        disguisedEntity.isHeadRotationYawLocked() ? (byte) 0 : (byte) decodedYaw,
                                        disguisedEntity.isHeadRotationPitchLocked() ? (byte) 0 : (byte) decodedPitch, tracker.onGround));
                                packetsToSend.add(new PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook(disguisedEntity.getRelatedEntitiesWrapper().getHittableSquidEntity().getVirtualID(),
                                        (byte) absoluteLocX, (byte) absoluteLocY, (byte) absoluteLocZ, (byte) 0, (byte) 0, false));
                            }
                        }
                    }

                    // calculations are above the byte threshold OR we are forcibly
                    // sending teleport packets for synchronisation. handle accordingly
                    else {

                        isFlaggedOnGround = tracker.onGround;
                        ticksSinceLastForcedTeleportSynchronisation = 0; // reset the incrementing counter

                        // CraftBukkit patch - refresh list of who can see the tracked entity (if it's a player) before sending teleport packet
                        // (this is to ensure they are actually visible to all viewing clients, else sending the packet might cause it to not work on the client cos it's not "visible" to them)
                        if (tracker instanceof EntityPlayer) {this.s_scanPlayers(trackedPlayers);}

                        if (disguisedEntity == null) {
                            packetsToSend.add(new PacketPlayOutEntityTeleport(tracker.getId(),
                                    a, b, decodedLocZ, (byte) decodedYaw, (byte) decodedPitch, tracker.onGround));
                        }

                        else {
                            packetsToSend.add(new PacketPlayOutEntityTeleport(disguisedEntity.getRelatedEntitiesWrapper().getBaseDisguiseEntity().getVirtualID(),
                                    a, b, decodedLocZ,
                                    disguisedEntity.isHeadRotationYawLocked() ? (byte) 0 : (byte) decodedYaw,
                                    disguisedEntity.isHeadRotationPitchLocked() ? (byte) 0 : (byte) decodedPitch, tracker.onGround));
                            packetsToSend.add(new PacketPlayOutEntityTeleport(disguisedEntity.getRelatedEntitiesWrapper().getHittableSquidEntity().getVirtualID(),
                                    a,
                                    MathHelper.floor(disguisedEntity.getCalculatedSquidRelatedEntityYPos(tracker.locY) * 32.0d),
                                    decodedLocZ, (byte) 0, (byte) 0, false));
                        }
                    }
                }

                // now handle velocity for the tracked entity
                if (isSendingVelocityPackets) {
                    final double absoluteMotX = tracker.motX - this.j;
                    final double absoluteMotY = tracker.motY - this.k;
                    final double absoluteMotZ = tracker.motZ - this.l;
                    final double sqrt = absoluteMotX * absoluteMotX + absoluteMotY * absoluteMotY + absoluteMotZ * absoluteMotZ; // really cringe, but im not sure how we would even optimise this

                    // quicc maffs to check and see if we should be updating velocity
                    if (sqrt > 0.0004f || (sqrt > 0.0f && tracker.motX == 0.0d && tracker.motY == 0.0d && tracker.motZ == 0.0d)) {
                        this.j = tracker.motX;
                        this.k = tracker.motY;
                        this.l = tracker.motZ;

                        // note: we don't need to handle velocity packets within the list because the VelocityPacketDistributor
                        //       is always active regardless of PacketHandlingType. so that will handle velocity updates for us instead.
                        this.broadcast(new PacketPlayOutEntityVelocity(tracker.getId(), this.j, this.k, this.l));
                        // for this specific case, velocity must be sent first. do not put into the packet list.
                        // this is a bug carried over from the superclass, where sending velocity post pos update
                        // causes certain entities (like arrows) to become de-synchronised from their positions clientside -> serverside.
                        // not sure the best way to fix this, but for now doing it this way parodies how the tracker previously would work
                    }
                }

                // woo-hoo! we have packets we want to send to clients! bEHoLd mY maGiKs >!>!>!**(_@^>>_+&
                if (!packetsToSend.isEmpty()) {
                    this.broadcastPackets(packetsToSend);
                }

                // b()
                this.sendDataUpdatesToAllViewersIncludingSelf();
                isRidingAnotherEntity = false;
            }

            // tracked entity is currently riding a vehicle
            // (so the following section determines "riding" logic)
            // (it's literally just head rotations for packets that's it)
            else {
                a = MathHelper.d(tracker.yaw * 256.0f / 360.0f);
                b = MathHelper.d(tracker.pitch * 256.0f / 360.0f);

                final boolean hasRotChanged = Math.abs(a - this.yRot) >= 4 || Math.abs(b - this.xRot) >= 4;

                if (hasRotChanged) {
                    this.yRot = a;
                    this.xRot = b;
                    this.broadcast(new PacketPlayOutEntity.PacketPlayOutEntityLook(
                            disguisedEntity == null ? tracker.getId() : disguisedEntity.getRelatedEntitiesWrapper().getBaseDisguiseEntity().getVirtualID(),
                            (byte) a, (byte) b, tracker.onGround));
                }

                // update fields for local positions
                // (even though we are riding, we still need to ensure we are keeping track of where the entity is)
                this.xLoc = MathHelper.floor(tracker.locX * 32.0d);
                this.yLoc = MathHelper.floor(tracker.locY * 32.0d);
                this.zLoc = MathHelper.floor(tracker.locZ * 32.0d);

                // b()
                this.sendDataUpdatesToAllViewersIncludingSelf();
                isRidingAnotherEntity = true;
            }

            // head rotation packets
            // (this is calculated regardless of whether the tracked entity is riding something or not)
            // (a similar thing is found within our distributors class for HeadRot)
            a = MathHelper.d(tracker.getHeadRotation() * 256.0f / 360.0f);

            // greater than 4 units, allow the head rotation to be updated
            if (Math.abs(a - this.i) >= 4) {
                this.i = a;
                final PacketPlayOutEntityHeadRotation headRotPacket = new PacketPlayOutEntityHeadRotation(
                        disguisedEntity == null ? tracker : disguisedEntity.getRelatedEntitiesWrapper().getBaseDisguiseEntity().getVirtualEntity(),
                        disguisedEntity != null && disguisedEntity.isHeadRotationYawLocked() ? (byte) 0 : (byte) a);
                this.broadcast(headRotPacket);
                // stupid way to do this, directly calling the virtual entity (even though we shouldn't need to).
                // because packetdataserializer is very finiky and not stable at all in these versions...
            }

            // tracker.ai --> tracker.isAirBorne
            tracker.ai = false;
        }

        // increment update counter
        ++this.m;

        // if the entities velocity has been modified, prepare to send velocity to everyone
        if (tracker.velocityChanged) {

            // below is basically the same as the superclass, some CraftBukkit event patch
            boolean cancelled = false;

            if (tracker instanceof EntityPlayer) {
                final Player player = (Player) tracker.getBukkitEntity();
                final Vector velocity = player.getVelocity();

                final PlayerVelocityEvent velocityEvent = new PlayerVelocityEvent(player, velocity);
                tracker.world.getServer().getPluginManager().callEvent(velocityEvent);

                if (velocityEvent.isCancelled()) cancelled = true;
                else if (!velocity.equals(velocityEvent.getVelocity())) player.setVelocity(velocity);
            }

            if (!cancelled) {
                this.broadcastIncludingSelf(new PacketPlayOutEntityVelocity(tracker));
            }

            // update the flag, since now the velocity packet has been sent
            tracker.velocityChanged = false;
        }
    }

    @Override
    public void updatePlayer(@NotNull final EntityPlayer entityPlayer) {
        this.internalUpdatePlayer(entityPlayer, true);
    }

    public void updatePlayerForDisguises(@NotNull final EntityPlayer entityPlayer) {
        this.internalUpdatePlayer(entityPlayer, false);
    }

    @ApiStatus.Internal
    private void internalUpdatePlayer(@NotNull final EntityPlayer entityPlayer, final boolean allowSpawningBasicEntity) {
        AsyncCatcher.catchOp("updatePlayer() tracker method"); // copied from decomp, don't allow async updates of players, this breaks shit, and we are trying to parody 1.8!

        // make sure the player we are attempting to update is not the same as the tracked entity
        // (in decomp, they do a raw != check, using IDs is just simpler)
        if (entityPlayer.getId() == tracker.getId()) return;

        // isWithinChunkTrackingRangeThreshold()
        // they are within range, we are going to do SPAWNING packet(s)
        if (this.c(entityPlayer)) {

            // player ISN'T within the trackedPlayers set (meaning they have never received spawning packet(s))
            // they also either have to be within the same chunk(s) as the tracked entity or "attached" (either riding or leashed) to the tracked entity
            if (!trackedPlayers.contains(entityPlayer) && (this.isPlayerWatchingThisChunk(entityPlayer) || tracker.attachedToPlayer)) {

                // CraftBukkit - respect the shitty vanishAPI
                if (tracker instanceof EntityPlayer) {
                    if (!entityPlayer.getBukkitEntity().canSee(((EntityPlayer) tracker).getBukkitEntity())) return;
                    // if the player being updated "cannot see" the tracked entity, we can early exit
                }

                if (entityPlayer.removeQueue.contains(tracker.getId())) {
                    entityPlayer.removeQueue.remove(tracker.getId()); // remove from vanishAPI queue
                }

                // [!] add them to the tracked set, they are now considered as having been sent SPAWNING packets
                trackedPlayers.add(entityPlayer);

                // now handle sending relevant spawning packet(s)
                // (either it will be disguise related packets or vanilla behavior)
                this.handleSpawningPacketsForTrackedEntity(entityPlayer, allowSpawningBasicEntity);
            }
        }

        // they are outside range, we are going to do DESTROY packet(s)
        else if (trackedPlayers.contains(entityPlayer)) {
            this.a(entityPlayer);
            // (decomp manually calls methods, but it's exactly the same as a())
        }
    }

    @Override
    public void scanPlayers(@NotNull final List<EntityHuman> list) {
        if (list.isEmpty()) return; // optimisation: don't bother if the list is empty, waste of process

        for (final EntityHuman humanViewer : list) {
            if (humanViewer instanceof EntityPlayer) {
                this.updatePlayer((EntityPlayer) humanViewer);
                // i genuinely have no idea why tf mojank have a list of EntityHuman instead of just using EntityPlayer...
            }
        }
    }

    // my spicy method for doing scanPlayers() with the list of EntityPlayer
    // (because the stupid tracking method creates new arrays every tick with the same trackedPlayers list and that's just retarded)
    @ApiStatus.Internal
    private void s_scanPlayers(@NotNull final Set<EntityPlayer> setPlayers) {
        if (setPlayers.isEmpty()) return; // optimisation: don't bother if the list is empty, waste of process

        for (final EntityPlayer playerViewer : setPlayers) {
            this.updatePlayer(playerViewer);
        }
    }

    // same method as above, except this is specifically for disguise related logic
    public void disguiseHandled_ScanPlayers(@NotNull final List<Player> collectPlayers, final boolean isShowingBaseEntity) {
        if (collectPlayers.isEmpty()) return; // optimisation: don't bother if the list is empty, waste of process

        for (final Player playerViewer : collectPlayers) {
            final EntityPlayer nmsPlayer = ((CraftPlayer) playerViewer).getHandle();

            if (isShowingBaseEntity) this.updatePlayer(nmsPlayer);
            else this.updatePlayerForDisguises(nmsPlayer);
        }
    }

    public void forcePositionRotationSynchronisation() {
        this.m = this.c; // make the updateCounter = updateFrequency to force packet calculations
        this.ticksSinceLastForcedTeleportSynchronisation = this.maximumAllowedTicksUntilForcedTeleportSynchronisation + 1; // make ticks counter greater than the max, forcing a teleport synchronisation
    }

    @Override
    public void broadcast(@NotNull final Packet packet) {
        if (trackedPlayers == null || trackedPlayers.isEmpty()) return; // optimization: don't bother iterating if we have no one to send them too

        for (final EntityPlayer trackedPlayer : trackedPlayers) {
            trackedPlayer.playerConnection.sendPacket(packet);
        }
    }

    @Override
    public void broadcastIncludingSelf(@NotNull final Packet packet) {
        this.broadcast(packet);

        if (tracker instanceof EntityPlayer) {
            ((EntityPlayer) tracker).playerConnection.sendPacket(packet);
        }
    }

    @ApiStatus.Internal
    private void broadcastPackets(@NotNull final List<Packet<?>> packets) {
        if (packets.isEmpty() || trackedPlayers == null || trackedPlayers.isEmpty()) return;

        // loop over all players being handled, send them all the packets we want
        for (final EntityPlayer trackedPlayer : trackedPlayers) {
            for (final Packet<?> packetToSend : packets) {
                trackedPlayer.playerConnection.sendPacket(packetToSend);
            }
        }
    }

    // superclass method is b() -> sendMetadataToAllPlayers()
    // responsible for sending metadata and attribute packets to all viewing clients including the owner
    public void sendDataUpdatesToAllViewersIncludingSelf() {
        final DataWatcher dataWatcher = tracker.getDataWatcher();
        final AbstractDisguise<?> trackedEntityDisguise = this.getTrackedEntityDisguise();

        if (dataWatcher.a()) { // dataWatcher.hasObjectChanged() --> or in other words, has the metadata changed from the previous tick

            // most likely situation is that it's not a disguised player, so handle like normal
            if (trackedEntityDisguise == null) {
                this.broadcastIncludingSelf(new PacketPlayOutEntityMetadata(tracker.getId(), dataWatcher, false));
            }

            // if it is a disguised player, things get a little more complicated.
            // essentially we still want to send the "entityPlayer" metadata to the tracked player entity.
            // however, for all other viewers, they are seeing the disguise entity. so we need to instead send them
            // specific metadata packets for our disguise entity.
            else {
                assert tracker instanceof EntityPlayer; // safe because getTrackedEntityDisguise() has already validated that it's an EntityPlayer
                final EntityPlayer trackedEntityPlayer = (EntityPlayer) tracker;

                // [1] send the owning client his own metadata, specifically the EntityPlayer data
                trackedEntityPlayer.playerConnection.sendPacket(new PacketPlayOutEntityMetadata(tracker.getId(), dataWatcher, false));

                // [2] for all other viewers, we are going to send them the disguise metadata (fucking this up will cause all other clients to crash!)
                if (!trackedPlayers.isEmpty()) {
                    final WrapperPlayServerEntityMetadata PE_MetadataPacket = trackedEntityDisguise.getRelatedEntitiesWrapper().updateRelevantFieldsAndReturnMetadataPacketForBaseEntity();

                    for (final EntityPlayer viewingPlayer : trackedPlayers) {
                        if (viewingPlayer.getId() == trackedEntityPlayer.getId()) continue; // 10000% DO NOT SEND THE TRACKED OWNER THIS PACKET ELSE THEY WILL CRASH!
                        PacketEvents.getAPI().getPlayerManager().sendPacketSilently(viewingPlayer.getBukkitEntity(), PE_MetadataPacket);
                    }
                }
            }
        }

        if (tracker instanceof EntityLiving) {
            final EntityLiving nmsLivingTracker = (EntityLiving) tracker;
            final AttributeMapServer attributeMapServer = (AttributeMapServer) nmsLivingTracker.getAttributeMap();

            final Set<AttributeInstance> attributes = attributeMapServer.getAttributes();
            if (attributes.isEmpty()) return;

            // CraftBukkit patch - send scaled max health
            if (tracker instanceof EntityPlayer) {
                ((EntityPlayer) tracker).getBukkitEntity().injectScaledMaxHealth(attributes, false);
            }

            // only send attributes packet if the tracked entity ISN'T disguised
            // (i don't think it's necessary to update attributes for the disguise, they aren't used???)
            if (trackedEntityDisguise == null) {
                this.broadcastIncludingSelf(new PacketPlayOutUpdateAttributes(tracker.getId(), attributes));
            }

            attributes.clear();
        }
    }

    @ApiStatus.Internal
    private void determineAndDestroyEntity(@NotNull final EntityPlayer viewer) {

        // if the tracked entity is a disguised player, we need to call our disguise api
        // to correctly handle removing all relevant disguise related entities from the viewing client
        if (tracker instanceof EntityPlayer && featherDisguise.getDisguiseAPI().isPlayerCurrentlyDisguised(((EntityPlayer) tracker).getBukkitEntity())) {
            featherDisguise.getDisguiseAPI().getPlayerDisguise(((EntityPlayer) tracker).getBukkitEntity())
                    .ifPresent(disguise ->
                            featherDisguise.getDisguiseAPI().hideDisguiseForPlayer(disguise, viewer.getBukkitEntity(), false));
        }

        // else assume not a disguised player, handle destroys like normal superclass
        else {viewer.d(tracker);} // removeEntity()
    }

    // sendDestroyEntityPacketToTrackedPlayers()
    @Override
    public void a() {
        if (trackedPlayers == null || trackedPlayers.isEmpty()) return; // optimization: don't bother iterating if we have no one to send them too

        for (final EntityPlayer viewingClient : trackedPlayers) {
            this.determineAndDestroyEntity(viewingClient);
        }
    }

    public void sendDestroyAndClearTrackedPlayers() {
        if (trackedPlayers == null || trackedPlayers.isEmpty()) return;  // optimization: don't bother iterating if we have no one to send them too

        final Iterator<EntityPlayer> viewersIter = trackedPlayers.iterator();
        while (viewersIter.hasNext()) {
            this.determineAndDestroyEntity(viewersIter.next());
            viewersIter.remove();
        }
    }

    // removeFromTrackedPlayers()
    @Override
    public void a(@NotNull final EntityPlayer entityPlayer) {
        if (trackedPlayers == null) return; // we would be fucked to begin with o_O

        if (!trackedPlayers.contains(entityPlayer)) return; // they aren't being tracked!

        trackedPlayers.remove(entityPlayer);
        this.determineAndDestroyEntity(entityPlayer);
    }

    public void removeBaseEntityFromTrackedPlayers(@NotNull final List<Player> players) {
        if (players.isEmpty()) return;

        for (final Player viewerPlayer : players) {
            final EntityPlayer nmsPlayer = ((CraftPlayer) viewerPlayer).getHandle();

            if (!trackedPlayers.contains(nmsPlayer)) continue;

            trackedPlayers.remove(nmsPlayer);
            nmsPlayer.d(tracker);
        }
    }

    @Override
    public void clear(@NotNull final EntityPlayer entityPlayer) {
        AsyncCatcher.catchOp("viewing player tracker clear call"); // copied from decomp, don't allow async clearing of player clients, this breaks shit, and we are trying to parody 1.8!
        this.a(entityPlayer);
        // idk why mojank or spigot made 2 methods that literally do the same thing. the exception is this clear() method
        // has an async catch while the a() method doesn't regardless they do the same thing, so just make them share the logic.
        // ( clear() is called by other external classes, so maybe they needed to ensure thread safety? but it is a stupid way to write it regardless )
    }

    @ApiStatus.Internal
    @Nullable private Packet<?> generateSpawnPacketForTrackedEntity() {
        if (tracker.dead) return null;

        final EntityType<?> entityType = featherDisguise.getCachedEntityTypes().getEntityTypeViaReference(tracker); // lazy ass >:
        if (entityType == null) return null;

        return entityType.getSpawningPacketFromEntity(tracker);
    }

    @ApiStatus.Internal
    private void handleSpawningPacketsForTrackedEntity(@NotNull final EntityPlayer entityPlayer, final boolean allowSpawningBasicEntity) {

        // [?] if the tracked entity is a player and that player is disguised, instead we want to
        //     call our api method to send spawn packets for all our disguise related entities, that way,
        //     instead of seeing the EntityPlayer, they see the Disguise and its entities instead
        if (tracker instanceof EntityPlayer && featherDisguise.getDisguiseAPI().isPlayerCurrentlyDisguised(((EntityPlayer) tracker).getBukkitEntity())) {
            final Optional<AbstractDisguise<?>> optDisguise = featherDisguise.getDisguiseAPI().getPlayerDisguise(((EntityPlayer) tracker).getBukkitEntity());
            optDisguise.ifPresent(disguise -> featherDisguise.getDisguiseAPI().showDisguiseForPlayer(disguise, entityPlayer.getBukkitEntity()));
        }

        // either not a player or not disguised, regardless handle entity like normal
        else {

            // when refreshing disguises, we might utilise this flag to ensure the player entity doesn't get added back in
            if (allowSpawningBasicEntity) {
                final Packet<?> spawnPacket = this.generateSpawnPacketForTrackedEntity();
                if (spawnPacket == null)
                    return; // if we for whatever reason return a null spawn packet, we are fucked so early exit

                entityPlayer.playerConnection.sendPacket(spawnPacket); // player can now see the tracked entity on their client

                // metadata next (without metadata, the client will crash)
                if (tracker.getDataWatcher().d()) {
                    entityPlayer.playerConnection.sendPacket(new PacketPlayOutEntityMetadata(tracker.getId(), tracker.getDataWatcher(), true));
                }

                // CraftBukkit patch - fix for nonsensical head yaw
                // (applying our own fix, which is based off a paper-spigot patch. only send the head rot packet to the viewer client)
                this.i = MathHelper.d(tracker.getHeadRotation() * 256.0f / 360.0f);
                entityPlayer.playerConnection.sendPacket(new PacketPlayOutEntityHeadRotation(tracker, (byte) this.i));

                // next do any relevant nbt data for the entity
                final NBTTagCompound NBT_Tag = tracker.getNBTTag();
                if (NBT_Tag != null) {
                    entityPlayer.playerConnection.sendPacket(new PacketPlayOutUpdateEntityNBT(tracker.getId(), NBT_Tag));
                }

                // send a velocity packet if we are allowing it & if the spawn packet being sent isn't living based
                // (this is because mojank is stupid and decided not to add delta fields to the base entity spawning packet constructor)
                if (isSendingVelocityPackets && !(spawnPacket instanceof PacketPlayOutSpawnEntityLiving)) {
                    entityPlayer.playerConnection.sendPacket(new PacketPlayOutEntityVelocity(tracker.getId(), tracker.motX, tracker.motY, tracker.motZ));
                }

                // if the entity being tracked is riding something, ensure to the viewing client they are also shown as riding the vehicle
                if (tracker.vehicle != null) {
                    entityPlayer.playerConnection.sendPacket(new PacketPlayOutAttachEntity(0, tracker, tracker.vehicle));
                }

                // if the entity being tracked is leashed, ensure to the viewing client they are also shown as being leashed
                if (tracker instanceof EntityInsentient && ((EntityInsentient) tracker).getLeashHolder() != null) {
                    entityPlayer.playerConnection.sendPacket(new PacketPlayOutAttachEntity(1, tracker, ((EntityInsentient) tracker).getLeashHolder()));
                }

                // now do living entity based packets
                if (tracker instanceof EntityLiving) {
                    final EntityLiving livingTrackedEntity = (EntityLiving) tracker;

                    // [!] attributes packet
                    final Collection<AttributeInstance> attributes = ((AttributeMapServer) livingTrackedEntity.getAttributeMap()).c();

                    // CraftBukkit patch - if sending own attributes, send scaled health instead of current maximum health
                    if (tracker.getId() == entityPlayer.getId()) {
                        ((EntityPlayer) tracker).getBukkitEntity().injectScaledMaxHealth(attributes, false);
                    }

                    if (!attributes.isEmpty()) {
                        entityPlayer.playerConnection.sendPacket(new PacketPlayOutUpdateAttributes(tracker.getId(), attributes));
                    }

                    attributes.clear();

                    // [!] equipment packet
                    for (int i = 0; i < 5; ++i) {
                        final ItemStack equipmentItem = livingTrackedEntity.getEquipment(i);
                        if (equipmentItem == null) continue;
                        entityPlayer.playerConnection.sendPacket(new PacketPlayOutEntityEquipment(tracker.getId(), i, equipmentItem));
                    }

                    // [!] potion effect packet
                    final Collection<MobEffect> effects = livingTrackedEntity.getEffects();

                    if (!effects.isEmpty()) {
                        for (final MobEffect effect : effects) {
                            entityPlayer.playerConnection.sendPacket(new PacketPlayOutEntityEffect(tracker.getId(), effect));
                        }
                    }

                    effects.clear();
                }

                // now do human based packets
                if (tracker instanceof EntityHuman) {
                    final EntityHuman humanTrackedEntity = (EntityHuman) tracker;

                    // ensure to the viewing client they are sleeping
                    if (humanTrackedEntity.isSleeping()) {
                        entityPlayer.playerConnection.sendPacket(new PacketPlayOutBed(humanTrackedEntity, new BlockPosition(humanTrackedEntity)));
                    }
                }
            }
        }

        // set fields for storing local tracked delta values
        // (this happens regardless of what packets we send, as they get used via the update() method)
        this.j = tracker.motX;
        this.k = tracker.motY;
        this.l = tracker.motZ;

    }

    @ApiStatus.Internal
    private boolean isPlayerWatchingThisChunk(@NotNull final EntityPlayer entityPlayer) {
        return entityPlayer.u().getPlayerChunkMap().a(entityPlayer, tracker.ae, tracker.ag); // tracker.chunkX, tracker.chunkZ
    }

    @ApiStatus.Internal
    @Nullable private AbstractDisguise<?> getTrackedEntityDisguise() {
        final Optional<AbstractDisguise<?>> optDisguise = tracker instanceof EntityPlayer ?
                featherDisguise.getDisguiseAPI().getPlayerDisguise(((EntityPlayer) tracker).getBukkitEntity()) : Optional.empty();
        return optDisguise.orElse(null);
    }

}
