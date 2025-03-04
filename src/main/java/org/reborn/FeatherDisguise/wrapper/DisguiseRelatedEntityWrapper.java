package org.reborn.FeatherDisguise.wrapper;

import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnLivingEntity;
import net.minecraft.server.v1_8_R3.*;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.reborn.FeatherDisguise.types.AbstractDisguise;
import org.reborn.FeatherDisguise.util.DisguiseHelper;
import org.reborn.FeatherDisguise.util.PacketRef;

import java.util.ArrayList;
import java.util.List;

public class DisguiseRelatedEntityWrapper<E extends Entity> {

    @NotNull private final AbstractDisguise<?> owningDisguise;

    @NotNull private final E baseDisguiseEntity;

    @NotNull private final EntitySquid hittableSquidEntity;

    @NotNull private final EntityArmorStand nametagArmorStandEntity;

    public DisguiseRelatedEntityWrapper(@NotNull final AbstractDisguise<?> owningDisguise, @NotNull final E baseDisguiseEntityObject) {
        this.owningDisguise = owningDisguise;
        this.baseDisguiseEntity = baseDisguiseEntityObject;

        this.hittableSquidEntity = new EntitySquid(DisguiseHelper.getNMSWorldFromBukkitPlayer(owningDisguise.getOwningBukkitPlayer()));
        this.nametagArmorStandEntity = new EntityArmorStand(DisguiseHelper.getNMSWorldFromBukkitPlayer(owningDisguise.getOwningBukkitPlayer()));

        this.modifyRelatedEntitiesAndPrepareForSpawning();
    }

    @ApiStatus.Internal
    private void modifyRelatedEntitiesAndPrepareForSpawning() {

        // squid which is hittable, appears on the top position of all disguises
        this.hittableSquidEntity.setInvisible(false);

        // armor-stand which displays the name-tag for the disguise owner
        this.nametagArmorStandEntity.setInvisible(false);
        this.nametagArmorStandEntity.n(true); // setMarker()
        this.nametagArmorStandEntity.setBasePlate(false);
        this.nametagArmorStandEntity.setSmall(true);
        this.nametagArmorStandEntity.setCustomName(this.owningDisguise.getDisguiseNametag());
        this.nametagArmorStandEntity.setCustomNameVisible(true);
    }

    @NotNull public List<PacketRef<?>> generateSpawningPacketsForAllDisguiseRelatedEntities() {
        final List<PacketRef<?>> spawningPackets = new ArrayList<>(8);
        // base disguise spawn
        // base disguise metadata
        // base disguise equipment
        // squid entity spawn
        // squid entity metadata
        // armor-stand entity spawn
        // armor-stand metadata
        // armor-stand attach -> squid entity

        spawningPackets.add(new WrapperPlayServerSpawnLivingEntity(
                this.baseDisguiseEntity.getId(), this.baseDisguiseEntity.getUniqueID(),

        ))
    }
}
