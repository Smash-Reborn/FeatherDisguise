package org.reborn.FeatherDisguise.metadata.modal;

import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.reborn.FeatherDisguise.metadata.EntityMetadataIndexes;
import org.reborn.FeatherDisguise.metadata.EntityType;

public class AgedEntityMetadataHolder<E extends EntityType<?>> extends LivingEntityMetadataHolder<E> {

    public AgedEntityMetadataHolder(@NotNull E entityType) {
        super(entityType);
    }

    @Override
    protected void constructDefaultMetadata() {
        super.constructDefaultMetadata(); // construct LivingEntity metadata
        this.setBaby(false);
    }

    @ApiStatus.Internal
    private byte getInternalAge() {
        return this.getIndex((byte) EntityMetadataIndexes.AGEABLE_ENTITY_AGE, (byte) 0);
    }

    public boolean isBaby() {
        return this.getInternalAge() <= -1;
    }

    public void setBaby(boolean isBaby) {
        this.setIndex((byte) EntityMetadataIndexes.AGEABLE_ENTITY_AGE, EntityDataTypes.BYTE, (byte) (isBaby ? -1 : 1));
    }
}
