package org.reborn.FeatherDisguise.metadata.types.hostile;

import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import net.minecraft.server.v1_8_R3.EntityWitch;
import org.jetbrains.annotations.ApiStatus;
import org.reborn.FeatherDisguise.metadata.EntityMetadataIndexes;
import org.reborn.FeatherDisguise.metadata.EntityType;
import org.reborn.FeatherDisguise.metadata.modal.LivingEntityMetadataHolder;

public class WitchMetadataHolder extends LivingEntityMetadataHolder<EntityType<EntityWitch>> {

    public WitchMetadataHolder() {
        super(EntityType.WITCH);
    }

    @Override
    protected void constructDefaultMetadata() {
        super.constructDefaultMetadata();
        this.setAggressive(this.isAggressive());
    }

    @ApiStatus.Internal
    private byte getInternalWitchAggressiveID() {
        return this.getIndex((byte) EntityMetadataIndexes.WITCH_IS_AGGRESSIVE, (byte) 0);
    }

    public boolean isAggressive() {
        return this.getInternalWitchAggressiveID() != 0;
    }

    public void setAggressive(boolean isMed) { // this doesn't actually do anything clientside lmao
        this.setIndex((byte) EntityMetadataIndexes.WITCH_IS_AGGRESSIVE, EntityDataTypes.BYTE, (byte) (isMed ? 1 : 0));
    }
}
