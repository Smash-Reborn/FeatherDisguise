package org.reborn.FeatherDisguise.types.hostile;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.reborn.FeatherDisguise.enums.DisguiseType;
import org.reborn.FeatherDisguise.metadata.types.hostile.ElderGuardianMetadataHolder;
import org.reborn.FeatherDisguise.types.AbstractDisguise;

public class DisguiseElderGuardian extends AbstractDisguise<ElderGuardianMetadataHolder> {

    public DisguiseElderGuardian(@NotNull Player player) {
        super(DisguiseType.ELDER_GUARDIAN, new ElderGuardianMetadataHolder(), player);
    }

    @NotNull public String getDisguiseHurtSoundString() {
        return "mob.guardian.elder.hit";
    }

    @NotNull public String getDisguiseDeathSoundString() {
        return "mob.guardian.elder.death";
    }
}
