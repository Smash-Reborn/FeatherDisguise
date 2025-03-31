package org.reborn.FeatherDisguise.types.passive;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.reborn.FeatherDisguise.enums.DisguiseType;
import org.reborn.FeatherDisguise.metadata.types.passive.PigMetadataHolder;
import org.reborn.FeatherDisguise.types.AbstractDisguise;

public class DisguisePig extends AbstractDisguise<PigMetadataHolder> {

    public DisguisePig(@NotNull Player player) {
        super(DisguiseType.PIG, new PigMetadataHolder(), player);
    }

    @Override
    @NotNull public Sound getDisguiseHurtSound() {
        return Sound.PIG_IDLE;
    }

    @Override
    @NotNull public Sound getDisguiseDeathSound() {
        return Sound.PIG_DEATH;
    }
}
