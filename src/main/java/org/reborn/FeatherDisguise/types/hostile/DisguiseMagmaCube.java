package org.reborn.FeatherDisguise.types.hostile;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.reborn.FeatherDisguise.DisguiseType;
import org.reborn.FeatherDisguise.metadata.types.hostile.MagmaCubeMetadataHolder;
import org.reborn.FeatherDisguise.types.AbstractDisguise;

public class DisguiseMagmaCube extends AbstractDisguise<MagmaCubeMetadataHolder> {

    public DisguiseMagmaCube(@NotNull Player player) {
        super(DisguiseType.MAGMA_CUBE, new MagmaCubeMetadataHolder(), player);
    }

    @Override
    @NotNull public Sound getDisguiseHurtSound() {
        return Sound.MAGMACUBE_WALK2;
    }

    @Override
    @NotNull public Sound getDisguiseDeathSound() {
        return Sound.LAVA_POP;
    }

    @Override
    public float getDisguiseBaseSoundPitch() {
        return 0.75f;
    }
}
