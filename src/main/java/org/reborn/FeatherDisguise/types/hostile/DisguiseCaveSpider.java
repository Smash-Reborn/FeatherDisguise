package org.reborn.FeatherDisguise.types.hostile;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.reborn.FeatherDisguise.enums.DisguiseType;
import org.reborn.FeatherDisguise.metadata.types.hostile.CaveSpiderMetadataHolder;
import org.reborn.FeatherDisguise.types.AbstractDisguise;

public class DisguiseCaveSpider extends AbstractDisguise<CaveSpiderMetadataHolder> {

    public DisguiseCaveSpider(@NotNull Player player) {
        super(DisguiseType.CAVE_SPIDER, new CaveSpiderMetadataHolder(), player);
    }

    @Override
    @NotNull public Sound getDisguiseHurtSound() {
        return Sound.SPIDER_IDLE;
    }

    @Override
    @NotNull public Sound getDisguiseDeathSound() {
        return Sound.SPIDER_DEATH;
    }

    @Override
    public float getDisguiseBaseSoundPitch() {
        return 1.45f;
    }
}
