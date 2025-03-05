package org.reborn.FeatherDisguise.metadata.types.hostile;

import net.minecraft.server.v1_8_R3.EntityGuardian;
import org.reborn.FeatherDisguise.metadata.EntityType;
import org.reborn.FeatherDisguise.metadata.modal.AbstractGuardianMetadataHolder;

public class GuardianMetadataHolder extends AbstractGuardianMetadataHolder<EntityType<EntityGuardian>> {

    public GuardianMetadataHolder() {
        super(EntityType.GUARDIAN);
    }
}
