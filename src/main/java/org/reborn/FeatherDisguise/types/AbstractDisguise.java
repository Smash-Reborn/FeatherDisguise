package org.reborn.FeatherDisguise.types;

import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.CraftSound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.reborn.FeatherDisguise.enums.DisguiseType;
import org.reborn.FeatherDisguise.enums.ViewType;
import org.reborn.FeatherDisguise.metadata.EntityDimensions;
import org.reborn.FeatherDisguise.metadata.types.AbstractMetadataHolder;
import org.reborn.FeatherDisguise.util.DisguiseUtil;
import org.reborn.FeatherDisguise.util.EntityEquipmentHandler;
import org.reborn.FeatherDisguise.wrapper.DisguiseRelatedEntityWrapper;

import java.util.List;
import java.util.Optional;
import java.util.WeakHashMap;

@Log4j2
public class AbstractDisguise<E extends AbstractMetadataHolder<?>> {

    @Getter @NotNull private final DisguiseType disguiseType;

    @Getter @NotNull private final Player owningBukkitPlayer;

    @Getter @NotNull private final DisguiseRelatedEntityWrapper<E> relatedEntitiesWrapper;

    @Getter @NotNull private String disguiseNametag;

    @Getter @Setter private boolean headRotationYawLocked = false;

    @Getter @Setter private boolean headRotationPitchLocked = false;

    private EntityEquipmentHandler equipmentHandler;

    @Getter @NotNull private final WeakHashMap<Player, ViewType> viewingPlayerMarkedType;

    @Setter @NotNull private EntityDimensions cachedEntityDimensions;

    private float precalculatedSquidEntityYPosModifier;

    public AbstractDisguise(@NotNull final DisguiseType disguiseType,
                            @NotNull final E entityObject,
                            @NotNull final Player owningPlayer) {

        this.disguiseType = disguiseType;
        this.owningBukkitPlayer = owningPlayer;
        this.disguiseNametag = ChatColor.YELLOW + owningPlayer.getName();
        this.relatedEntitiesWrapper = new DisguiseRelatedEntityWrapper<>(this, entityObject);
        this.viewingPlayerMarkedType = new WeakHashMap<>();
        this.cachedEntityDimensions = this.getRelatedEntitiesWrapper().getBaseDisguiseEntity().getMetadataHolder().getEntityType().getEntityDimensions();
        this.recalculateSquidEntityYPosModifier();
    }

    @Nullable public Sound getDisguiseHurtSound() {
        return Sound.HURT_FLESH;
    }

    @Nullable public Sound getDisguiseDeathSound() {
        return Sound.HURT_FLESH;
    }

    @Nullable public String getDisguiseHurtSoundString() {
        return this.getDisguiseHurtSound() != null ?
                CraftSound.getSound(this.getDisguiseHurtSound()) : null;
    }

    @Nullable public String getDisguiseDeathSoundString() {
        return this.getDisguiseDeathSound() != null ?
                CraftSound.getSound(this.getDisguiseDeathSound()) : null;
    }

    public float getDisguiseBaseSoundVolume() {
        return 1.5f;
    }

    public float getDisguiseBaseSoundPitch() {
        return 0.8f;
    }

    public void setDisguiseNametag(@Nullable String nametag) {
        if (nametag == null) return;
        this.disguiseNametag = nametag;
        this.getRelatedEntitiesWrapper().getNametagArmorStandEntity().getMetadataHolder().setCustomName(this.disguiseNametag);
    }

    @NotNull public Optional<EntityEquipmentHandler> getEquipmentHandler() {

        // only certain disguises are able to render items in slots.
        // sending equipment packets to them is useless so let's not bother if we can't
        if (!DisguiseUtil.isDisguiseAbleToRenderItemsInHandSlots(disguiseType) && !DisguiseUtil.isDisguiseAbleToRenderItemsInArmorSlots(disguiseType)) {
            log.warn("Disguise for ({}) is not able to update slots with items because this entity type doesn't support that", owningBukkitPlayer.getName());
            return Optional.empty();
        }

        // only constructs the equipment handler if we need too
        if (equipmentHandler == null) {equipmentHandler = new EntityEquipmentHandler();}
        return Optional.of(equipmentHandler);
    }

    public boolean doesViewingPlayerMatchMarkerType(@NotNull Player viewer, @NotNull ViewType viewType) {
        return viewingPlayerMarkedType.containsKey(viewer) && viewingPlayerMarkedType.get(viewer) == viewType;
    }

    public boolean isDisguiseAndRelatedEntitiesHiddenForViewer(@NotNull Player viewer) {
        return doesViewingPlayerMatchMarkerType(viewer, ViewType.CANNOT_SEE_DISGUISE) || doesViewingPlayerMatchMarkerType(viewer, ViewType.HARD_HIDDEN_DISGUISE);
    }

    public double getSquidRelatedEntityYOffset() {
        return 0.0d;
    }

    public double getCalculatedSquidRelatedEntityYPos(final double baseYInput) {
        return baseYInput + this.precalculatedSquidEntityYPosModifier;
    }

    @NotNull public Vector3d getCalculatedSquidRelatedEntityPos(final double baseXInput, final double baseYInput, final double baseZInput) {
        return new Vector3d(baseXInput, this.getCalculatedSquidRelatedEntityYPos(baseYInput), baseZInput);
    }

    protected void recalculateSquidEntityYPosModifier() {
        this.precalculatedSquidEntityYPosModifier = (float) ((this.cachedEntityDimensions.getHeight() * 0.65d /*0.75*/) + this.getSquidRelatedEntityYOffset());
    }

    @Nullable public List<PacketWrapper<?>> extraPacketsToProvideDuringEntitySpawning() {
        return null;
    }
}
