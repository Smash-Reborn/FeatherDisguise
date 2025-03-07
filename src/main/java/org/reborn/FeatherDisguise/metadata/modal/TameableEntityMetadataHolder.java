package org.reborn.FeatherDisguise.metadata.modal;

import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.reborn.FeatherDisguise.metadata.EntityMetadataIndexes;
import org.reborn.FeatherDisguise.metadata.EntityType;

import java.util.Optional;

public class TameableEntityMetadataHolder<E extends EntityType<?>> extends AgedEntityMetadataHolder<E> {

    public TameableEntityMetadataHolder(@NotNull E entityType) {
        super(entityType);
    }

    @Override
    protected void constructDefaultMetadata() {
        super.constructDefaultMetadata(); // construct the AgedEntity metadata
        this.setMask((byte) EntityMetadataIndexes.TAMEABLE_ENTITY_GENERIC, (byte) 0); // makes the bitmask 0, which is the default for tamed entities
        this.setOwnerName("");
    }

    public boolean isTamed() {
        return this.getTamedMaskBit((byte) TamedEntityBitMaskType.IS_TAMED.getBitID());
    }

    public void setTamed(boolean isTamed) {
        this.setTamedMaskBit((byte) TamedEntityBitMaskType.IS_TAMED.getBitID(), isTamed);
    }

    public boolean isSitting() {
        return this.getTamedMaskBit((byte) TamedEntityBitMaskType.IS_SITTING.getBitID());
    }

    public void setSitting(boolean isPlonkedOnDaGround) {
        this.setTamedMaskBit((byte) TamedEntityBitMaskType.IS_SITTING.getBitID(), isPlonkedOnDaGround);
    }

    @ApiStatus.Internal
    @NotNull private String getInternalOwnerName() {
        return this.getIndex((byte) EntityMetadataIndexes.TAMEABLE_ENTITY_OWNER_NAME, "");
    }

    @NotNull public Optional<String> getOwnerName() {
        final String ownerName = this.getInternalOwnerName();
        return ownerName.isEmpty() ? Optional.empty() : Optional.of(ownerName);
    }

    public void setOwnerName(@Nullable String name) {
        if (name == null) {name = "";}
        this.setIndex((byte) EntityMetadataIndexes.TAMEABLE_ENTITY_OWNER_NAME, EntityDataTypes.STRING, name);
    }

    @ApiStatus.Internal
    private boolean getTamedMaskBit(byte bit) {
        return (this.getMask((byte) EntityMetadataIndexes.TAMEABLE_ENTITY_GENERIC) & bit) != 0;
    }

    @ApiStatus.Internal
    private void setTamedMaskBit(byte bit, boolean value) {
        byte mask = this.getIndex((byte) EntityMetadataIndexes.TAMEABLE_ENTITY_GENERIC, (byte) 0); // default bitmask value
        final boolean currentValue = (mask & bit) == bit;
        if (currentValue == value) return;

        if (value) mask = (byte) (mask | bit);
        else mask = (byte) (mask & -(bit + 1));

        this.setMask((byte) EntityMetadataIndexes.TAMEABLE_ENTITY_GENERIC, mask);
    }

    @Getter @AllArgsConstructor
    public enum TamedEntityBitMaskType {
        IS_TAMED(4), // 4, -5
        IS_SITTING(1); // 1, -2

        private final int bitID;
    }
}
