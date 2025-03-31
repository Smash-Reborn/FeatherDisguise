package org.reborn.FeatherDisguise.types.passive;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.reborn.FeatherDisguise.enums.DisguiseType;
import org.reborn.FeatherDisguise.metadata.types.passive.VillagerMetadataHolder;
import org.reborn.FeatherDisguise.types.AbstractDisguise;

public class DisguiseVillager extends AbstractDisguise<VillagerMetadataHolder> {

    public DisguiseVillager(@NotNull Player player) {
        super(DisguiseType.VILLAGER, new VillagerMetadataHolder(), player);
    }

    @Override
    @NotNull public Sound getDisguiseHurtSound() {
        return Sound.VILLAGER_HIT;
    }

    @Override
    @NotNull public Sound getDisguiseDeathSound() {
        return Sound.VILLAGER_DEATH;
    }
}
