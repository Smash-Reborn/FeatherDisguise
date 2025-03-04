package org.reborn.FeatherDisguise.metadata.modal;

import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.reborn.FeatherDisguise.metadata.EntityMetadataIndexes;
import org.reborn.FeatherDisguise.metadata.EntityType;

public class AbstractSkeletonMetadataHolder<E extends EntityType<?>> extends LivingEntityMetadataHolder<E> {

    public AbstractSkeletonMetadataHolder(@NotNull E entityType) {
        super(entityType);
    }

    @Override
    protected void constructDefaultMetadata() {
        super.constructDefaultMetadata();
        this.setSkeletonType(SkeletonType.NORMAL);
    }

    @ApiStatus.Internal
    private byte getSkeletonByteType() {
        return this.getIndex((byte) EntityMetadataIndexes.SKELETON_TYPE, (byte) 0); // normal skeleton
    }

    @NotNull public SkeletonType getSkeletonType() {
        return this.getSkeletonByteType() != 0 ? SkeletonType.WITHER : SkeletonType.NORMAL;
    }

    public void setSkeletonType(@NotNull SkeletonType skeletonType) {
        this.setIndex((byte) EntityMetadataIndexes.SKELETON_TYPE, EntityDataTypes.BYTE, (byte) (skeletonType != SkeletonType.NORMAL ? 1 : 0));
    }

    public enum SkeletonType {
        NORMAL,
        WITHER;
    }
}
