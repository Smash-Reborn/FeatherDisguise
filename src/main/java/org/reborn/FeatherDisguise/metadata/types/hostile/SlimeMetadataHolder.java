package org.reborn.FeatherDisguise.metadata.types.hostile;

import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import net.minecraft.server.v1_8_R3.EntitySlime;
import org.jetbrains.annotations.ApiStatus;
import org.reborn.FeatherDisguise.metadata.EntityMetadataIndexes;
import org.reborn.FeatherDisguise.metadata.EntityType;
import org.reborn.FeatherDisguise.metadata.modal.LivingEntityMetadataHolder;

public class SlimeMetadataHolder extends LivingEntityMetadataHolder<EntityType<EntitySlime>> {

    public SlimeMetadataHolder() {
        super(EntityType.SLIME);
    }

    @Override
    protected void constructDefaultMetadata() {
        super.constructDefaultMetadata();
        this.setSize(this.getSize());
    }

    @ApiStatus.Internal
    private byte getInternalSlimeSizeID() {
        return this.getIndex((byte) EntityMetadataIndexes.SLIME_SIZE, (byte) 1);
    }

    public int getSize() {
        return this.getInternalSlimeSizeID();
    }

    /** DON'T USE THIS METHOD BECAUSE IT WILL NOT CORRECTLY ADJUST DISGUISE-RELATED ENTITY POSITIONS.
     * INSTEAD PLEASE USE THE METHODS PROVIDED WITHIN THE DISGUISE-RELATED CLASSES!!! **/
    @Deprecated
    public void setSize(int size) {
        this.setIndex((byte) EntityMetadataIndexes.SLIME_SIZE, EntityDataTypes.BYTE, (byte) size);
    }
}
