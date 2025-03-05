package org.reborn.FeatherDisguise.metadata.types.hostile;

import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import net.minecraft.server.v1_8_R3.EntitySpider;
import org.jetbrains.annotations.ApiStatus;
import org.reborn.FeatherDisguise.metadata.EntityMetadataIndexes;
import org.reborn.FeatherDisguise.metadata.EntityType;
import org.reborn.FeatherDisguise.metadata.modal.LivingEntityMetadataHolder;

public class SpiderMetadataHolder extends LivingEntityMetadataHolder<EntityType<EntitySpider>> {

    public SpiderMetadataHolder() {
        super(EntityType.SPIDER);
    }

    @Override
    protected void constructDefaultMetadata() {
        super.constructDefaultMetadata();
        this.setClimbing(this.isClimbing());
    }

    @ApiStatus.Internal
    private byte getInternalSpiderIsClimbingID() {
        return this.getIndex((byte) EntityMetadataIndexes.SPIDER_IS_CLIMBING, (byte) 0);
    }

    public boolean isClimbing() {
        return this.getInternalSpiderIsClimbingID() != 0;
    }

    public void setClimbing(boolean isClimbing) {
        byte b = this.getInternalSpiderIsClimbingID();
        b = isClimbing ? (byte) (b | 1) : (byte) (b & -2);
        this.setIndex((byte) EntityMetadataIndexes.SPIDER_IS_CLIMBING, EntityDataTypes.BYTE, b);
    }

}
