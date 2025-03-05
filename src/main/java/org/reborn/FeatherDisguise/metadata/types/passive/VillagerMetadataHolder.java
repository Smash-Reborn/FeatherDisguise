package org.reborn.FeatherDisguise.metadata.types.passive;

import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import net.minecraft.server.v1_8_R3.EntityVillager;
import org.bukkit.entity.Villager;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.reborn.FeatherDisguise.metadata.EntityMetadataIndexes;
import org.reborn.FeatherDisguise.metadata.EntityType;
import org.reborn.FeatherDisguise.metadata.modal.AgedEntityMetadataHolder;

public class VillagerMetadataHolder extends AgedEntityMetadataHolder<EntityType<EntityVillager>> {

    public VillagerMetadataHolder() {
        super(EntityType.VILLAGER);
    }

    @Override
    protected void constructDefaultMetadata() {
        super.constructDefaultMetadata();
        this.setVillagerType(Villager.Profession.FARMER);   // it ain't much but it's honest work
    }

    @ApiStatus.Internal
    private int getInternalVillagerTypeID() {
        return this.getIndex((byte) EntityMetadataIndexes.VILLAGER_TYPE, Villager.Profession.FARMER.ordinal());
    }

    @NotNull public Villager.Profession getVillagerType() {
        return Villager.Profession.values()[this.getInternalVillagerTypeID()];
    }

    public void setVillagerType(@NotNull Villager.Profession profession) {
        this.setIndex((byte) EntityMetadataIndexes.VILLAGER_TYPE, EntityDataTypes.INT, profession.ordinal());
    }
}
