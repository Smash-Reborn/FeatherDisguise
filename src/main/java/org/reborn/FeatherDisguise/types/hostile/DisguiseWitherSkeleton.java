package org.reborn.FeatherDisguise.types.hostile;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.reborn.FeatherDisguise.DisguiseType;
import org.reborn.FeatherDisguise.metadata.types.hostile.WitherSkeletonMetadataHolder;
import org.reborn.FeatherDisguise.types.AbstractDisguise;

public class DisguiseWitherSkeleton extends AbstractDisguise<WitherSkeletonMetadataHolder> {

    public DisguiseWitherSkeleton(@NotNull Player player) {
        super(DisguiseType.WITHER_SKELETON, new WitherSkeletonMetadataHolder(), player);
    }

    @Override
    @NotNull public Sound getDisguiseHurtSound() {
        return Sound.SKELETON_HURT;
    }

    @Override
    @NotNull public Sound getDisguiseDeathSound() {
        return Sound.SKELETON_DEATH;
    }

    @Override
    public float getDisguiseBaseSoundPitch() {
        return 0.35f;
    }
}
