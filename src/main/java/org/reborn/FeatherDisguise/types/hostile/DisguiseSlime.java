package org.reborn.FeatherDisguise.types.hostile;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.reborn.FeatherDisguise.DisguiseType;
import org.reborn.FeatherDisguise.metadata.types.hostile.SlimeMetadataHolder;
import org.reborn.FeatherDisguise.types.AbstractDisguise;

public class DisguiseSlime extends AbstractDisguise<SlimeMetadataHolder> {

    public DisguiseSlime(@NotNull Player player) {
        super(DisguiseType.SLIME, new SlimeMetadataHolder(), player);
    }

    @Override
    @NotNull public Sound getDisguiseHurtSound() {
        return Sound.SLIME_ATTACK;
    }

    @Override
    @NotNull public Sound getDisguiseDeathSound() {
        return Sound.SLIME_WALK2;
    }
}
