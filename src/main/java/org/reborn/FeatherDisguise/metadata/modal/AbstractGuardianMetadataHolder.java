package org.reborn.FeatherDisguise.metadata.modal;

import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.reborn.FeatherDisguise.metadata.EntityMetadataIndexes;
import org.reborn.FeatherDisguise.metadata.EntityType;

public class AbstractGuardianMetadataHolder<E extends EntityType<?>> extends LivingEntityMetadataHolder<E> {

    public AbstractGuardianMetadataHolder(@NotNull E entityType) {
        super(entityType);
    }

    @Override
    protected void constructDefaultMetadata() {
        super.constructDefaultMetadata();
        this.resetGuardianFlag();
        this.setTargetID(null);
    }

    public boolean isElder() {
        return this.getInternalGuardianMaskBit(GuardianEntityBitMaskType.IS_ELDER.getBitValue());
    }

    public void setElder(boolean isElder) {
        this.setInternalGuardianFlag(GuardianEntityBitMaskType.IS_ELDER, isElder);
    }

    public void setRetractingSpikes(boolean isRetracting) {
        this.setInternalGuardianFlag(GuardianEntityBitMaskType.IS_RETRACTING_SPIKES, isRetracting);
    }

    public void resetGuardianFlag() {
        this.setIndex((byte) EntityMetadataIndexes.GUARDIAN_GENERIC, EntityDataTypes.INT, 0);
    }

    public int getTargetID() {
        return this.getIndex((byte) EntityMetadataIndexes.GUARDIAN_TARGET_ENTITY, 0);
    }

    public boolean hasValidTarget() {
        return this.getTargetID() != 0;
    }

    public void setTargetID(@Nullable Integer targetID) {
        this.setIndex((byte) EntityMetadataIndexes.GUARDIAN_TARGET_ENTITY, EntityDataTypes.INT, targetID != null ? targetID : 0);
    }

    @ApiStatus.Internal
    private int getInternalGuardianFlag() {
        return this.getIndex((byte) EntityMetadataIndexes.GUARDIAN_GENERIC, 0);
    }

    @ApiStatus.Internal
    private boolean getInternalGuardianMaskBit(int bit) {
        return (this.getInternalGuardianFlag() & bit) != 0;
    }

    @ApiStatus.Internal
    private void setInternalGuardianFlag(@NotNull GuardianEntityBitMaskType bitType, boolean flag) {
        int i = this.getInternalGuardianFlag();
        final boolean currentValue = (i & bitType.getBitValue()) == bitType.getBitValue();
        if (currentValue == flag) return;

        if (flag) i |= bitType.getBitValue();
        else i &= ~bitType.getBitValue();

        this.setIndex((byte) EntityMetadataIndexes.GUARDIAN_GENERIC, EntityDataTypes.INT, i);
    }

    @Getter @AllArgsConstructor
    public enum GuardianEntityBitMaskType {
        IS_RETRACTING_SPIKES(2),
        IS_ELDER(4);

        private final int bitValue;
    }
}
