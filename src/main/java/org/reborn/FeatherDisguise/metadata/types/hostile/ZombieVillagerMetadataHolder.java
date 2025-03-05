package org.reborn.FeatherDisguise.metadata.types.hostile;

import net.minecraft.server.v1_8_R3.EntityZombie;
import org.reborn.FeatherDisguise.metadata.EntityType;
import org.reborn.FeatherDisguise.metadata.modal.AbstractZombieMetadataHolder;

public class ZombieVillagerMetadataHolder extends AbstractZombieMetadataHolder<EntityType<EntityZombie>> {

    public ZombieVillagerMetadataHolder() {
        super(EntityType.ZOMBIE_VILLAGER);
    }

    @Override
    protected void constructDefaultMetadata() {
        super.constructDefaultMetadata();
        this.setZombifiedVillager(true);
    }
}
