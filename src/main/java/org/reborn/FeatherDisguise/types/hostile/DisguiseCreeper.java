package org.reborn.FeatherDisguise.types.hostile;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.reborn.FeatherDisguise.DisguiseType;
import org.reborn.FeatherDisguise.metadata.types.hostile.CreeperMetadataHolder;
import org.reborn.FeatherDisguise.types.AbstractDisguise;

public class DisguiseCreeper extends AbstractDisguise<CreeperMetadataHolder> {

    public DisguiseCreeper(@NotNull Player player) {
        super(DisguiseType.CREEPER, new CreeperMetadataHolder(), player);
    }

    @Override
    @NotNull public Sound getDisguiseHurtSound() {
        return Sound.CREEPER_HISS;
    }

    @Override
    @NotNull public Sound getDisguiseDeathSound() {
        return Sound.CREEPER_DEATH;
    }
}
