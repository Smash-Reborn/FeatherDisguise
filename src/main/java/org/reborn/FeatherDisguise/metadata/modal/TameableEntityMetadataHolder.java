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
        return this.getMaskBit((byte) EntityMetadataIndexes.TAMEABLE_ENTITY_GENERIC, TamedEntityBitMaskType.IS_TAMED.getBitValue());
    }

    public void setTamed(boolean isTamed) {
        this.setMaskBit(EntityMetadataIndexes.TAMEABLE_ENTITY_GENERIC, TamedEntityBitMaskType.IS_TAMED.getBitValue(), isTamed);
    }

    public boolean isSitting() {
        return this.getMaskBit((byte) EntityMetadataIndexes.TAMEABLE_ENTITY_GENERIC, TamedEntityBitMaskType.IS_SITTING.getBitValue());
    }

    public void setSitting(boolean isPlonkedOnDaGround) {
        this.setMaskBit(EntityMetadataIndexes.TAMEABLE_ENTITY_GENERIC, TamedEntityBitMaskType.IS_SITTING.getBitValue(), isPlonkedOnDaGround);
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

    @Getter @AllArgsConstructor
    public enum TamedEntityBitMaskType {
        IS_TAMED(0, (byte) 0x01),
        IS_SITTING(2, (byte) 0x04);

        private final int bitID;
        private final byte bitValue;
    }
}
