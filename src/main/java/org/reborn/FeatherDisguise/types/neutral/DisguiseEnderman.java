package org.reborn.FeatherDisguise.types.neutral;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.reborn.FeatherDisguise.enums.DisguiseType;
import org.reborn.FeatherDisguise.metadata.types.neutral.EndermanMetadataHolder;
import org.reborn.FeatherDisguise.types.AbstractDisguise;

public class DisguiseEnderman extends AbstractDisguise<EndermanMetadataHolder> {

    public DisguiseEnderman(@NotNull Player player) {
        super(DisguiseType.ENDERMAN, new EndermanMetadataHolder(), player);
    }

    @Override
    @NotNull public Sound getDisguiseHurtSound() {
        return Sound.ENDERMAN_HIT;
    }

    @Override
    @NotNull public Sound getDisguiseDeathSound() {
        return Sound.ENDERMAN_DEATH;
    }

    @Override
    public double getSquidRelatedEntityYOffset() {
        return 0.25d;
    }
}
