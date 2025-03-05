package org.reborn.FeatherDisguise.metadata.types.passive;

import net.minecraft.server.v1_8_R3.EntityHorse;
import org.bukkit.entity.Horse;
import org.reborn.FeatherDisguise.metadata.EntityType;
import org.reborn.FeatherDisguise.metadata.modal.AbstractHorseMetadataHolder;

public class DonkeyMetadataHolder extends AbstractHorseMetadataHolder<EntityType<EntityHorse>> {

    public DonkeyMetadataHolder() {
        super(EntityType.DONKEY);
    }

    @Override
    protected void constructDefaultMetadata() {
        super.constructDefaultMetadata();
        this.setHorseType(Horse.Variant.DONKEY);
    }
}
