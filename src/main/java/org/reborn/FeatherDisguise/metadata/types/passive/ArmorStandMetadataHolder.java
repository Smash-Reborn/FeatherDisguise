package org.reborn.FeatherDisguise.metadata.types.passive;

import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.util.Vector3f;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
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
        this.setMask((byte) EntityMetadataIndexes.ARMOR_STAND_GENERIC, (byte) 0); // makes the bitmask 0, which is the default for armorstand entities
        this.setHeadRotation(this.getHeadRotation());
        this.setBodyRotation(this.getBodyRotation());
        this.setLeftArmRotation(this.getLeftArmRotation());
        this.setRightArmRotation(this.getRightArmRotation());
        this.setLeftLegRotation(this.getLeftLegRotation());
        this.setRightLegRotation(this.getRightLegRotation());
    }

    public boolean isSmall() {
        return this.getMaskBit((byte) EntityMetadataIndexes.ARMOR_STAND_GENERIC, ArmorStandBitMaskType.IS_SMALL.getBitValue());
    }

    public void setSmall(boolean isSmall) {
        this.setMaskBit(EntityMetadataIndexes.ARMOR_STAND_GENERIC, ArmorStandBitMaskType.IS_SMALL.getBitValue(), isSmall);
    }

    public boolean hasGravity() {
        return this.getMaskBit((byte) EntityMetadataIndexes.ARMOR_STAND_GENERIC, ArmorStandBitMaskType.HAS_GRAVITY.getBitValue());
    }

    public void setGravity(boolean hasGravity) {
        this.setMaskBit(EntityMetadataIndexes.ARMOR_STAND_GENERIC, ArmorStandBitMaskType.HAS_GRAVITY.getBitValue(), hasGravity);
    }

    public boolean hasArms() {
        return this.getMaskBit((byte) EntityMetadataIndexes.ARMOR_STAND_GENERIC, ArmorStandBitMaskType.HAS_ARMS.getBitValue());
    }

    public void setArms(boolean hasArms) {
        this.setMaskBit(EntityMetadataIndexes.ARMOR_STAND_GENERIC, ArmorStandBitMaskType.HAS_ARMS.getBitValue(), hasArms);
    }

    public boolean hasRemovedBaseplate() {
        return this.getMaskBit((byte) EntityMetadataIndexes.ARMOR_STAND_GENERIC, ArmorStandBitMaskType.REMOVE_BASEPLATE.getBitValue());
    }

    public void setRemovedBaseplate(boolean removedBaseplate) {
        this.setMaskBit(EntityMetadataIndexes.ARMOR_STAND_GENERIC, ArmorStandBitMaskType.REMOVE_BASEPLATE.getBitValue(), removedBaseplate);
    }

    public boolean isMarker() {
        return this.getMaskBit((byte) EntityMetadataIndexes.ARMOR_STAND_GENERIC, ArmorStandBitMaskType.IS_MARKER.getBitValue());
    }

    public void setMarker(boolean isMarker) {
        this.setMaskBit(EntityMetadataIndexes.ARMOR_STAND_GENERIC, ArmorStandBitMaskType.IS_MARKER.getBitValue(), isMarker);
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

    public void setLeftLegRotation(@NotNull Vector3f leftLegRotation) { // todo for some reason this is throwing a massive error???
        this.setIndex((byte) EntityMetadataIndexes.ARMOR_STAND_LEFT_LEG_POSITION, EntityDataTypes.ROTATION, leftLegRotation);
    }

    @NotNull public Vector3f getRightLegRotation() {
        return this.getIndex((byte) EntityMetadataIndexes.ARMOR_STAND_RIGHT_LEG_POSITION, new Vector3f(1.0f, 0f, 1.0f));
    }

    public void setRightLegRotation(@NotNull Vector3f rightLegRotation) {
        this.setIndex((byte) EntityMetadataIndexes.ARMOR_STAND_RIGHT_LEG_POSITION, EntityDataTypes.ROTATION, rightLegRotation);
    }

    @Getter @AllArgsConstructor
    public enum ArmorStandBitMaskType {
        IS_SMALL(0, (byte) 0x01),
        HAS_GRAVITY(1, (byte) 0x02),
        HAS_ARMS(2, (byte) 0x04),
        REMOVE_BASEPLATE(3, (byte) 0x08),
        IS_MARKER(4, (byte) 0x16);

        private final int bitID;
        private final byte bitValue;
    }
}
