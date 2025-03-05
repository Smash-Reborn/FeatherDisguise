package org.reborn.FeatherDisguise.types.passive;

import net.minecraft.server.v1_8_R3.EntityChicken;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.reborn.FeatherDisguise.DisguiseType;
import org.reborn.FeatherDisguise.metadata.EntityType;
import org.reborn.FeatherDisguise.metadata.modal.AgedEntityMetadataHolder;
import org.reborn.FeatherDisguise.types.AbstractDisguise;

public class DisguiseChicken extends AbstractDisguise<AgedEntityMetadataHolder<EntityType<EntityChicken>>> {

    public DisguiseChicken(@NotNull Player player) {
        super(DisguiseType.CHICKEN, new AgedEntityMetadataHolder<>(EntityType.CHICKEN), player);
    }

    @Override
    @NotNull public Sound getDisguiseHurtSound() {
        return Sound.CHICKEN_HURT;
    }

    @Override
    @NotNull public Sound getDisguiseDeathSound() {
        return Sound.CHICKEN_EGG_POP;
    }
}
