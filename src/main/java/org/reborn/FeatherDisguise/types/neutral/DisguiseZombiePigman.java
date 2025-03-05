package org.reborn.FeatherDisguise.types.neutral;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.reborn.FeatherDisguise.DisguiseType;
import org.reborn.FeatherDisguise.metadata.types.neutral.ZombiePigmanMetadataHolder;
import org.reborn.FeatherDisguise.types.AbstractDisguise;

public class DisguiseZombiePigman extends AbstractDisguise<ZombiePigmanMetadataHolder> {

    public DisguiseZombiePigman(@NotNull Player player) {
        super(DisguiseType.ZOMBIE_PIGMAN, new ZombiePigmanMetadataHolder(), player);
    }

    @Override
    @NotNull public Sound getDisguiseHurtSound() {
        return Sound.ZOMBIE_PIG_HURT;
    }

    @Override
    @NotNull public Sound getDisguiseDeathSound() {
        return Sound.ZOMBIE_PIG_DEATH;
    }
}
