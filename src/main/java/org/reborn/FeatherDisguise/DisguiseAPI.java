package org.reborn.FeatherDisguise;

import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDestroyEntities;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import lombok.extern.log4j.Log4j2;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.reborn.FeatherDisguise.distributors.DisguiseListenerDistributor;
import org.reborn.FeatherDisguise.enums.DisguiseType;
import org.reborn.FeatherDisguise.enums.PacketHandlingType;
import org.reborn.FeatherDisguise.enums.ViewType;
import org.reborn.FeatherDisguise.tracker.DisguiseTrackerListener;
import org.reborn.FeatherDisguise.types.AbstractDisguise;
import org.reborn.FeatherDisguise.util.DisguiseUtil;
import org.reborn.FeatherDisguise.util.ITeardown;
import org.reborn.FeatherDisguise.util.PacketUtil;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Log4j2
public class DisguiseAPI implements ITeardown {

    @Nullable private Int2ObjectOpenHashMap<AbstractDisguise<?>> activeDisguiseData;

    @Nullable private Int2IntOpenHashMap disguiseHittableData;

    @ApiStatus.Internal
    @NotNull private final FeatherDisguise featherDisguise;

    @NotNull private final PacketHandlingType packetHandlingType;

    private DisguiseListenerDistributor disguiseListenerDistributor;

    private DisguiseTrackerListener disguiseTrackerListener;

    public DisguiseAPI(@NotNull final FeatherDisguise featherDisguise, @NotNull final PacketHandlingType packetHandlingType) {
        this.featherDisguise = featherDisguise;
        this.packetHandlingType = packetHandlingType;

        if (packetHandlingType == PacketHandlingType.CHAD_FEATHER_TRACKER) {
            this.disguiseTrackerListener = new DisguiseTrackerListener(featherDisguise);
        }
    }


    /** Takes the {@link Player} in the constructor and disguises them as
     * the supplied {@link DisguiseType} entity.
     * @apiNote
     * The method will call {@link #generateDisguise(Player, DisguiseType, String)} initially
     * to try and generate all relevant data for both the class and the player. If this
     * method returns {@code false}, the {@code disguise} itself will not construct and
     * the method will abort.
     * **/
    public void disguisePlayerAsEntity(@NotNull Player player, @NotNull DisguiseType disguiseType, @Nullable String nametagText) {
        final boolean successfullyGeneratedDisguiseData = this.generateDisguise(player, disguiseType, nametagText);
        if (!successfullyGeneratedDisguiseData) return;

        // our custom tracker needs to take priority over raw calling the showDisguise() method
        // (tracker also calls this but passes through itself first in order to synchronise the data there)
        if (packetHandlingType == PacketHandlingType.CHAD_FEATHER_TRACKER) {
            DisguiseUtil.handleEntityTrackerUpdateOrSpawnForAllMethodCallForDisguisedPlayer(player, false);
        }

        // packet handling type --> interceptors, call our regular method
        else this.showDisguiseForPlayersInWorld(player);

        // [!] important we do this last, ensure our handlers & data are active
        this.checkAndGeneratePacketHandlers();
    }

    public void disguisePlayerAsEntity(@NotNull Player player, @NotNull DisguiseType disguiseType) {
        this.disguisePlayerAsEntity(player, disguiseType, null);
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
    public boolean generateDisguise(@NotNull Player player, @NotNull DisguiseType disguiseType, @Nullable String nametagText) {
        try {

            final Optional<AbstractDisguise<?>> optDisguise = disguiseType.generateDisguiseObjectFromType(player);
            if (!optDisguise.isPresent()) {
                log.warn("Failed to generate disguise instance for ({}) (player: {})", disguiseType.name().toLowerCase(), player.getName());
                return false;
            }

            this.removeDisguise(player, false); // just in case, clear any existing disguise

            final AbstractDisguise<?> disguise = optDisguise.get();
            if (nametagText != null) {disguise.setDisguiseNametag(nametagText);}
            if (activeDisguiseData == null) {activeDisguiseData = new Int2ObjectOpenHashMap<>();}
            activeDisguiseData.put(player.getEntityId(), disguise);

            if (disguiseHittableData == null) {disguiseHittableData = new Int2IntOpenHashMap();}
            disguiseHittableData.put(disguise.getRelatedEntitiesWrapper().getHittableSquidEntity().getVirtualID(), player.getEntityId());
            disguiseHittableData.put(disguise.getRelatedEntitiesWrapper().getBaseDisguiseEntity().getVirtualID(), player.getEntityId());

        } catch (Exception ex) {
            log.warn("Failed to dry-generate new disguise instance for player ({})", player.getName(), ex);
            return false;
        }

        return true;
    }

    public boolean generateDisguise(@NotNull Player player, @NotNull DisguiseType disguiseType) {
        return this.generateDisguise(player, disguiseType, null);
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
            log.info("Player ({}) does not have a valid disguise active. Early exiting method call", player.getName());
            return;
        }

        final AbstractDisguise<?> disguise = optPlayerDisguise.get();

        // we can just call the trackers a() (destroyForAll()) method because our custom tracker is handling packet work for us
        if (packetHandlingType == PacketHandlingType.CHAD_FEATHER_TRACKER) {
            DisguiseUtil.handleEntityTrackerUpdateOrSpawnForAllMethodCallForDisguisedPlayer(player, showPlayerAfterRemoval);
            // setting the flag to FALSE prevents the tracker from attempting to respawn back in the player entity
        }

        // packet handling type --> interceptors, call our regular method
        else this.hideDisguiseForPlayersInWorld(disguise, showPlayerAfterRemoval); // this will remove the disguise entities & make players visible again

        // remove any hittable related entityID data
        if (disguiseHittableData != null) {
            disguiseHittableData.remove(disguise.getRelatedEntitiesWrapper().getHittableSquidEntity().getVirtualID());
            disguiseHittableData.remove(disguise.getRelatedEntitiesWrapper().getBaseDisguiseEntity().getVirtualID());
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

        // optimisation: if all viewers are already flagged as being able to "see" the disguise, don't bother creating packets
        //               (you don't have to worry about hiding the owning playerEntity, because they technically should already be removed for the viewer)
        boolean shouldContinue = false;
        for (final Player viewer : viewingPlayers) {
            if (!disguise.doesViewingPlayerMatchMarkerType(viewer, ViewType.CAN_SEE_DISGUISE)) {
                shouldContinue = true;
                break;
            }
        }

        if (!shouldContinue) return;


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
            //if (!viewer.getWorld().equals(disguise.getOwningBukkitPlayer().getWorld())) continue; // this is potentially breaking shit, remove for now

            // [1] destroy the owning player to the viewers client
            //     (we only need to send destroy packets to the view if they are flagged as previously not seeing the disguise.
            //      if the disguise was 'hard hidden' we don't have to worry because their client already doesn't have the player visible)
            if (disguise.doesViewingPlayerMatchMarkerType(viewer, ViewType.CANNOT_SEE_DISGUISE)) {
                PacketUtil.sendPacketEventsPacket(viewer, destroyPlayerPacket, true);
                log.info("===== player entity destroyed");
            }

            // [2] send spawning packets for all our disguise related entities, now the viewing client can see the disguise!
            if (!disguise.doesViewingPlayerMatchMarkerType(viewer, ViewType.CAN_SEE_DISGUISE)) {
                PacketUtil.sendPacketEventsPackets(viewer, spawningPackets, true);
                log.info("===== disguise related entities spawned");
            }

            // [3] flag the viewer as now being able to "see" the disguise (so distributors can handle packet work)
            disguise.getViewingPlayerMarkedType().put(viewer, ViewType.CAN_SEE_DISGUISE);
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

        log.info("directly called hidedisguise() method");

        // optimisation: if we AREN'T showing the owning player entity after removing the disguise entities &
        //               ALL viewers already have the disguise marked as 'hidden', don't bother creating packets.
        boolean shouldContinue = false;
        if (!showPlayerAfterHiding) {
            for (final Player viewer : viewingPlayers) {
                if (!disguise.doesViewingPlayerMatchMarkerType(viewer, ViewType.HARD_HIDDEN_DISGUISE)) {
                    shouldContinue = true;
                    break;
                }
            }

            if (!shouldContinue) return;
        }

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

            final boolean isAlreadyHidden = disguise.isDisguiseAndRelatedEntitiesHiddenForViewer(viewer);

            // [1] disguise & all related entities are now removed from the viewers client
            if (!isAlreadyHidden) {
                PacketUtil.sendPacketEventsPacket(viewer, destroyPacket, true);
                log.info("===== disguise related entities destroyed");
            }

            // [2] if flag is true, player is now restored back to the viewers client
            //      we are trying to send them owning player entity spawn packets HOWEVER, the viewer has
            //      already been flagged as being able to "see" the owning player entity. there's no point
            //      re-sending them player packets again, so if we can, just skip this section.
            if (showPlayerAfterHiding && spawningPackets != null && disguise.doesViewingPlayerMatchMarkerType(viewer, ViewType.CANNOT_SEE_DISGUISE)) {
                PacketUtil.sendPacketEventsPackets(viewer, spawningPackets, true);
                log.info("===== player entity spawned back in");
            }

            // [3] update the marker to correctly reflect the viewers situation with the disguise (ALWAYS DO THIS LAST, AFTER SENDING PACKETS)
            //      if they weren't already hidden. it will only send packets if they aren't marked as already hidden.
            //      if showPlayerAfterHiding = true, we mark them as just only hiding the disguise, but they can still see the owning player.
            //      else we can assume we are attempting completely hide everything from the viewer, so mark as hard hidden.
            if (!isAlreadyHidden) {
                disguise.getViewingPlayerMarkedType().put(viewer, showPlayerAfterHiding ? ViewType.CANNOT_SEE_DISGUISE : ViewType.HARD_HIDDEN_DISGUISE);
            }
        }
    }

    public void refreshDisguiseForPlayer(@NotNull Player observer, @NotNull Player disguisedPlayer) {
        final Optional<AbstractDisguise<?>> optDisguise = this.getPlayerDisguise(disguisedPlayer);
        if (!optDisguise.isPresent()) return; // doesn't have a disguise, exit early

        // remove any disguise entities -> re-send packets for spawning
        final AbstractDisguise<?> disguise = optDisguise.get();
        // todo tracker implementation updatePlayer() here
        this.hideDisguiseForPlayer(disguise, observer, false);
        this.showDisguiseForPlayer(disguise, observer);
    }

    public void refreshAllDisguisesForPlayer(@NotNull Player observer) {
        if (this.activeDisguiseData == null || this.activeDisguiseData.isEmpty()) return;

        // loop over all active disguises:
        // remove any disguise entities -> re-send packets for spawning
        for (final AbstractDisguise<?> disguise : this.activeDisguiseData.values()) {
            this.hideDisguiseForPlayer(disguise, observer, false);
            this.showDisguiseForPlayer(disguise, observer);
        }
    }

    @SuppressWarnings("unchecked")
    @NotNull public <T extends AbstractDisguise<?>> Optional<T> getPlayerDisguise(@NotNull Player player, @NotNull final Class<T> disClass) {
        final Optional<AbstractDisguise<?>> optDisguise = this.getPlayerDisguise(player);
        if (!optDisguise.isPresent() || !disClass.equals(optDisguise.get().getClass())) return Optional.empty();
        return Optional.of((T) optDisguise.get()); // stop being a bitch, we already checked this above
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

        if (disguiseTrackerListener != null) {
            disguiseTrackerListener.teardown();
        }

        disguiseListenerDistributor = null;
        disguiseTrackerListener = null;
    }
}
