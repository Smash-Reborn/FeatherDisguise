package org.reborn.FeatherDisguise.metadata.types.hostile;

import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import net.minecraft.server.v1_8_R3.EntityBlaze;
import org.jetbrains.annotations.ApiStatus;
import org.reborn.FeatherDisguise.metadata.EntityMetadataIndexes;
import org.reborn.FeatherDisguise.metadata.EntityType;
import org.reborn.FeatherDisguise.metadata.modal.LivingEntityMetadataHolder;

public class BlazeMetadataHolder extends LivingEntityMetadataHolder<EntityType<EntityBlaze>> {

    public BlazeMetadataHolder() {
        super(EntityType.BLAZE);
    }

    @Override
    protected void constructDefaultMetadata() {
        super.constructDefaultMetadata();
        this.setBlazing(this.isBlazing());
    }

    @ApiStatus.Internal
    private byte getInternalIsBlazingID() {
        return this.getIndex((byte) EntityMetadataIndexes.BLAZE_IS_ON_FIRE, (byte) 0);
    }

    public boolean isBlazing() {
        return this.getInternalIsBlazingID() != 0;
    }

    public void setBlazing(boolean isBlazing) { // determines whether the blaze will render using visual fire (idk why they even have this lmao)
        byte b = this.getInternalIsBlazingID();
        b = isBlazing ? (byte) (b | 1) : (byte) (b & -2);
        this.setIndex((byte) EntityMetadataIndexes.BLAZE_IS_ON_FIRE, EntityDataTypes.BYTE, b);
    }
}
