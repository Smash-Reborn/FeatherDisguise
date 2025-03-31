package org.reborn.FeatherDisguise.types.neutral;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.reborn.FeatherDisguise.enums.DisguiseType;
import org.reborn.FeatherDisguise.metadata.types.neutral.WolfMetadataHolder;
import org.reborn.FeatherDisguise.types.AbstractDisguise;

public class DisguiseWolf extends AbstractDisguise<WolfMetadataHolder> {

    public DisguiseWolf(@NotNull Player player) {
        super(DisguiseType.WOLF, new WolfMetadataHolder(), player);
    }

    @Override
    @NotNull public Sound getDisguiseHurtSound() {
        return Sound.WOLF_HURT;
    }

    @Override
    @NotNull public Sound getDisguiseDeathSound() {
        return Sound.WOLF_DEATH;
    }
}
