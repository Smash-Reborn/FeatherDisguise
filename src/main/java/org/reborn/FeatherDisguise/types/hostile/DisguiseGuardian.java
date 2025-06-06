package org.reborn.FeatherDisguise.types.hostile;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.reborn.FeatherDisguise.enums.DisguiseType;
import org.reborn.FeatherDisguise.metadata.types.hostile.GuardianMetadataHolder;
import org.reborn.FeatherDisguise.types.AbstractDisguise;

public class DisguiseGuardian extends AbstractDisguise<GuardianMetadataHolder> {

    public DisguiseGuardian(@NotNull Player player) {
        super(DisguiseType.GUARDIAN, new GuardianMetadataHolder(), player);
        this.setHeadRotationPitchLocked(true); // SO IT DOESN'T IEIOJIHOINHNHOIEN ITS HEAD AROUND YO
    }

    @NotNull public String getDisguiseHurtSoundString() {
        return "mob.guardian.hit";
    }

    @NotNull public String getDisguiseDeathSoundString() {
        return "mob.guardian.death";
    }
}
