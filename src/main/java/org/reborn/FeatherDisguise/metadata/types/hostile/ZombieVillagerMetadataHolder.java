package org.reborn.FeatherDisguise.metadata.types.hostile;

import org.reborn.FeatherDisguise.metadata.EntityType;

public class ZombieVillagerMetadataHolder extends ZombieMetadataHolder {

    public ZombieVillagerMetadataHolder() {
        super(EntityType.ZOMBIE_VILLAGER);
    }

    @Override
    protected void constructDefaultMetadata() {
        super.constructDefaultMetadata();
        this.setZombifiedVillager(true);
    }
}
