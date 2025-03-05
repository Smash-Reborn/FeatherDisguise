package org.reborn.FeatherDisguise;

import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDestroyEntities;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.reborn.FeatherDisguise.distributors.DisguiseListenerDistributor;
import org.reborn.FeatherDisguise.types.AbstractDisguise;
import org.reborn.FeatherDisguise.util.DisguiseUtil;
import org.reborn.FeatherDisguise.util.ITeardown;
import org.reborn.FeatherDisguise.util.PacketUtil;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor @Log4j2
public class DisguiseAPI implements ITeardown {

    @Nullable private Int2ObjectOpenHashMap<AbstractDisguise<?>> activeDisguiseData;

    @Nullable private Int2IntOpenHashMap disguiseHittableData;

    private final FeatherDisguise featherDisguise;

    private DisguiseListenerDistributor disguiseListenerDistributor;


    /** Takes the {@link Player} in the constructor and disguises them as
     * the supplied {@link DisguiseType} entity.
     * @apiNote
     * The method will call {@link #generateDisguise(Player, DisguiseType)} initially
     * to try and generate all relevant data for both the class and the player. If this
     * method returns {@code false}, the {@code disguise} itself will not construct and
     * the method will abort.
     * **/
    public void disguisePlayerAsEntity(@NotNull Player player, @NotNull DisguiseType disguiseType) {
        final boolean successfullyGeneratedDisguiseData = this.generateDisguise(player, disguiseType);
        if (!successfullyGeneratedDisguiseData) return;
        this.showDisguiseForPlayersInWorld(player);
        this.checkAndGeneratePacketHandlers();
    }

    /** Takes the {@link Player} in the constructor and <b>silently</b> generates new
     * {@link AbstractDisguise} data for them. Any previously active disguise data
     * is removed first, then the new data put in its place.
     * @apiNote
     * The method will first attempt to generate {@link AbstractDisguise} data from the
     * {@link DisguiseType} enum, via {@link DisguiseType#generateDisguiseObjectFromType(Player)}.
     * If this method fails, the method will abort and return {@code false}. The only time this
     * method might potentially fail is when calling {@link #removeDisguise(Player, boolean)}.
     * **/
    public boolean generateDisguise(@NotNull Player player, @NotNull DisguiseType disguiseType) {
        try {

            final Optional<AbstractDisguise<?>> optDisguise = disguiseType.generateDisguiseObjectFromType(player);
            if (!optDisguise.isPresent()) {
                log.warn("Failed to generate disguise instance for ({}) (player: {})", disguiseType.name().toLowerCase(), player.getName());
                return false;
            }

            this.removeDisguise(player, false); // just in case, clear any existing disguise

            final AbstractDisguise<?> disguise = optDisguise.get();
            if (activeDisguiseData == null) {activeDisguiseData = new Int2ObjectOpenHashMap<>();}
            activeDisguiseData.put(player.getEntityId(), disguise);

            if (disguiseHittableData == null) {disguiseHittableData = new Int2IntOpenHashMap();}
            disguiseHittableData.put(disguise.getRelatedEntitiesWrapper().getHittableSquidEntity().getVirtualID(), player.getEntityId());

        } catch (Exception ex) {
            log.warn("Failed to dry-generate new disguise instance for player ({})", player.getName(), ex);
            return false;
        }

        return true;
    }

    /** Takes the {@link Player} in the constructor and attempts to remove
     * any current {@link AbstractDisguise} data that is active. This includes
     * both removal from the {@link #activeDisguiseData} & {@link #disguiseHittableData} maps.
     * @apiNote
     * If {@link #getPlayerDisguise(Player)} returns {@code false}, the method will abort
     * as it means the {@link Player} has no active disguise. If a {@code disguise} has been
     * successfully referenced however, packets will get sent to viewing players clients
     * which will remove all the {@code disguise} related entities.
     *
     * @param showPlayerAfterRemoval determines whether {@link Player} related
     * packets should be sent to clients AFTER removing the {@code disguise} entities. (By
     * setting this to false, it means the player will not get shown to viewing clients)
     * **/
    public void removeDisguise(@NotNull Player player, boolean showPlayerAfterRemoval) {
        final Optional<AbstractDisguise<?>> optPlayerDisguise = this.getPlayerDisguise(player);
        if (!optPlayerDisguise.isPresent()) {
            //log.info("Player ({}) does not have a valid disguise active. Unable to remove disguise", player.getName());
            return;
        }

        final AbstractDisguise<?> disguise = optPlayerDisguise.get();
        this.hideDisguiseForPlayersInWorld(disguise, showPlayerAfterRemoval); // this will remove the disguise entities & make players visible again

        // remove any hittable related entityID data
        if (disguiseHittableData != null) {
            disguiseHittableData.remove(disguise.getRelatedEntitiesWrapper().getHittableSquidEntity().getVirtualID());
        }

        // now finally remove them from the main disguise data (should probably assert but whatever this is safer)
        if (activeDisguiseData != null) {
            activeDisguiseData.remove(player.getEntityId());
        }

        // make sure we are correctly clearing our shit if we don't need it anymore
        this.checkAndRemoveDataAndPacketHandlers();
    }

    /** Attempts to send spawning packets for all {@code disguise} related entities
     * to viewing client(s) for the {@link Player} in the constructor. If the player
     * has no {@code disguise} active, the method will abort and not do anything else.
     * **/
    public void showDisguiseForPlayersInWorld(@NotNull Player playerWhoIsDisguised) {
        this.showDisguiseForPlayers(playerWhoIsDisguised, DisguiseUtil.getPlayersInWorldExcluding(playerWhoIsDisguised));
    }

    /** Attempts to send spawning packets for all {@code disguise} related entities
     * to the viewing player (provided in the constructor). If the {@link Player}
     * has no {@code disguise} active, the method will abort and not do anything else.
     * **/
    public void showDisguiseForPlayer(@NotNull Player playerWhoIsDisguised, @NotNull Player viewingPlayer) {
        this.showDisguiseForPlayers(playerWhoIsDisguised, Collections.singletonList(viewingPlayer));
    }

    /** Attempts to send spawning packets for all {@code disguise} related entities to the viewing
     * players list provided. If the {@link Player} has no {@code disguise} active,
     * or the viewing players list is empty, the method will abort and not do anything else.
     * **/
    public void showDisguiseForPlayers(@NotNull Player playerWhoIsDisguised, @NotNull List<Player> viewingPlayers) {
        final Optional<AbstractDisguise<?>> optPlayerDisguise = this.getPlayerDisguise(playerWhoIsDisguised);
        if (!optPlayerDisguise.isPresent()) return;
        this.showDisguiseForPlayers(optPlayerDisguise.get(), viewingPlayers);
    }

    /** Attempts to send spawning packets for all {@link AbstractDisguise} related entities to
     * viewing client(s). (this usually means all {@link Player} within the same world)
     * **/
    public void showDisguiseForPlayersInWorld(@NotNull AbstractDisguise<?> disguise) {
        this.showDisguiseForPlayers(disguise, DisguiseUtil.getPlayersInWorldExcluding(disguise.getOwningBukkitPlayer()));
    }

    /** Attempts to send spawning packets for all {@link AbstractDisguise} related entities to
     * the viewing {@link Player} provided in the constructor.
     * **/
    public void showDisguiseForPlayer(@NotNull AbstractDisguise<?> disguise, @NotNull Player viewingPlayer) {
        this.showDisguiseForPlayers(disguise, Collections.singletonList(viewingPlayer));
    }

    /** Attempts to send spawning packets for all {@link AbstractDisguise} related entities to
     * all players within the viewing list provided in the constructor.
     * **/
    public void showDisguiseForPlayers(@NotNull AbstractDisguise<?> disguise, @NotNull List<Player> viewingPlayers) {
        if (viewingPlayers.isEmpty()) return; // you will suffer the stabbing pain of my thousand sword strike

        final Optional<List<PacketWrapper<?>>> optSpawningPackets = disguise.getRelatedEntitiesWrapper().generateSpawningPacketsForAllDisguiseRelatedEntities();
        if (!optSpawningPackets.isPresent()) {
            log.warn("Unable to generate spawning packets for disguise ({}) (owningPlayer: {})",
                    disguise.getDisguiseType().name().toLowerCase(), disguise.getOwningBukkitPlayer().getName());
            return;
        }

        final List<PacketWrapper<?>> spawningPackets = optSpawningPackets.get();
        if (spawningPackets.isEmpty()) {
            log.warn("Spawning packets for disguise ({}) (owningPlayer: {}) were empty",
                    disguise.getDisguiseType().name().toLowerCase(), disguise.getOwningBukkitPlayer().getName());
            return;
        }

        final WrapperPlayServerDestroyEntities destroyPlayerPacket = disguise.getRelatedEntitiesWrapper().generateDestroyPacketForDisguisePlayerOwner();

        for (final Player viewer : viewingPlayers) {
            if (viewer.getEntityId() == disguise.getOwningBukkitPlayer().getEntityId()) continue; // cannot do any packets for the player who owns the disguise
            if (!viewer.getWorld().equals(disguise.getOwningBukkitPlayer().getWorld())) continue;

            // if they were previously marked as the disguise being hidden from them, unmark as we are re-sending spawn packets
            disguise.getViewingPlayerIDsMarkedAsHidden().remove(viewer.getEntityId());

            // [1] destroy the owning player to the viewers client
            // [2] send spawning packets for all our disguise related entities, now the viewing client can see the disguise!
            PacketUtil.sendPacketEventsPacket(viewer, destroyPlayerPacket, true);
            PacketUtil.sendPacketEventsPackets(viewer, spawningPackets, true);
        }
    }

    /** Attempts to send destroy packets for all {@code disguise} related entities
     * to viewing client(s) for the {@link Player} in the constructor. If the player
     * has no {@code disguise} active, the method will abort and not do anything else.
     * **/
    public void hideDisguiseForPlayersInWorld(@NotNull Player playerWhoIsDisguised, boolean showPlayerAfterHiding) {
        this.hideDisguiseForPlayers(playerWhoIsDisguised, DisguiseUtil.getPlayersInWorldExcluding(playerWhoIsDisguised), showPlayerAfterHiding);
    }

    /** Attempts to send destroy packets for all {@code disguise} related entities
     * to the viewing player (provided in the constructor). If the {@link Player}
     * has no {@code disguise} active, the method will abort and not do anything else.
     * **/
    public void hideDisguiseForPlayer(@NotNull Player playerWhoIsDisguised, @NotNull Player viewingPlayer, boolean showPlayerAfterHiding) {
        this.hideDisguiseForPlayers(playerWhoIsDisguised, Collections.singletonList(viewingPlayer), showPlayerAfterHiding);
    }

    /** Attempts to send destroy packets for all {@code disguise} related entities to the viewing
     * players list provided. If the {@link Player} has no {@code disguise} active,
     * or the viewing players list is empty, the method will abort and not do anything else.
     * **/
    public void hideDisguiseForPlayers(@NotNull Player playerWhoIsDisguised, @NotNull List<Player> viewingPlayers, boolean showPlayerAfterHiding) {
        final Optional<AbstractDisguise<?>> optPlayerDisguise = this.getPlayerDisguise(playerWhoIsDisguised);
        if (!optPlayerDisguise.isPresent()) return; // nooooooooPE you're a lying bitCH
        this.hideDisguiseForPlayers(optPlayerDisguise.get(), viewingPlayers, showPlayerAfterHiding);
    }

    /** Attempts to send destroy packets for all {@link AbstractDisguise} related entities to
     * viewing client(s). (this usually means all {@link Player} within the same world)
     * **/
    public void hideDisguiseForPlayersInWorld(@NotNull AbstractDisguise<?> disguise, boolean showPlayerAfterHiding) {
        this.hideDisguiseForPlayers(disguise, DisguiseUtil.getPlayersInWorldExcluding(disguise.getOwningBukkitPlayer()), showPlayerAfterHiding);
    }

    /** Attempts to send destroy packets for all {@link AbstractDisguise} related entities to
     * the viewing {@link Player} provided in the constructor.
     * **/
    public void hideDisguiseForPlayer(@NotNull AbstractDisguise<?> disguise, @NotNull Player viewingPlayer, boolean showPlayerAfterHiding) {
        this.hideDisguiseForPlayers(disguise, Collections.singletonList(viewingPlayer), showPlayerAfterHiding);
    }

    /** Attempts to send destroy packets for all {@link AbstractDisguise} related entities
     * to all players within the viewing list provided in the constructor.
     *
     * @param showPlayerAfterHiding determines whether {@link Player} related spawning
     * packets should be sent AFTER removing the disguise. If this is set to {@code false},
     * the player will not show up on viewing clients after removing the {@code disguise} entities.
     * **/
    public void hideDisguiseForPlayers(@NotNull AbstractDisguise<?> disguise, @NotNull List<Player> viewingPlayers, boolean showPlayerAfterHiding) {
        if (viewingPlayers.isEmpty()) return; // why... why would u do dis to me

        // we might not need to send spawning packets if the flag is set to false.
        // so this bit of code here just makes we are being efficient with our packets
        final List<PacketWrapper<?>> spawningPackets;
        if (showPlayerAfterHiding) {

            final Optional<List<PacketWrapper<?>>> optPlayerSpawningPackets = disguise.getRelatedEntitiesWrapper().generateSpawningPacketsForDisguisePlayerOwner();
            spawningPackets = optPlayerSpawningPackets.orElse(null);

        } else spawningPackets = null;

        // this however... this we always need to generate
        final WrapperPlayServerDestroyEntities destroyPacket = disguise.getRelatedEntitiesWrapper().generateDestroyPacketForAllDisguiseRelatedEntities();

        for (final Player viewer : viewingPlayers) {
            if (viewer.getEntityId() == disguise.getOwningBukkitPlayer().getEntityId()) continue; // cannot do any packets for the player who owns the disguise
            //if (!viewer.getWorld().equals(disguise.getOwningBukkitPlayer().getWorld())) continue; // can't do this because what if we wanna hide on world switches lol....
            if (disguise.getViewingPlayerIDsMarkedAsHidden().contains(viewer.getEntityId())) continue; // if they have been marked as hidden for the viewer already, don't bother

            disguise.getViewingPlayerIDsMarkedAsHidden().add(viewer.getEntityId()); // now marked as hidden for this viewer

            // [1] disguise & all related entities are now removed from the viewers client
            // [2] if flag is true, player is now restored back to the viewers client
            PacketUtil.sendPacketEventsPacket(viewer, destroyPacket, true);
            if (showPlayerAfterHiding && spawningPackets != null) {PacketUtil.sendPacketEventsPackets(viewer, spawningPackets, true);}
        }
    }

    /** @return {@link Optional} {@link AbstractDisguise} that is currently active
     * for the {@link Player}. If there is no valid disguise, then instead returns {@link Optional#empty()}.
     * **/
    @NotNull public Optional<AbstractDisguise<?>> getPlayerDisguise(@NotNull Player player) {
        return this.getDisguiseFromEntityID(player.getEntityId());
    }

    /** @return {@link Optional} {@link AbstractDisguise} that is currently
     * active for the provided {@code entityID}. If the {@code entityID} has no valid
     * disguise loaded, then the method will instead return {@link Optional#empty()}.
     * @apiNote
     * You should only be referencing {@link Player} related {@code entityIDs}. Doing so
     * with other IDs will probably return {@code false} because disguises are only handled by
     * players. In almost all cases you can just use {@link #getPlayerDisguise(Player)} instead.
     * **/
    @NotNull public Optional<AbstractDisguise<?>> getDisguiseFromEntityID(final int entityID) {
        if (!this.isEntityIDValidAsDisguise(entityID)) return Optional.empty();
        // assertions fine because of the check above
        assert activeDisguiseData != null;
        assert activeDisguiseData.containsKey(entityID);
        return Optional.of(activeDisguiseData.get(entityID));
    }

    /** @return {@code true} if the {@link Player} current {@link AbstractDisguise} has
     * a matching {@link DisguiseType} to the one provided within the constructor. In
     * almost all causes this will probably be referencing {@link DisguiseType}.
     * **/
    public boolean isPlayerCurrentlyDisguisedAs(@NotNull Player player, @NotNull DisguiseType disguiseType) {
        final Optional<AbstractDisguise<?>> optPlayerDisguise = this.getPlayerDisguise(player);
        return optPlayerDisguise.filter(abstractDisguise -> abstractDisguise.getDisguiseType() == disguiseType).isPresent();
    }

    /** @return {@code true} if the {@link Player} has valid {@link AbstractDisguise} data. **/
    public boolean isPlayerCurrentlyDisguised(@NotNull Player player) {
        return this.isEntityIDValidAsDisguise(player.getEntityId());
    }

    /** @return {@code true} if the {@code entityID} has valid {@link AbstractDisguise} data.
     * @apiNote
     * You should only be referencing {@link Player} related {@code entityIDs}. Doing so with
     * other IDs will probably return {@code false} because disguises are only handled by players.
     * In almost all cases you can just use {@link #isPlayerCurrentlyDisguised(Player)} instead.
     * **/
    public boolean isEntityIDValidAsDisguise(final int entityID) {
        return activeDisguiseData != null && activeDisguiseData.containsKey(entityID);
    }

    /** @return {@link Optional} {@link AbstractDisguise} that is currently
     * active for the provided {@code entityID}. If the {@code entityID} has no valid
     * disguise loaded, then the method will instead return {@link Optional#empty()}.
     * @apiNote
     * You should only be referencing {@link org.reborn.FeatherDisguise.wrapper.DisguiseRelatedEntityWrapper}
     * {@code hittable} entities. Doing so with other IDs will probably return {@link Optional#empty()} because
     * the method is only supposed to be checking {@link #disguiseHittableData} related entityIDs.
     * <p></p>
     * If you are looking for a method to directly return {@link AbstractDisguise} data for a {@link Player}
     * you should probably just use {@link #getPlayerDisguise(Player)} instead.
     * **/
    @NotNull public Optional<AbstractDisguise<?>> getDisguiseFromRelatedHittableEntityID(final int entityID) {
        if (!this.doesHittableEntityHaveValidDisguise(entityID)) {
            return Optional.empty();
        }

        // assertions fine because of the check above
        assert disguiseHittableData != null;
        assert disguiseHittableData.containsKey(entityID);
        return this.getDisguiseFromEntityID(disguiseHittableData.get(entityID));
    }

    /** @return {@code true} if the {@code entityID} has valid {@link #disguiseHittableData}
     * (which means it is a disguise 'related' hittable entity (either the aabb or the squid))
     * and the {@code entityID} also has valid {@link AbstractDisguise} data.
     * @apiNote
     * You should only be referencing {@link org.reborn.FeatherDisguise.wrapper.DisguiseRelatedEntityWrapper}
     * {@code hittable} entities. Doing so with other IDs will probably return {@code false} because disguises
     * are only supposed to be handled/linked by players.
     * <p></p>
     * You can realistically only use this method to determine: 'is x entity related to a particular disguise'.
     * **/
    public boolean doesHittableEntityHaveValidDisguise(final int entityID) {
        return disguiseHittableData != null && disguiseHittableData.containsKey(entityID) && this.isEntityIDValidAsDisguise(disguiseHittableData.get(entityID));
    }

    @ApiStatus.Internal
    private void checkAndGeneratePacketHandlers() {
        if (disguiseListenerDistributor == null && activeDisguiseData != null && !activeDisguiseData.isEmpty()) {
            disguiseListenerDistributor = new DisguiseListenerDistributor(featherDisguise);
        }
    }

    @ApiStatus.Internal
    private void checkAndRemoveDataAndPacketHandlers() {

        // if our data is empty, just null them out because we don't need them
        if (activeDisguiseData != null && activeDisguiseData.isEmpty()) {activeDisguiseData = null;}
        if (disguiseHittableData != null && disguiseHittableData.isEmpty()) {disguiseHittableData = null;}

        // we have no disguises we are handling, so teardown the handler we don't need it atm
        if (disguiseListenerDistributor != null && activeDisguiseData == null) {
            disguiseListenerDistributor.teardown();
        }

        disguiseListenerDistributor = null;
    }

    @Override
    public void teardown() {

        // revert all disguises and clear any data
        if (activeDisguiseData != null && !activeDisguiseData.isEmpty()) {
            final ObjectIterator<Int2ObjectMap.Entry<AbstractDisguise<?>>> disguiseIterator = activeDisguiseData.int2ObjectEntrySet().fastIterator();
            while (disguiseIterator.hasNext()) {
                final AbstractDisguise<?> disguise = disguiseIterator.next().getValue();
                this.hideDisguiseForPlayersInWorld(disguise, true); // this will remove the disguise entities & make players visible again
                disguiseIterator.remove();
            }
        }

        if (disguiseHittableData != null && !disguiseHittableData.isEmpty()) {disguiseHittableData.clear();}

        activeDisguiseData = null;
        disguiseHittableData = null;

        if (disguiseListenerDistributor != null) {
            disguiseListenerDistributor.teardown();
        }

        disguiseListenerDistributor = null;
    }
}
