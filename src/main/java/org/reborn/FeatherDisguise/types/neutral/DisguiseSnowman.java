package org.reborn.FeatherDisguise.types.neutral;

import net.minecraft.server.v1_8_R3.EntitySnowman;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.reborn.FeatherDisguise.DisguiseType;
import org.reborn.FeatherDisguise.metadata.EntityType;
import org.reborn.FeatherDisguise.metadata.modal.LivingEntityMetadataHolder;
import org.reborn.FeatherDisguise.types.AbstractDisguise;

public class DisguiseSnowman extends AbstractDisguise<LivingEntityMetadataHolder<EntityType<EntitySnowman>>> {

    public DisguiseSnowman(@NotNull Player player) {
        super(DisguiseType.SNOW_GOLEM, new LivingEntityMetadataHolder<>(EntityType.SNOWMAN), player);
    }

    @Override
    @NotNull public Sound getDisguiseHurtSound() {
        return Sound.STEP_SNOW;
    }

    @Override
    @NotNull public Sound getDisguiseDeathSound() {
        return Sound.DIG_SNOW;
    }
}
