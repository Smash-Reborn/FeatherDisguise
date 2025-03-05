package org.reborn.FeatherDisguise.types.passive;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.reborn.FeatherDisguise.DisguiseType;
import org.reborn.FeatherDisguise.metadata.types.passive.BatMetadataHolder;
import org.reborn.FeatherDisguise.types.AbstractDisguise;

public class DisguiseBat extends AbstractDisguise<BatMetadataHolder> {

    public DisguiseBat(@NotNull Player player) {
        super(DisguiseType.BAT, new BatMetadataHolder(), player);
    }

    @Override
    @NotNull public Sound getDisguiseHurtSound() {
        return Sound.BAT_HURT;
    }

    @Override
    @NotNull public Sound getDisguiseDeathSound() {
        return Sound.BAT_DEATH;
    }
}
