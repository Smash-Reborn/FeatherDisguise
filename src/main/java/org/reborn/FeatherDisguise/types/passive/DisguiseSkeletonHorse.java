package org.reborn.FeatherDisguise.types.passive;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.reborn.FeatherDisguise.enums.DisguiseType;
import org.reborn.FeatherDisguise.metadata.types.passive.SkeletonHorseMetadataHolder;
import org.reborn.FeatherDisguise.types.AbstractDisguise;

public class DisguiseSkeletonHorse extends AbstractDisguise<SkeletonHorseMetadataHolder> {

    public DisguiseSkeletonHorse(@NotNull Player player) {
        super(DisguiseType.SKELETON_HORSE, new SkeletonHorseMetadataHolder(), player);
    }

    @NotNull public String getDisguiseHurtSoundString() {
        return "mob.horse.skeleton.hit";
    }

    @NotNull public String getDisguiseDeathSoundString() {
        return "mob.horse.skeleton.death";
    }
}
