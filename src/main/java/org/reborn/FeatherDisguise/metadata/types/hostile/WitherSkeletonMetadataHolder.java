package org.reborn.FeatherDisguise.metadata.types.hostile;

import net.minecraft.server.v1_8_R3.EntitySkeleton;
import org.reborn.FeatherDisguise.metadata.EntityType;
import org.reborn.FeatherDisguise.metadata.modal.AbstractSkeletonMetadataHolder;

public class WitherSkeletonMetadataHolder extends AbstractSkeletonMetadataHolder<EntityType<EntitySkeleton>> {

    public WitherSkeletonMetadataHolder() {
        super(EntityType.WITHER_SKELETON);
    }

    @Override
    protected void constructDefaultMetadata() {
        super.constructDefaultMetadata();
        this.setSkeletonType(SkeletonType.WITHER); // bAAcK iNn bL aAA c K
    }
}
