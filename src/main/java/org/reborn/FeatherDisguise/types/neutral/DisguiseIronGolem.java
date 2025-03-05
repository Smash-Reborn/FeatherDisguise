package org.reborn.FeatherDisguise.types.neutral;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.reborn.FeatherDisguise.DisguiseType;
import org.reborn.FeatherDisguise.metadata.types.neutral.IronGolemMetadataHolder;
import org.reborn.FeatherDisguise.types.AbstractDisguise;

public class DisguiseIronGolem extends AbstractDisguise<IronGolemMetadataHolder> {

    public DisguiseIronGolem(@NotNull Player player) {
        super(DisguiseType.IRON_GOLEM, new IronGolemMetadataHolder(), player);
    }

    @Override
    @NotNull public Sound getDisguiseHurtSound() {
        return Sound.IRONGOLEM_HIT;
    }

    @Override
    @NotNull public Sound getDisguiseDeathSound() {
        return Sound.IRONGOLEM_DEATH;
    }
}
