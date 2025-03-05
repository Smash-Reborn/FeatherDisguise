package org.reborn.FeatherDisguise.metadata.types.neutral;

import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import net.minecraft.server.v1_8_R3.EntityIronGolem;
import org.jetbrains.annotations.ApiStatus;
import org.reborn.FeatherDisguise.metadata.EntityMetadataIndexes;
import org.reborn.FeatherDisguise.metadata.EntityType;
import org.reborn.FeatherDisguise.metadata.modal.LivingEntityMetadataHolder;

public class IronGolemMetadataHolder extends LivingEntityMetadataHolder<EntityType<EntityIronGolem>> {

    public IronGolemMetadataHolder() {
        super(EntityType.IRON_GOLEM);
    }

    @Override
    protected void constructDefaultMetadata() {
        super.constructDefaultMetadata();
        this.setPlayerCreated(this.isPlayerCreated());
    }

    @ApiStatus.Internal
    private byte getInternalGholemPlayerCreatedID() {
        return this.getIndex((byte) EntityMetadataIndexes.IRON_GOLEM_IS_PLAYER_CREATED, (byte) 0);
    }

    public boolean isPlayerCreated() {
        return this.getInternalGholemPlayerCreatedID() != 0;
    }

    public void setPlayerCreated(boolean created) {     // this also does nothing clientside (well notable for the game anyways)
        byte b = this.getInternalGholemPlayerCreatedID();
        b = created ? (byte) (b | 1) : (byte) (b & -2);
        this.setIndex((byte) EntityMetadataIndexes.IRON_GOLEM_IS_PLAYER_CREATED, EntityDataTypes.BYTE, b);
    }
}
