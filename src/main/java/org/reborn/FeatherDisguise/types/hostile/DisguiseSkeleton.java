package org.reborn.FeatherDisguise.types.hostile;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.reborn.FeatherDisguise.enums.DisguiseType;
import org.reborn.FeatherDisguise.metadata.types.hostile.SkeletonMetadataHolder;
import org.reborn.FeatherDisguise.types.AbstractDisguise;

public class DisguiseSkeleton extends AbstractDisguise<SkeletonMetadataHolder> {

    public DisguiseSkeleton(@NotNull Player player) {
        super(DisguiseType.SKELETON, new SkeletonMetadataHolder(), player);
    }

    @Override
    @NotNull public Sound getDisguiseHurtSound() {
        return Sound.SKELETON_HURT;
    }

    @Override
    @NotNull public Sound getDisguiseDeathSound() {
        return Sound.SKELETON_DEATH;
    }
}
