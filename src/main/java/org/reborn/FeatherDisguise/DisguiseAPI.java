package org.reborn.FeatherDisguise;

import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.reborn.FeatherDisguise.distributors.DisguiseListenerDistributor;
import org.reborn.FeatherDisguise.types.AbstractDisguise;
import org.reborn.FeatherDisguise.util.ITeardown;

import java.util.Optional;

@RequiredArgsConstructor @Log4j2
public class DisguiseAPI implements ITeardown {

    @Nullable private Int2ObjectOpenHashMap<AbstractDisguise<?>> activeDisguiseData;

    @Nullable private Int2IntOpenHashMap disguiseHittableData;

    private final FeatherDisguise featherDisguise;

    private DisguiseListenerDistributor disguiseListenerDistributor;

    /** @return {@link Optional} {@link AbstractDisguise} that is currently active
     * for the {@link Player}. If there is no valid disguise, then instead returns {@link Optional#empty()}.
     * **/
    @NotNull
    public Optional<AbstractDisguise<?>> getPlayerDisguise(@NotNull Player player) {
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

        // todo revert all disguses and clear any data

        if (disguiseHittableData != null && !disguiseHittableData.isEmpty()) {disguiseHittableData.clear();}

        activeDisguiseData = null;
        disguiseHittableData = null;

        if (disguiseListenerDistributor != null) {
            disguiseListenerDistributor.teardown();
        }

        disguiseListenerDistributor = null;
    }
}
