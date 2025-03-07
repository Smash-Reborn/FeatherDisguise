package org.reborn.FeatherDisguise.metadata.types.passive;

import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.util.Vector3f;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.reborn.FeatherDisguise.metadata.EntityMetadataIndexes;
import org.reborn.FeatherDisguise.metadata.EntityType;
import org.reborn.FeatherDisguise.metadata.modal.LivingEntityMetadataHolder;

public class ArmorStandMetadataHolder extends LivingEntityMetadataHolder<EntityType<EntityArmorStand>> {

    public ArmorStandMetadataHolder() {
        super(EntityType.ARMOR_STAND);
    }

    @Override
    protected void constructDefaultMetadata() {
        super.constructDefaultMetadata();
        this.removeIndex((byte) 15); // LivingEntityBase within the NMS uses this for "AI", but armor-stands don't have AI. to avoid client crashes, always remove this index
        this.setMask((byte) EntityMetadataIndexes.ARMOR_STAND_GENERIC, (byte) 0); // makes the bitmask 0, which is the default for armorstand entities
        this.setHeadRotation(this.getHeadRotation());
        this.setBodyRotation(this.getBodyRotation());
        this.setLeftArmRotation(this.getLeftArmRotation());
        this.setRightArmRotation(this.getRightArmRotation());
        this.setLeftLegRotation(this.getLeftLegRotation());
        this.setRightLegRotation(this.getRightLegRotation());
    }

    public boolean isSmall() {
        return this.getArmorStandMaskBit((byte) ArmorStandBitMaskType.IS_SMALL.getBitID());
    }

    public void setSmall(boolean isSmall) {
        this.setArmorStandMaskBit((byte) ArmorStandBitMaskType.IS_SMALL.getBitID(), isSmall);
    }

    public boolean hasNoGravity() {
        return this.getArmorStandMaskBit((byte) ArmorStandBitMaskType.HAS_NO_GRAVITY.getBitID());
    }

    public void setNoGravity(boolean hasNoGravity) {
        this.setArmorStandMaskBit((byte) ArmorStandBitMaskType.HAS_NO_GRAVITY.getBitID(), hasNoGravity);
    }

    public boolean hasArms() {
        return this.getArmorStandMaskBit((byte) ArmorStandBitMaskType.HAS_ARMS.getBitID());
    }

    public void setArms(boolean hasArms) {
        this.setArmorStandMaskBit((byte) ArmorStandBitMaskType.HAS_ARMS.getBitID(), hasArms);
    }

    public boolean hasRemovedBaseplate() {
        return this.getArmorStandMaskBit((byte) ArmorStandBitMaskType.REMOVE_BASEPLATE.getBitID());
    }

    public void setRemovedBaseplate(boolean removedBaseplate) {
        this.setArmorStandMaskBit((byte) ArmorStandBitMaskType.REMOVE_BASEPLATE.getBitID(), removedBaseplate);
    }

    public boolean isMarker() {
        return this.getArmorStandMaskBit((byte) ArmorStandBitMaskType.IS_MARKER.getBitID());
    }

    public void setMarker(boolean isMarker) {
        this.setArmorStandMaskBit((byte) ArmorStandBitMaskType.IS_MARKER.getBitID(), isMarker);
    }

    @NotNull public Vector3f getHeadRotation() {
        return this.getIndex((byte) EntityMetadataIndexes.ARMOR_STAND_HEAD_POSITION, Vector3f.zero());
    }

    public void setHeadRotation(@NotNull Vector3f headRotation) {
        this.setIndex((byte) EntityMetadataIndexes.ARMOR_STAND_HEAD_POSITION, EntityDataTypes.ROTATION, headRotation);
    }

    @NotNull public Vector3f getBodyRotation() {
        return this.getIndex((byte) EntityMetadataIndexes.ARMOR_STAND_BODY_POSITION, Vector3f.zero());
    }

    public void setBodyRotation(@NotNull Vector3f bodyRotation) {
        this.setIndex((byte) EntityMetadataIndexes.ARMOR_STAND_BODY_POSITION, EntityDataTypes.ROTATION, bodyRotation);
    }

    @NotNull public Vector3f getLeftArmRotation() {
        return this.getIndex((byte) EntityMetadataIndexes.ARMOR_STAND_LEFT_ARM_POSITION, new Vector3f(-10.0f, 0f, -10.0f));
    }

    public void setLeftArmRotation(@NotNull Vector3f leftArmRotation) {
        this.setIndex((byte) EntityMetadataIndexes.ARMOR_STAND_LEFT_ARM_POSITION, EntityDataTypes.ROTATION, leftArmRotation);
    }

    @NotNull public Vector3f getRightArmRotation() {
        return this.getIndex((byte) EntityMetadataIndexes.ARMOR_STAND_RIGHT_ARM_POSITION, new Vector3f(-15.0f, 0f, 10.0f));
    }

    public void setRightArmRotation(@NotNull Vector3f rightArmRotation) {
        this.setIndex((byte) EntityMetadataIndexes.ARMOR_STAND_RIGHT_ARM_POSITION, EntityDataTypes.ROTATION, rightArmRotation);
    }

    @NotNull public Vector3f getLeftLegRotation() {
        return this.getIndex((byte) EntityMetadataIndexes.ARMOR_STAND_LEFT_LEG_POSITION, new Vector3f(-1.0f, 0f, -1.0f));
    }

    public void setLeftLegRotation(@NotNull Vector3f leftLegRotation) {
        this.setIndex((byte) EntityMetadataIndexes.ARMOR_STAND_LEFT_LEG_POSITION, EntityDataTypes.ROTATION, leftLegRotation);
    }

    @NotNull public Vector3f getRightLegRotation() {
        return this.getIndex((byte) EntityMetadataIndexes.ARMOR_STAND_RIGHT_LEG_POSITION, new Vector3f(1.0f, 0f, 1.0f));
    }

    public void setRightLegRotation(@NotNull Vector3f rightLegRotation) {
        this.setIndex((byte) EntityMetadataIndexes.ARMOR_STAND_RIGHT_LEG_POSITION, EntityDataTypes.ROTATION, rightLegRotation);
    }

    @ApiStatus.Internal
    private boolean getArmorStandMaskBit(byte bit) {
        return (this.getMask((byte) EntityMetadataIndexes.ARMOR_STAND_GENERIC) & bit) != 0;
    }

    @ApiStatus.Internal
    private void setArmorStandMaskBit(byte bit, boolean value) {
        byte mask = this.getIndex((byte) EntityMetadataIndexes.ARMOR_STAND_GENERIC, (byte) 0); // default bitmask value
        final boolean currentValue = (mask & bit) == bit;
        if (currentValue == value) return;

        if (value) mask = (byte) (mask | bit);
        else mask = (byte) (mask & -(bit + 1));

        this.setMask((byte) EntityMetadataIndexes.ARMOR_STAND_GENERIC, mask);
    }

    @Getter @AllArgsConstructor
    public enum ArmorStandBitMaskType {
        IS_SMALL(1), // 1, -2
        HAS_NO_GRAVITY(2), // 2, -3
        HAS_ARMS(4), // 4, -5
        REMOVE_BASEPLATE(8), // 8, -9
        IS_MARKER(16); // 16, -17

        private final int bitID;
    }
}
