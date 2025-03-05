package org.reborn.FeatherDisguise.metadata.types.hostile;

import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import net.minecraft.server.v1_8_R3.EntityZombie;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.reborn.FeatherDisguise.metadata.EntityMetadataIndexes;
import org.reborn.FeatherDisguise.metadata.EntityType;
import org.reborn.FeatherDisguise.metadata.modal.LivingEntityMetadataHolder;

public class ZombieMetadataHolder extends LivingEntityMetadataHolder<EntityType<EntityZombie>> {

    public ZombieMetadataHolder(@NotNull EntityType<EntityZombie> entityType) {
        super(entityType);
    }

    public ZombieMetadataHolder() {
        super(EntityType.ZOMBIE);
    }

    @Override
    protected void constructDefaultMetadata() {
        super.constructDefaultMetadata();
        this.setChild(this.isChild());
        this.setZombifiedVillager(this.isZombieVillager());
        this.setShakingForConverting(this.isShakingForConverting());
    }

    @ApiStatus.Internal
    private byte getInternalIsChildID() {
        return this.getIndex((byte) EntityMetadataIndexes.ZOMBIE_IS_CHILD, (byte) 0);
    }

    public boolean isChild() {
        return this.getInternalIsChildID() != 0;
    }

    public void setChild(boolean isBebi) {
        this.setIndex((byte) EntityMetadataIndexes.ZOMBIE_IS_CHILD, EntityDataTypes.BYTE, (byte) (isBebi ? 1 : 0));
    }

    @ApiStatus.Internal
    private byte getInternalIsVillagerZombieID() {
        return this.getIndex((byte) EntityMetadataIndexes.ZOMBIE_IS_VILLAGER, (byte) 0);
    }

    public boolean isZombieVillager() {
        return this.getInternalIsVillagerZombieID() != 0;
    }

    public void setZombifiedVillager(boolean isZombified) {
        this.setIndex((byte) EntityMetadataIndexes.ZOMBIE_IS_VILLAGER, EntityDataTypes.BYTE, (byte) (isZombified ? 1 : 0));
    }

    @ApiStatus.Internal
    private byte getInternalIsConvertingID() {
        return this.getIndex((byte) EntityMetadataIndexes.ZOMBIE_IS_CONVERTING, (byte) 0);
    }

    public boolean isShakingForConverting() {
        return this.getInternalIsConvertingID() != 0;
    }

    public void setShakingForConverting(boolean isShaking) {
        this.setIndex((byte) EntityMetadataIndexes.ZOMBIE_IS_CONVERTING, EntityDataTypes.BYTE, (byte) (isShaking ? 1 : 0));
    }
}
