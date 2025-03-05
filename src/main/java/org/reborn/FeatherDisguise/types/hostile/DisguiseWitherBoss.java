package org.reborn.FeatherDisguise.types.hostile;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.reborn.FeatherDisguise.DisguiseType;
import org.reborn.FeatherDisguise.metadata.types.hostile.WitherBossMetadataHolder;
import org.reborn.FeatherDisguise.types.AbstractDisguise;

public class DisguiseWitherBoss extends AbstractDisguise<WitherBossMetadataHolder> {

    public DisguiseWitherBoss(@NotNull Player player) {
        super(DisguiseType.WITHER_BOSS, new WitherBossMetadataHolder(), player);
    }

    @Override
    @NotNull public Sound getDisguiseHurtSound() {
        return Sound.WITHER_HURT;
    }

    @Override
    @NotNull public Sound getDisguiseDeathSound() {
        return Sound.WITHER_DEATH;
    }
}
