package org.reborn.FeatherDisguise.metadata.types.passive;

import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import net.minecraft.server.v1_8_R3.EntitySheep;
import net.minecraft.server.v1_8_R3.EnumColor;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.reborn.FeatherDisguise.metadata.EntityMetadataIndexes;
import org.reborn.FeatherDisguise.metadata.EntityType;
import org.reborn.FeatherDisguise.metadata.modal.AgedEntityMetadataHolder;

public class SheepMetadataHolder extends AgedEntityMetadataHolder<EntityType<EntitySheep>> {

    public SheepMetadataHolder() {
        super(EntityType.SHEEP);
    }

    @Override
    protected void constructDefaultMetadata() {
        super.constructDefaultMetadata();
        this.setSheepFleeceColor(EnumColor.WHITE);
        this.setSheared(false);
    }

    @ApiStatus.Internal
    private byte getSheepFleeceDataID() {
        return this.getIndex((byte) EntityMetadataIndexes.SHEEP_WOOL_DATA, (byte) 0);
    }

    @NotNull public EnumColor getSheepFleeceColor() {
        return EnumColor.fromColorIndex(this.getSheepFleeceDataID() & 15);
    }

    public void setSheepFleeceColor(@NotNull EnumColor colorData) {
        final byte b = this.getSheepFleeceDataID();
        this.setIndex((byte) EntityMetadataIndexes.SHEEP_WOOL_DATA, EntityDataTypes.BYTE, (byte) (b & 240 | colorData.getColorIndex() & 15));
    }

    public boolean isSheared() {
        return (this.getSheepFleeceDataID() & 16) != 0;
    }

    public void setSheared(boolean setNude) {
        final byte b = this.getSheepFleeceDataID();
        this.setIndex((byte) EntityMetadataIndexes.SHEEP_WOOL_DATA, EntityDataTypes.BYTE, (byte) (setNude ? b | 16 : b & -17));
    }
}
