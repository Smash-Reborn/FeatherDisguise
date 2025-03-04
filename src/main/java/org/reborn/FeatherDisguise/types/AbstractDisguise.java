package org.reborn.FeatherDisguise.types;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.minecraft.server.v1_8_R3.Entity;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.reborn.FeatherDisguise.DisguiseType;
import org.reborn.FeatherDisguise.util.DisguiseHelper;
import org.reborn.FeatherDisguise.util.EntityEquipmentHandler;
import org.reborn.FeatherDisguise.wrapper.DisguiseRelatedEntityWrapper;

import java.util.HashSet;
import java.util.Optional;

@Log4j2
public class AbstractDisguise<E extends Entity> {

    @Getter @NotNull private final DisguiseType disguiseType;

    @Getter @NotNull private final Player owningBukkitPlayer;

    @Getter @NotNull private final DisguiseRelatedEntityWrapper<E> relatedEntityWrapper;

    @Getter @Setter @NotNull private String disguiseNametag;

    @Getter @Setter private boolean headRotationYawLocked = false;

    @Getter @Setter private boolean headRotationPitchLocked = false;

    private EntityEquipmentHandler equipmentHandler;

    @Getter @NotNull private final HashSet<Integer> viewingPlayerIDsMarkedAsHidden;

    public AbstractDisguise(@NotNull final DisguiseType disguiseType,
                            @NotNull final E entityObject,
                            @NotNull final Player owningPlayer) {

        this.disguiseType = disguiseType;
        this.owningBukkitPlayer = owningPlayer;
        this.relatedEntityWrapper = new DisguiseRelatedEntityWrapper<>(this, entityObject);
        this.disguiseNametag = ChatColor.YELLOW + owningPlayer.getName();
        this.viewingPlayerIDsMarkedAsHidden = new HashSet<>();
    }

    @NotNull public Sound getDisguiseHurtSound() {
        return Sound.HURT_FLESH;
    }

    @NotNull public Sound getDisguiseDeathSound() {
        return Sound.FALL_BIG;
    }

    public float getDisguiseBaseSoundVolume() {
        return 1.5f;
    }

    public float getDisguiseBaseSoundPitch() {
        return 0.8f;
    }

    @NotNull public Optional<EntityEquipmentHandler> getEquipmentHandler() {

        // only certain disguises are able to render items in slots.
        // sending equipment packets to them is useless so let's not bother if we can't
        if (!DisguiseHelper.isDisguiseAbleToRenderItemsInHandSlots(disguiseType) && !DisguiseHelper.isDisguiseAbleToRenderItemsInArmorSlots(disguiseType)) {
            log.warn("Disguise for ({}) is not able to update slots with items because this entity type doesn't support that", owningBukkitPlayer.getName());
            return Optional.empty();
        }

        // only constructs the equipment handler if we need too
        if (equipmentHandler == null) {equipmentHandler = new EntityEquipmentHandler();}
        return Optional.of(equipmentHandler);
    }
}
