package org.reborn.FeatherDisguise.types.hostile;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.reborn.FeatherDisguise.DisguiseType;
import org.reborn.FeatherDisguise.metadata.modal.AbstractZombieMetadataHolder;
import org.reborn.FeatherDisguise.types.AbstractDisguise;

public class DisguiseZombie extends AbstractDisguise<AbstractZombieMetadataHolder> {

    public DisguiseZombie(@NotNull Player player) {
        super(DisguiseType.ZOMBIE, new AbstractZombieMetadataHolder(), player);
    }

    @Override
    @NotNull public Sound getDisguiseHurtSound() {
        return Sound.ZOMBIE_HURT;
    }

    @Override
    @NotNull public Sound getDisguiseDeathSound() {
        return Sound.ZOMBIE_DEATH;
    }
}
