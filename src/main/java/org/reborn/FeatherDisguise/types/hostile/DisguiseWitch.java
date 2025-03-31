package org.reborn.FeatherDisguise.types.hostile;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.reborn.FeatherDisguise.enums.DisguiseType;
import org.reborn.FeatherDisguise.metadata.types.hostile.WitchMetadataHolder;
import org.reborn.FeatherDisguise.types.AbstractDisguise;

public class DisguiseWitch extends AbstractDisguise<WitchMetadataHolder> {

    public DisguiseWitch(@NotNull Player player) {
        super(DisguiseType.WITCH, new WitchMetadataHolder(), player);
    }

    @Override
    @NotNull public Sound getDisguiseHurtSound() {
        return Sound.VILLAGER_HIT;
    }

    @Override
    @NotNull public Sound getDisguiseDeathSound() {
        return Sound.VILLAGER_NO;
    }

    @Override
    public float getDisguiseBaseSoundPitch() {
        return 0.6f;
    }
}
