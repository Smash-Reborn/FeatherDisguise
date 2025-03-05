package org.reborn.FeatherDisguise.types.passive;

import net.minecraft.server.v1_8_R3.EntityCow;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.reborn.FeatherDisguise.DisguiseType;
import org.reborn.FeatherDisguise.metadata.EntityType;
import org.reborn.FeatherDisguise.metadata.modal.AgedEntityMetadataHolder;
import org.reborn.FeatherDisguise.types.AbstractDisguise;

public class DisguiseCow extends AbstractDisguise<AgedEntityMetadataHolder<EntityType<EntityCow>>> {

    public DisguiseCow(@NotNull Player player) {
        super(DisguiseType.COW, new AgedEntityMetadataHolder<>(EntityType.COW), player);
    }

    @Override
    @NotNull public Sound getDisguiseHurtSound() {
        return Sound.COW_HURT;
    }

    @Override
    @NotNull public Sound getDisguiseDeathSound() {
        return Sound.COW_IDLE;
    }
}
