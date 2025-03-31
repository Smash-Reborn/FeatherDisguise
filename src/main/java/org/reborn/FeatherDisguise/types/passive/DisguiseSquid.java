package org.reborn.FeatherDisguise.types.passive;

import net.minecraft.server.v1_8_R3.EntitySquid;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.reborn.FeatherDisguise.enums.DisguiseType;
import org.reborn.FeatherDisguise.metadata.EntityType;
import org.reborn.FeatherDisguise.metadata.modal.LivingEntityMetadataHolder;
import org.reborn.FeatherDisguise.types.AbstractDisguise;

public class DisguiseSquid extends AbstractDisguise<LivingEntityMetadataHolder<EntityType<EntitySquid>>> {

    public DisguiseSquid(@NotNull Player player) {
        super(DisguiseType.SQUID, new LivingEntityMetadataHolder<>(EntityType.SQUID), player);
    }

    @NotNull public String getDisguiseHurtSoundString() {
        return "mob.guardian.flop";
    }

    @Override
    @NotNull public Sound getDisguiseDeathSound() {
        return Sound.AMBIENCE_THUNDER;
    }

    public float getDisguiseBaseSoundPitch() {
        return 1.54f;
    }
}
