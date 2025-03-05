package org.reborn.FeatherDisguise.metadata.types.hostile;

import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import net.minecraft.server.v1_8_R3.EntityGhast;
import org.jetbrains.annotations.ApiStatus;
import org.reborn.FeatherDisguise.metadata.EntityMetadataIndexes;
import org.reborn.FeatherDisguise.metadata.EntityType;
import org.reborn.FeatherDisguise.metadata.modal.LivingEntityMetadataHolder;

public class GhastMetadataHolder extends LivingEntityMetadataHolder<EntityType<EntityGhast>> {

    public GhastMetadataHolder() {
        super(EntityType.GHAST);
    }

    @Override
    protected void constructDefaultMetadata() {
        super.constructDefaultMetadata();
        this.setAttacking(this.isAttacking());
    }

    @ApiStatus.Internal
    private byte getInternalGhastIsAttackingID() {
        return this.getIndex((byte) EntityMetadataIndexes.GHAST_IS_SCREAMING, (byte) 0);
    }

    public boolean isAttacking() {
        return this.getInternalGhastIsAttackingID() != 0;
    }

    public void setAttacking(boolean isAttacking) {
        this.setIndex((byte) EntityMetadataIndexes.GHAST_IS_SCREAMING, EntityDataTypes.BYTE, (byte) (isAttacking ? 1 : 0));
    }
}
