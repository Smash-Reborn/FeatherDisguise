package org.reborn.FeatherDisguise.types.hostile;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.reborn.FeatherDisguise.DisguiseType;
import org.reborn.FeatherDisguise.metadata.types.hostile.BlazeMetadataHolder;
import org.reborn.FeatherDisguise.types.AbstractDisguise;

public class DisguiseBlaze extends AbstractDisguise<BlazeMetadataHolder> {

    public DisguiseBlaze(@NotNull Player player) {
        super(DisguiseType.BLAZE, new BlazeMetadataHolder(), player);
    }

    @Override
    @NotNull public Sound getDisguiseHurtSound() {
        return Sound.BLAZE_HIT;
    }

    @Override
    @NotNull public Sound getDisguiseDeathSound() {
        return Sound.BLAZE_DEATH;
    }
}
