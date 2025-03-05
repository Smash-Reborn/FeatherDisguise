package org.reborn.FeatherDisguise.metadata.types.passive;

import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.EntityRabbit;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.reborn.FeatherDisguise.metadata.EntityMetadataIndexes;
import org.reborn.FeatherDisguise.metadata.EntityType;
import org.reborn.FeatherDisguise.metadata.modal.AgedEntityMetadataHolder;

public class RabbitMetadataHolder extends AgedEntityMetadataHolder<EntityType<EntityRabbit>> {

    public RabbitMetadataHolder() {
        super(EntityType.RABBIT);
    }

    @Override
    protected void constructDefaultMetadata() {
        super.constructDefaultMetadata();
        this.setRabbitVariant(RabbitVariant.BROWN);
    }

    @ApiStatus.Internal
    private byte getInternalRabbitTypeID() {
        return this.getIndex((byte) EntityMetadataIndexes.RABBIT_TYPE, (byte) RabbitVariant.BROWN.getVariantID());
    }

    @NotNull public RabbitVariant getRabbitVariant() {
        return this.getInternalRabbitTypeID() == RabbitVariant.KILLER.getVariantID() ? RabbitVariant.KILLER :
                RabbitVariant.values()[this.getInternalRabbitTypeID()];
    }

    public void setRabbitVariant(@NotNull RabbitVariant rabbitVariant) {
        this.setIndex((byte) EntityMetadataIndexes.RABBIT_TYPE, EntityDataTypes.BYTE, (byte) rabbitVariant.getVariantID());
    }

    @Getter @AllArgsConstructor
    public enum RabbitVariant {
        BROWN(0),
        WHITE(1),
        BLACK(2),
        PATCHES(3),
        GOLD(4),
        PEPPER(5),
        KILLER(99);         // BRING FORTH THE HOLY HAND GRENADE

        private final int variantID;
    }
}
