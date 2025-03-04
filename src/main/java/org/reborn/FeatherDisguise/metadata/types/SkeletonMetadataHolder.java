package org.reborn.FeatherDisguise.metadata.types;

import net.minecraft.server.v1_8_R3.EntitySkeleton;
import org.reborn.FeatherDisguise.metadata.EntityType;
import org.reborn.FeatherDisguise.metadata.modal.AbstractSkeletonMetadataHolder;

public class SkeletonMetadataHolder extends AbstractSkeletonMetadataHolder<EntityType<EntitySkeleton>> {

    public SkeletonMetadataHolder() {
        super(EntityType.SKELETON);
    }
}
