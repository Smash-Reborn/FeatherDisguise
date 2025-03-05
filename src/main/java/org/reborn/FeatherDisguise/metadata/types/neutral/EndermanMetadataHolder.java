package org.reborn.FeatherDisguise.metadata.types.neutral;

import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import net.minecraft.server.v1_8_R3.EntityEnderman;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.reborn.FeatherDisguise.metadata.EntityMetadataIndexes;
import org.reborn.FeatherDisguise.metadata.EntityType;
import org.reborn.FeatherDisguise.metadata.modal.LivingEntityMetadataHolder;

public class EndermanMetadataHolder extends LivingEntityMetadataHolder<EntityType<EntityEnderman>> {

    public EndermanMetadataHolder() {
        super(EntityType.ENDERMAN);
    }

    @Override
    protected void constructDefaultMetadata() {
        super.constructDefaultMetadata();
        this.clearCarriedBlock();
        this.setScreaming(this.isScreaming());
    }

    public void clearCarriedBlock() {
        this.setIndex((byte) EntityMetadataIndexes.ENDERMAN_CARRIED_BLOCK, EntityDataTypes.SHORT, (short) 0); // blockID
        this.setIndex((byte) EntityMetadataIndexes.ENDERMAN_CARRIED_BLOCK, EntityDataTypes.BYTE, (byte) 0); // blockData
    }

    public void setCarriedBlock(@NotNull ItemStack itemStack) {
        this.setIndex((byte) EntityMetadataIndexes.ENDERMAN_CARRIED_BLOCK, EntityDataTypes.SHORT, (short) (itemStack.getTypeId() & 255)); // blockID
        this.setIndex((byte) EntityMetadataIndexes.ENDERMAN_CARRIED_BLOCK, EntityDataTypes.BYTE, (byte) (itemStack.getDurability() & 255)); // blockData
    }

    @ApiStatus.Internal
    private byte getInternalIsScreamingID() {
        return this.getIndex((byte) EntityMetadataIndexes.ENDERMAN_IS_SCREAMING, (byte) 0);
    }

    public boolean isScreaming() {
        return this.getInternalIsScreamingID() != 0;
    }

    public void setScreaming(boolean screaming) {
        this.setIndex((byte) EntityMetadataIndexes.ENDERMAN_IS_SCREAMING, EntityDataTypes.BYTE, (byte) (screaming ? 1 : 0));
    }

}
