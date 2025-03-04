package org.reborn.FeatherDisguise.metadata;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataType;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.google.common.base.Preconditions;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Log4j2
public class AbstractMetadataHolder<E extends EntityType<?>> {

    // byte = index
    // entityData = PacketEvents wrapped metadata object
    @NotNull private HashMap<Byte, EntityData> entityMetadata;

    @Getter @NotNull private final E entityType;

    public AbstractMetadataHolder(@NotNull final E entityType) {
        this.entityMetadata = new HashMap<>();
        this.entityType = entityType;
        this.constructDefaultMetadata(); // always construct the default, so we have fallback to prevent any invalid metadata and therefore client crashing
    }

    protected void constructDefaultMetadata() {
        this.setMask((byte) EntityMetadataIndexes.ENTITY_GENERIC, (byte) 0); // maskes the bitmask 0, which is the default
        this.setAirTicks(this.getAirTicks());
        this.setCustomName(null);
        this.setCustomNameVisible(false);
        this.setSilent(true); // stfu!!
        // since all getters have to provide defaults, we can
        // perform a bit of a hack and just piggy-back off of those
    }

    @NotNull public HashMap<Byte, EntityData> getMetadata() {
        return new HashMap<>(entityMetadata); // always clone, never let external calls modify our hashmap
    }

    @NotNull public Optional<List<EntityData>> getConstructedListOfMetadata() {
        if (entityMetadata.isEmpty()) return Optional.empty();
        final List<EntityData> entityData = new ArrayList<>(entityMetadata.size());
        entityMetadata.forEach((ibyte, entData) -> entityData.add(entData));
        return Optional.of(entityData);
    }

    @NotNull public Optional<List<EntityData>> getConstructedListOfMetadata(int... indexes) {
        if (entityMetadata.isEmpty()) return Optional.empty();
        final List<EntityData> entityData = new ArrayList<>(indexes.length);

        for (final int index : indexes) {
            if (!entityMetadata.containsKey((byte) index)) continue;
            entityData.add(entityMetadata.get((byte) index));
        }

        return Optional.of(entityData);
    }

    protected <T> void setIndex(byte index, @NotNull EntityDataType<T> dataType, @Nullable T value) {
        Preconditions.checkNotNull(entityMetadata, "Entity metadata hashmap is invalid or null. Unable to set index");
        this.entityMetadata.put(index, new EntityData(index, dataType, value));
    }

    @SuppressWarnings("unchecked")
    protected <T> T getIndex(byte index, @Nullable T defaultValue) {
        Preconditions.checkNotNull(entityMetadata, "Entity metadata hashmap is invalid or null. Unable to get index");
        final EntityData value = this.entityMetadata.get(index);
        return value != null ? (T) value.getValue() : defaultValue;
    }

    protected <T> void removeIndex(byte index) {
        Preconditions.checkNotNull(entityMetadata, "Entity metadata hashmap is invalid or null. Unable to remove index");
        this.entityMetadata.remove(index);
    }

    protected byte getMask(byte index) {
        return this.getIndex(index, (byte) 0); // default bitmask value
    }

    protected void setMask(byte index, byte mask) {
        this.setIndex(index, EntityDataTypes.BYTE, mask);
    }

    protected boolean getMaskBit(byte index, byte bit) {
        return (this.getMask(index) & bit) == bit;
    }

    protected void setMaskBit(int index, byte bit, boolean value) {
        byte mask = this.getMask((byte) index);
        final boolean currentValue = (mask & bit) == bit;

        // don't bother setting if the value is already the same
        if (currentValue == value) return;

        // pretty much copied from nms source
        if (value) mask |= bit;
        else mask &= (byte) ~bit;

        this.setMask((byte) index, mask);
    }

    public boolean isOnFire() {
        return this.getMaskBit((byte) EntityMetadataIndexes.ENTITY_GENERIC, EntityBitMaskType.ON_FIRE.getBitValue());
    }

    public void setOnFire(boolean onFire) {
        this.setMaskBit(EntityMetadataIndexes.ENTITY_GENERIC, EntityBitMaskType.ON_FIRE.getBitValue(), onFire);
    }

    public boolean isSneaking() {
        return this.getMaskBit((byte) EntityMetadataIndexes.ENTITY_GENERIC, EntityBitMaskType.IS_SNEAKING.getBitValue());
    }

    public void setSneaking(boolean isSneaking) {
        this.setMaskBit(EntityMetadataIndexes.ENTITY_GENERIC, EntityBitMaskType.IS_SNEAKING.getBitValue(), isSneaking);
    }

    public boolean isSprinting() {
        return this.getMaskBit((byte) EntityMetadataIndexes.ENTITY_GENERIC, EntityBitMaskType.IS_SPRINTING.getBitValue());
    }

    public void setSprinting(boolean isSprinting) {
        this.setMaskBit(EntityMetadataIndexes.ENTITY_GENERIC, EntityBitMaskType.IS_SPRINTING.getBitValue(), isSprinting);
    }

    public boolean isPerformingAction() {
        return this.getMaskBit((byte) EntityMetadataIndexes.ENTITY_GENERIC, EntityBitMaskType.IS_DOING_ACTION.getBitValue());
    }

    public void setPerformingAction(boolean isDoingSomething) {
        this.setMaskBit(EntityMetadataIndexes.ENTITY_GENERIC, EntityBitMaskType.IS_DOING_ACTION.getBitValue(), isDoingSomething);
    }

    public boolean isInvisible() {
        return this.getMaskBit((byte) EntityMetadataIndexes.ENTITY_GENERIC, EntityBitMaskType.IS_INVISIBLE.getBitValue());
    }

    public void setInvisible(boolean isInvis) {
        this.setMaskBit(EntityMetadataIndexes.ENTITY_GENERIC, EntityBitMaskType.IS_INVISIBLE.getBitValue(), isInvis);
    }

    public short getAirTicks() {
        return this.getIndex((byte) EntityMetadataIndexes.ENTITY_AIR_SUPPLY, (short) 300);
    }

    public void setAirTicks(short airTicks) {
        this.setIndex((byte) EntityMetadataIndexes.ENTITY_AIR_SUPPLY, EntityDataTypes.SHORT, airTicks);
    }

    @ApiStatus.Internal
    @NotNull private String getInternalCustomName() {
        return this.getIndex((byte) EntityMetadataIndexes.ENTITY_NAMETAG, "");
    }

    @NotNull public Optional<String> getCustomName() {
        final String nametag = this.getInternalCustomName();
        return nametag.isEmpty() ? Optional.empty() : Optional.of(nametag);
    }

    public void setCustomName(@Nullable String name) {
        if (name == null) {name = "";}
        this.setIndex((byte) EntityMetadataIndexes.ENTITY_NAMETAG, EntityDataTypes.STRING, name);
    }

    @ApiStatus.Internal
    private byte getInternalCustomNameVisible() {
        return this.getIndex((byte) EntityMetadataIndexes.ENTITY_SHOW_NAMETAG, (byte) 0); // 0 = false, 1 = true
    }

    public boolean isCustomNameVisible() {
        return this.getInternalCustomNameVisible() == 1;
    }

    public void setCustomNameVisible(boolean nameVisible) {
        this.setIndex((byte) EntityMetadataIndexes.ENTITY_SHOW_NAMETAG, EntityDataTypes.BYTE, (byte) (nameVisible ? 1 : 0));
    }

    @ApiStatus.Internal
    private byte getInternalIsSilent() {
        return this.getIndex((byte) EntityMetadataIndexes.ENTITY_IS_SILENT, (byte) 0);
    }

    public boolean isSilent() {
        return this.getInternalIsSilent() == 1;
    }

    public void setSilent(boolean isSilent) {
        this.setIndex((byte) EntityMetadataIndexes.ENTITY_IS_SILENT, EntityDataTypes.BYTE, (byte) (isSilent ? 1 : 0));
    }

    @Getter @AllArgsConstructor
    public enum EntityBitMaskType {
        ON_FIRE(0, (byte) 0x01),
        IS_SNEAKING(1, (byte) 0x02),
        IS_SPRINTING(3, (byte) 0x08),
        IS_DOING_ACTION(4, (byte) 0x10),    // eating/drinking/blocking
        IS_INVISIBLE(5, (byte) 0x20);
        // check Entity.class (nms) for bit mask values

        private final int bitID;
        private final byte bitValue;
    }
}
