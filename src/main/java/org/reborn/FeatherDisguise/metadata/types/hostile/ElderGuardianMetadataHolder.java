package org.reborn.FeatherDisguise.metadata.types.hostile;

import net.minecraft.server.v1_8_R3.EntityGuardian;
import org.reborn.FeatherDisguise.metadata.EntityType;
import org.reborn.FeatherDisguise.metadata.modal.AbstractGuardianMetadataHolder;

public class ElderGuardianMetadataHolder extends AbstractGuardianMetadataHolder<EntityType<EntityGuardian>> {

    public ElderGuardianMetadataHolder() {
        super(EntityType.ELDER_GUARDIAN);
    }

    @Override
    protected void constructDefaultMetadata() {
        super.constructDefaultMetadata();
        this.setElder(true); // aYo he an old ass man noW
    }
}
