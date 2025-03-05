package org.reborn.FeatherDisguise.metadata.types.hostile;

import net.minecraft.server.v1_8_R3.EntityZombie;
import org.reborn.FeatherDisguise.metadata.EntityType;
import org.reborn.FeatherDisguise.metadata.modal.AbstractZombieMetadataHolder;

public class ZombieMetadataHolder extends AbstractZombieMetadataHolder<EntityType<EntityZombie>> {

    public ZombieMetadataHolder() {
        super(EntityType.ZOMBIE);
    }
}
