package org.reborn.FeatherDisguise.metadata.types.neutral;

import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import net.minecraft.server.v1_8_R3.EntityWolf;
import net.minecraft.server.v1_8_R3.EnumColor;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.reborn.FeatherDisguise.metadata.EntityMetadataIndexes;
import org.reborn.FeatherDisguise.metadata.EntityType;
import org.reborn.FeatherDisguise.metadata.modal.TameableEntityMetadataHolder;

public class WolfMetadataHolder extends TameableEntityMetadataHolder<EntityType<EntityWolf>> {

    public WolfMetadataHolder() {
        super(EntityType.WOLF);
    }

    @Override
    protected void constructDefaultMetadata() {
        super.constructDefaultMetadata();
        this.setAngry(false);
        this.setWolfHealth(this.getWolfHealth());
        this.setBegging(this.isBegging());
        this.setCollarColor(this.getCollarColor());
    }

    public boolean isAngry() {
        return this.getMaskBit((byte) EntityMetadataIndexes.TAMEABLE_ENTITY_GENERIC, (byte) 0x02);
    }

    public void setAngry(boolean isMad) {
        this.setMaskBit(EntityMetadataIndexes.TAMEABLE_ENTITY_GENERIC, (byte) 0x02, isMad);
    }

    public float getWolfHealth() {
        return this.getIndex((byte) EntityMetadataIndexes.WOLF_HEALTH, this.getHealth());
    }

    public void setWolfHealth(float health) {
        this.setIndex((byte) EntityMetadataIndexes.WOLF_HEALTH, EntityDataTypes.FLOAT, health); // this affects the angle of the tail (lower the health, the lower the tail)
    }

    @ApiStatus.Internal
    private byte getInternalIsBeggingID() {
        return this.getIndex((byte) EntityMetadataIndexes.WOLF_BEGGING, (byte) 0);
    }

    public boolean isBegging() {
        return this.getInternalIsBeggingID() != 0;
    }

    public void setBegging(boolean isBegging) {
        this.setIndex((byte) EntityMetadataIndexes.WOLF_BEGGING, EntityDataTypes.BYTE, (byte) (isBegging ? 1 : 0));
    }

    @ApiStatus.Internal
    private byte getInternalCollarColorID() {
        return this.getIndex((byte) EntityMetadataIndexes.WOLF_COLLAR_COLOR, (byte) EnumColor.RED.getColorIndex());
    }

    @NotNull public EnumColor getCollarColor() {
        return EnumColor.fromInvColorIndex(this.getInternalCollarColorID() & 15);
    }

    public void setCollarColor(@NotNull EnumColor color) {
        this.setIndex((byte) EntityMetadataIndexes.WOLF_COLLAR_COLOR, EntityDataTypes.BYTE, (byte) (color.getInvColorIndex() & 15));
    }
}
