package org.reborn.FeatherDisguise.metadata.types.passive;

import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import net.minecraft.server.v1_8_R3.EntityPig;
import org.jetbrains.annotations.ApiStatus;
import org.reborn.FeatherDisguise.metadata.EntityMetadataIndexes;
import org.reborn.FeatherDisguise.metadata.EntityType;
import org.reborn.FeatherDisguise.metadata.modal.AgedEntityMetadataHolder;

public class PigMetadataHolder extends AgedEntityMetadataHolder<EntityType<EntityPig>> {

    public PigMetadataHolder() {
        super(EntityType.PIG);
    }

    @Override
    protected void constructDefaultMetadata() {
        super.constructDefaultMetadata();
        this.setSaddled(this.isSaddled());
    }

    @ApiStatus.Internal
    private byte getInternalIsSaddledID() {
        return this.getIndex((byte) EntityMetadataIndexes.PIG_HAS_SADDLE, (byte) 0);
    }

    public boolean isSaddled() {
        return this.getInternalIsSaddledID() != 0;
    }

    public void setSaddled(boolean setSaddle) {
        this.setIndex((byte) EntityMetadataIndexes.PIG_HAS_SADDLE, EntityDataTypes.BYTE, (byte) (setSaddle ? 1 : 0));
    }
}
