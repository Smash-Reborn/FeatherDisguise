package org.reborn.FeatherDisguise.types.passive;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.reborn.FeatherDisguise.DisguiseType;
import org.reborn.FeatherDisguise.metadata.types.passive.ZombieHorseMetadataHolder;
import org.reborn.FeatherDisguise.types.AbstractDisguise;

public class DisguiseZombieHorse extends AbstractDisguise<ZombieHorseMetadataHolder> {

    public DisguiseZombieHorse(@NotNull Player player) {
        super(DisguiseType.ZOMBIE_HORSE, new ZombieHorseMetadataHolder(), player);
    }

    @NotNull public String getDisguiseHurtSoundString() {
        return "mob.horse.zombie.hit";
    }

    @NotNull public String getDisguiseDeathSoundString() {
        return "mob.horse.zombie.death";
    }
}
