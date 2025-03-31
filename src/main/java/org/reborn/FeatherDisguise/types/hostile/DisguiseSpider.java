package org.reborn.FeatherDisguise.types.hostile;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.reborn.FeatherDisguise.enums.DisguiseType;
import org.reborn.FeatherDisguise.metadata.types.hostile.SpiderMetadataHolder;
import org.reborn.FeatherDisguise.types.AbstractDisguise;

public class DisguiseSpider extends AbstractDisguise<SpiderMetadataHolder> {

    public DisguiseSpider(@NotNull Player player) {
        super(DisguiseType.SPIDER, new SpiderMetadataHolder(), player);
    }

    @Override
    @NotNull public Sound getDisguiseHurtSound() {
        return Sound.SPIDER_IDLE;
    }

    @Override
    @NotNull public Sound getDisguiseDeathSound() {
        return Sound.SPIDER_DEATH;
    }
}
