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
        this.setIndex((byte) EntityMetadataIndexes.HORSE_GENERIC, EntityDataTypes.INT, 0); // makes the value 0, which is the default for horse entities
        this.setHorseType(this.getHorseType());
        this.setHorseColorStyleID(this.getInternalHorseColorStyleID());
        this.setOwnerName("");
        this.setHorseArmorType(this.getHorseArmorType());
    }

    public boolean isTamed() {
        return this.getHorseMaskBit(HorseEntityBitMaskType.IS_TAME.getBitID());
    }

    public void setTamed(boolean isTamed) {
        this.setHorseMaskBit(HorseEntityBitMaskType.IS_TAME.getBitID(), isTamed);
    }

    public boolean hasSaddle() {
        return this.getHorseMaskBit(HorseEntityBitMaskType.HAS_SADDLE.getBitID());
    }

    public void setSaddled(boolean isSaddled) {
        this.setHorseMaskBit(HorseEntityBitMaskType.HAS_SADDLE.getBitID(), isSaddled);
    }

    public boolean hasChest() {
        return this.getHorseMaskBit(HorseEntityBitMaskType.HAS_CHEST.getBitID());
    }

    public void setHasChest(boolean hasChest) {
        this.setHorseMaskBit(HorseEntityBitMaskType.HAS_CHEST.getBitID(), hasChest);
    }

    public boolean hasHadSexualIntercourse() {
        return this.getHorseMaskBit(HorseEntityBitMaskType.HAS_HAD_SEX.getBitID());
    }

    public void setHavingSex(boolean isSexing) {
        this.setHorseMaskBit(HorseEntityBitMaskType.HAS_HAD_SEX.getBitID(), isSexing);
    }

    public boolean isEating() {
        return this.getHorseMaskBit(HorseEntityBitMaskType.IS_EATING.getBitID());
    }

    public void setEating(boolean isEating) {
        this.setHorseMaskBit(HorseEntityBitMaskType.IS_EATING.getBitID(), isEating);
    }

    public boolean isRearing() {
        return this.getHorseMaskBit(HorseEntityBitMaskType.IS_REARING.getBitID());
    }

    public void setRearing(boolean isRearing) {
        this.setHorseMaskBit(HorseEntityBitMaskType.IS_REARING.getBitID(), isRearing);
    }

    public boolean isMouthOpen() {
        return this.getHorseMaskBit(HorseEntityBitMaskType.IS_MOUTH_OPEN.getBitID());
    }

    public void setMouthOpen(boolean isMouthOpen) {
        this.setHorseMaskBit(HorseEntityBitMaskType.IS_MOUTH_OPEN.getBitID(), isMouthOpen);
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

    @ApiStatus.Internal
    private boolean getHorseMaskBit(int bit) {
        return (this.getIndex((byte) EntityMetadataIndexes.HORSE_GENERIC, 0) & bit) != 0;
    }

    @ApiStatus.Internal
    private void setHorseMaskBit(int bit, boolean value) {
        int mask = this.getIndex((byte) EntityMetadataIndexes.HORSE_GENERIC, 0); // horses use integers LOL
        final boolean currentValue = (mask & bit) == bit;
        if (currentValue == value) return;

        if (value) mask |= bit;
        else mask &= ~bit;

        this.setIndex((byte) EntityMetadataIndexes.HORSE_GENERIC, EntityDataTypes.INT, mask);
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
        IS_TAME(2),
        HAS_SADDLE(4),
        HAS_CHEST(8),
        HAS_HAD_SEX(16),
        IS_EATING(32),
        IS_REARING(64),
        IS_MOUTH_OPEN(128);
        // taken from EntityHorse.class (nms)

        private final int bitID;
    }

}
