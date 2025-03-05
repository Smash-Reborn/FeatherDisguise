package org.reborn.FeatherDisguise.types.passive;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.reborn.FeatherDisguise.DisguiseType;
import org.reborn.FeatherDisguise.metadata.types.passive.DonkeyMetadataHolder;
import org.reborn.FeatherDisguise.types.AbstractDisguise;

public class DisguiseDonkey extends AbstractDisguise<DonkeyMetadataHolder> { // expand ur dong <:

    public DisguiseDonkey(@NotNull Player player) {
        super(DisguiseType.DONKEY, new DonkeyMetadataHolder(), player);
    }

    @NotNull public String getDisguiseHurtSoundString() {
        return "mob.horse.donkey.hit";
    }

    @NotNull public String getDisguiseDeathSoundString() {
        return "mob.horse.donkey.death";
    }
}
