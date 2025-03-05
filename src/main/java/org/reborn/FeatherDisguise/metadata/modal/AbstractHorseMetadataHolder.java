package org.reborn.FeatherDisguise.metadata.modal;

import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Horse;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.reborn.FeatherDisguise.metadata.EntityMetadataIndexes;
import org.reborn.FeatherDisguise.metadata.EntityType;

import java.util.Optional;

public class AbstractHorseMetadataHolder<E extends EntityType<?>> extends AgedEntityMetadataHolder<E> {

    public AbstractHorseMetadataHolder(@NotNull E entityType) {
        super(entityType);
    }

    @Override
    protected void constructDefaultMetadata() {
        super.constructDefaultMetadata();
        this.setMask((byte) EntityMetadataIndexes.HORSE_GENERIC, (byte) 0); // makes the bitmask 0, which is the default for horse entities
        this.setHorseType(this.getHorseType());
        this.setHorseColorStyleID(this.getInternalHorseColorStyleID());
        this.setOwnerName("");
        this.setHorseArmorType(this.getHorseArmorType());
    }

    public boolean isTamed() {
        return this.getMaskBit((byte) EntityMetadataIndexes.HORSE_GENERIC, HorseEntityBitMaskType.IS_TAME.getBitValue());
    }

    public void setTamed(boolean isTamed) {
        this.setMaskBit(EntityMetadataIndexes.HORSE_GENERIC, HorseEntityBitMaskType.IS_TAME.getBitValue(), isTamed);
    }

    public boolean hasSaddle() {
        return this.getMaskBit((byte) EntityMetadataIndexes.HORSE_GENERIC, HorseEntityBitMaskType.HAS_SADDLE.getBitValue());
    }

    public void setSaddled(boolean isSaddled) {
        this.setMaskBit(EntityMetadataIndexes.HORSE_GENERIC, HorseEntityBitMaskType.HAS_SADDLE.getBitValue(), isSaddled);
    }

    public boolean hasChest() {
        return this.getMaskBit((byte) EntityMetadataIndexes.HORSE_GENERIC, HorseEntityBitMaskType.HAS_CHEST.getBitValue());
    }

    public void setHasChest(boolean hasChest) {
        this.setMaskBit(EntityMetadataIndexes.HORSE_GENERIC, HorseEntityBitMaskType.HAS_CHEST.getBitValue(), hasChest);
    }

    public boolean hasHadSexualIntercourse() {
        return this.getMaskBit((byte) EntityMetadataIndexes.HORSE_GENERIC, HorseEntityBitMaskType.HAS_HAD_SEX.getBitValue());
    }

    public void setHavingSex(boolean isSexing) {
        this.setMaskBit(EntityMetadataIndexes.HORSE_GENERIC, HorseEntityBitMaskType.HAS_HAD_SEX.getBitValue(), isSexing);
    }

    public boolean isEating() {
        return this.getMaskBit((byte) EntityMetadataIndexes.HORSE_GENERIC, HorseEntityBitMaskType.IS_EATING.getBitValue());
    }

    public void setEating(boolean isEating) {
        this.setMaskBit(EntityMetadataIndexes.HORSE_GENERIC, HorseEntityBitMaskType.IS_EATING.getBitValue(), isEating);
    }

    public boolean isRearing() {
        return this.getMaskBit((byte) EntityMetadataIndexes.HORSE_GENERIC, HorseEntityBitMaskType.IS_REARING.getBitValue());
    }

    public void setRearing(boolean isRearing) {
        this.setMaskBit(EntityMetadataIndexes.HORSE_GENERIC, HorseEntityBitMaskType.IS_REARING.getBitValue(), isRearing);
    }

    public boolean isMouthOpen() {
        return this.getMaskBit((byte) EntityMetadataIndexes.HORSE_GENERIC, HorseEntityBitMaskType.IS_MOUTH_OPEN.getBitValue());
    }

    public void setMouthOpen(boolean isMouthOpen) {
        this.setMaskBit(EntityMetadataIndexes.HORSE_GENERIC, HorseEntityBitMaskType.IS_MOUTH_OPEN.getBitValue(), isMouthOpen);
    }

    @ApiStatus.Internal
    private byte getInternalHorseTypeID() {
        return this.getIndex((byte) EntityMetadataIndexes.HORSE_TYPE, (byte) Horse.Variant.HORSE.ordinal());
    }

    @NotNull public Horse.Variant getHorseType() {
        return Horse.Variant.values()[getInternalHorseTypeID()]; // these match the NMS order ((:
    }

    public void setHorseType(@NotNull Horse.Variant horseType) {
        this.setIndex((byte) EntityMetadataIndexes.HORSE_TYPE, EntityDataTypes.BYTE, (byte) horseType.ordinal());
    }

    @ApiStatus.Internal
    public int getInternalHorseColorStyleID() {
        return this.getIndex((byte) EntityMetadataIndexes.HORSE_COLOR_STYLE, 0);  // todo actually not bothered to figure out how to do horse color styles rn LMAO
    }

    public void setHorseColorStyleID(int colorStyleID) {
        this.setIndex((byte) EntityMetadataIndexes.HORSE_COLOR_STYLE, EntityDataTypes.INT, colorStyleID);
    }

    @ApiStatus.Internal
    @NotNull private String getInternalOwnerName() {
        return this.getIndex((byte) EntityMetadataIndexes.HORSE_OWNER_NAME, "");
    }

    @NotNull public Optional<String> getOwnerName() {
        final String ownerName = this.getInternalOwnerName();
        return ownerName.isEmpty() ? Optional.empty() : Optional.of(ownerName);
    }

    public void setOwnerName(@Nullable String name) {
        if (name == null) {name = "";}
        this.setIndex((byte) EntityMetadataIndexes.HORSE_OWNER_NAME, EntityDataTypes.STRING, name);
    }

    @ApiStatus.Internal
    private int getInternalHorseArmorTypeID() {
        return this.getIndex((byte) EntityMetadataIndexes.HORSE_ARMOR_TYPE, HorseEntityArmorType.NONE.ordinal());
    }

    @NotNull public HorseEntityArmorType getHorseArmorType() {
        return HorseEntityArmorType.values()[this.getInternalHorseArmorTypeID()];
    }

    public void setHorseArmorType(@NotNull HorseEntityArmorType armorType) {
        this.setIndex((byte) EntityMetadataIndexes.HORSE_ARMOR_TYPE, EntityDataTypes.INT, armorType.ordinal());
    }

    @NoArgsConstructor
    public enum HorseEntityArmorType {
        NONE,
        IRON_ARMOR,
        GOLD_ARMOR,
        DIAMOND_ARMOR;
    }

    @Getter @AllArgsConstructor
    public enum HorseEntityBitMaskType {
        IS_TAME((byte) 0x02),
        HAS_SADDLE((byte) 0x04),
        HAS_CHEST((byte) 0x08),
        HAS_HAD_SEX((byte) 0x10),
        IS_EATING((byte) 0x20),
        IS_REARING((byte) 0x40),
        IS_MOUTH_OPEN((byte) 0x80);
        // taken from EntityHorse.class (nms)

        private final byte bitValue;
    }

}
