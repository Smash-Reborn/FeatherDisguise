package org.reborn.FeatherDisguise.metadata.types.passive;

import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import net.minecraft.server.v1_8_R3.EntityBat;
import org.jetbrains.annotations.ApiStatus;
import org.reborn.FeatherDisguise.metadata.EntityMetadataIndexes;
import org.reborn.FeatherDisguise.metadata.EntityType;
import org.reborn.FeatherDisguise.metadata.modal.LivingEntityMetadataHolder;

public class BatMetadataHolder extends LivingEntityMetadataHolder<EntityType<EntityBat>> {

    public BatMetadataHolder() {
        super(EntityType.BAT);
    }

    @Override
    protected void constructDefaultMetadata() {
        super.constructDefaultMetadata();
        this.setHanging(false);
    }

    @ApiStatus.Internal
    private byte getInternalIsHangingID() {
        return this.getIndex((byte) EntityMetadataIndexes.BAT_IS_HANGING, (byte) 0);
    }

    public boolean isHanging() {
        return this.getInternalIsHangingID() != 0;
    }

    public void setHanging(boolean isHanging) {
        byte b = this.getInternalIsHangingID();
        b = isHanging ? (byte) (b | 1) : (byte) (b & -2);
        this.setIndex((byte) EntityMetadataIndexes.BAT_IS_HANGING, EntityDataTypes.BYTE, b);
    }
}
