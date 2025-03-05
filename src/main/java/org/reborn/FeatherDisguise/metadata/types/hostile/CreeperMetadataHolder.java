package org.reborn.FeatherDisguise.metadata.types.hostile;

import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.EntityCreeper;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.reborn.FeatherDisguise.metadata.EntityMetadataIndexes;
import org.reborn.FeatherDisguise.metadata.EntityType;
import org.reborn.FeatherDisguise.metadata.modal.LivingEntityMetadataHolder;

public class CreeperMetadataHolder extends LivingEntityMetadataHolder<EntityType<EntityCreeper>> {

    public CreeperMetadataHolder() {
        super(EntityType.CREEPER);
    }

    @Override
    protected void constructDefaultMetadata() {
        super.constructDefaultMetadata();
        this.setFuseState(CreeperFuseState.IDLE);
        this.setPowered(this.isPowered());
        this.setIgnited(this.isIgnited());
    }

    @ApiStatus.Internal
    private byte getInternalCreeperStateID() {
        return this.getIndex((byte) EntityMetadataIndexes.CREEPER_STATE, (byte) CreeperFuseState.IDLE.getStateID());
    }

    @NotNull public CreeperFuseState getFuseState() {
        return this.getInternalCreeperStateID() == CreeperFuseState.CHARGING.getStateID() ? CreeperFuseState.CHARGING : CreeperFuseState.IDLE;
    }

    public void setFuseState(@NotNull CreeperFuseState fuseState) {
        this.setIndex((byte) EntityMetadataIndexes.CREEPER_STATE, EntityDataTypes.BYTE, (byte) fuseState.getStateID());
    }

    @ApiStatus.Internal
    private byte getInternalCreeperIsPoweredID() {
        return this.getIndex((byte) EntityMetadataIndexes.CREEPER_IS_POWERED, (byte) 0);
    }

    public boolean isPowered() { // has the lightning effect rendered around the model
        return this.getInternalCreeperIsPoweredID() != 0;
    }

    public void setPowered(boolean setPowered) {
        this.setIndex((byte) EntityMetadataIndexes.CREEPER_IS_POWERED, EntityDataTypes.BYTE, (byte) (setPowered ? 1 : 0));
    }

    @ApiStatus.Internal
    private byte getInternalCreeperIsIgnitedID() {
        return this.getIndex((byte) EntityMetadataIndexes.CREEPER_IS_IGNITED, (byte) 0);
    }

    public boolean isIgnited() { // makes the creeper look thicc
        return this.getInternalCreeperIsIgnitedID() != 0;
    }

    public void setIgnited(boolean setIgnited) {
        this.setIndex((byte) EntityMetadataIndexes.CREEPER_IS_IGNITED, EntityDataTypes.BYTE, (byte) (setIgnited ? 1 : 0));
    }

    @AllArgsConstructor @Getter
    public enum CreeperFuseState {
        IDLE(-1),
        CHARGING(1);

        private final int stateID;
    }
}
