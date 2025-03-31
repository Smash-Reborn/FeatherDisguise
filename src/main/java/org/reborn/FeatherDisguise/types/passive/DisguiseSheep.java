package org.reborn.FeatherDisguise.types.passive;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.reborn.FeatherDisguise.enums.DisguiseType;
import org.reborn.FeatherDisguise.metadata.types.passive.SheepMetadataHolder;
import org.reborn.FeatherDisguise.types.AbstractDisguise;

public class DisguiseSheep extends AbstractDisguise<SheepMetadataHolder> {

    public DisguiseSheep(@NotNull Player player) {
        super(DisguiseType.SHEEP, new SheepMetadataHolder(), player);
    }

    @Override
    @NotNull public Sound getDisguiseHurtSound() {
        return Sound.SHEEP_IDLE;
    }

    @Override
    @NotNull public Sound getDisguiseDeathSound() {
        return Sound.DIG_WOOL;
    }
}
