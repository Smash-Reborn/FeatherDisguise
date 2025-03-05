package org.reborn.FeatherDisguise.metadata.types.passive;

import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import net.minecraft.server.v1_8_R3.EntityOcelot;
import org.bukkit.entity.Ocelot;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.reborn.FeatherDisguise.metadata.EntityMetadataIndexes;
import org.reborn.FeatherDisguise.metadata.EntityType;
import org.reborn.FeatherDisguise.metadata.modal.TameableEntityMetadataHolder;

public class OcelotMetadataHolder extends TameableEntityMetadataHolder<EntityType<EntityOcelot>> {

    public OcelotMetadataHolder() {
        super(EntityType.OCELOT);
    }

    @Override
    protected void constructDefaultMetadata() {
        super.constructDefaultMetadata();
        this.setOcelotType(this.getOcelotType());
    }

    @ApiStatus.Internal
    private byte getInternalOcelotTypeID() {
        return this.getIndex((byte) EntityMetadataIndexes.OCELOT_TYPE, (byte) Ocelot.Type.WILD_OCELOT.ordinal());
    }

    @NotNull public Ocelot.Type getOcelotType() {
        return Ocelot.Type.values()[this.getInternalOcelotTypeID()];
    }

    public void setOcelotType(@NotNull Ocelot.Type ocelotType) {
        this.setIndex((byte) EntityMetadataIndexes.OCELOT_TYPE, EntityDataTypes.BYTE, (byte) ocelotType.ordinal());
    }
}
