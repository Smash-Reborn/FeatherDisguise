package org.reborn.FeatherDisguise.wrapper;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.player.Equipment;
import com.github.retrooper.packetevents.protocol.player.EquipmentSlot;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.*;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.reborn.FeatherDisguise.metadata.AbstractMetadataHolder;
import org.reborn.FeatherDisguise.metadata.EntityDimensions;
import org.reborn.FeatherDisguise.metadata.EntityType;
import org.reborn.FeatherDisguise.metadata.modal.LivingEntityMetadataHolder;
import org.reborn.FeatherDisguise.metadata.types.ArmorStandMetadataHolder;
import org.reborn.FeatherDisguise.types.AbstractDisguise;
import org.reborn.FeatherDisguise.util.DisguiseUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class DisguiseRelatedEntityWrapper<E extends AbstractMetadataHolder<?>> {

    @NotNull private final AbstractDisguise<?> owningDisguise;

    @NotNull private final VirtualDisguiseEntityData<E> baseDisguiseEntity;

    @NotNull private final VirtualDisguiseEntityData<LivingEntityMetadataHolder<EntityType<EntitySquid>>> hittableSquidEntity;
    @NotNull private final VirtualDisguiseEntityData<ArmorStandMetadataHolder> nametagArmorStandEntity;

    public DisguiseRelatedEntityWrapper(@NotNull final AbstractDisguise<?> owningDisguise, @NotNull final E baseDisguiseEntityMetadataHolder) {
        this.owningDisguise = owningDisguise;
        final World bukkitWorld = owningDisguise.getOwningBukkitPlayer().getWorld();
        this.baseDisguiseEntity = new VirtualDisguiseEntityData<>(baseDisguiseEntityMetadataHolder, bukkitWorld);

        this.hittableSquidEntity = new VirtualDisguiseEntityData<>(new LivingEntityMetadataHolder<>(EntityType.SQUID), bukkitWorld);
        this.nametagArmorStandEntity = new VirtualDisguiseEntityData<>(new ArmorStandMetadataHolder(), bukkitWorld);

        this.modifyRelatedEntitiesAndPrepareForSpawning();
    }

    @ApiStatus.Internal
    private void modifyRelatedEntitiesAndPrepareForSpawning() {

        // squid which is hittable, appears on the top position of all disguises
        this.hittableSquidEntity.getMetadataHolder().setInvisible(false);

        // armor-stand which displays the name-tag for the disguise owner
        this.nametagArmorStandEntity.getMetadataHolder().setInvisible(false);
        this.nametagArmorStandEntity.getMetadataHolder().setMarker(true);
        this.nametagArmorStandEntity.getMetadataHolder().setRemovedBaseplate(true);
        this.nametagArmorStandEntity.getMetadataHolder().setSmall(true);
        this.nametagArmorStandEntity.getMetadataHolder().setCustomName(this.owningDisguise.getDisguiseNametag());
        this.nametagArmorStandEntity.getMetadataHolder().setCustomNameVisible(true);
    }

    @NotNull public Optional<List<PacketWrapper<?>>> generateSpawningPacketsForAllDisguiseRelatedEntities() {
        final Location disguisedPlayerCurrentPosRot = this.owningDisguise.getOwningBukkitPlayer().getLocation().clone();
        final List<PacketWrapper<?>> spawningPackets = new ArrayList<>(6);
        // base disguise spawn
        // base disguise equipment
        // squid entity spawn
        // armor-stand entity spawn
        // armor-stand entity metadata
        // armor-stand attach -> squid entity

        Optional<List<EntityData>> optDisguiseEntityDependentMetadata = this.baseDisguiseEntity.getMetadataHolder().getConstructedListOfMetadata();
        if (!optDisguiseEntityDependentMetadata.isPresent()) {
            return Optional.empty(); // exit early if we cannot construct metadata. the result will be crashing clients!
        }

        spawningPackets.add(new WrapperPlayServerSpawnLivingEntity(
                this.baseDisguiseEntity.getVirtualID(), this.baseDisguiseEntity.getVirtualUUID(),
                SpigotConversionUtil.fromBukkitEntityType(this.baseDisguiseEntity.getMetadataHolder().getEntityType().getBukkitEntityType()),
                SpigotConversionUtil.fromBukkitLocation(disguisedPlayerCurrentPosRot).getPosition(),
                this.owningDisguise.isHeadRotationYawLocked() ? 0f : disguisedPlayerCurrentPosRot.getYaw(),
                this.owningDisguise.isHeadRotationPitchLocked() ? 0f : disguisedPlayerCurrentPosRot.getPitch(),
                this.owningDisguise.isHeadRotationPitchLocked() ? 0f : disguisedPlayerCurrentPosRot.getPitch(),
                new Vector3d(
                        this.owningDisguise.getOwningBukkitPlayer().getVelocity().getX(),
                        this.owningDisguise.getOwningBukkitPlayer().getVelocity().getY(),
                        this.owningDisguise.getOwningBukkitPlayer().getVelocity().getZ()),
                optDisguiseEntityDependentMetadata.get()));

        // send an equipment packet for the hand slot if the player is holding an item & the disguise is allowed to show items in the hand
        if (DisguiseUtil.isDisguiseAbleToRenderItemsInHandSlots(this.owningDisguise.getDisguiseType()) &&
                this.owningDisguise.getOwningBukkitPlayer().getEquipment() != null && this.owningDisguise.getOwningBukkitPlayer().getEquipment().getItemInHand().getType() != Material.AIR) {
            spawningPackets.add(new WrapperPlayServerEntityEquipment(this.baseDisguiseEntity.getVirtualID(),
                    Collections.singletonList(
                            new Equipment(EquipmentSlot.MAIN_HAND,
                                    SpigotConversionUtil.fromBukkitItemStack(this.owningDisguise.getOwningBukkitPlayer().getItemInHand())))));
        }

        optDisguiseEntityDependentMetadata = this.hittableSquidEntity.getMetadataHolder().getConstructedListOfMetadata();
        if (!optDisguiseEntityDependentMetadata.isPresent()) {
            return Optional.empty();
        }

        spawningPackets.add(new WrapperPlayServerSpawnLivingEntity(
                this.hittableSquidEntity.getVirtualID(), this.hittableSquidEntity.getVirtualUUID(),
                SpigotConversionUtil.fromBukkitEntityType(this.hittableSquidEntity.getMetadataHolder().getEntityType().getBukkitEntityType()),
                this.owningDisguise.getCalculatedSquidRelatedEntityPos(
                        disguisedPlayerCurrentPosRot.getX(), disguisedPlayerCurrentPosRot.getY(), disguisedPlayerCurrentPosRot.getZ()),
                0f, 0f, 0f, Vector3d.zero(),
                optDisguiseEntityDependentMetadata.get()));

        spawningPackets.add(new WrapperPlayServerSpawnEntity(
                this.nametagArmorStandEntity.getVirtualID(), Optional.empty(),
                SpigotConversionUtil.fromBukkitEntityType(this.nametagArmorStandEntity.getMetadataHolder().getEntityType().getBukkitEntityType()),
                this.owningDisguise.getCalculatedSquidRelatedEntityPos(
                        disguisedPlayerCurrentPosRot.getX(), disguisedPlayerCurrentPosRot.getY(), disguisedPlayerCurrentPosRot.getZ()),
                0f, 0f, 0f, 0, Optional.empty()));

        optDisguiseEntityDependentMetadata = this.nametagArmorStandEntity.getMetadataHolder().getConstructedListOfMetadata();
        if (!optDisguiseEntityDependentMetadata.isPresent()) {
            return Optional.empty();
        }

        spawningPackets.add(new WrapperPlayServerEntityMetadata(
                this.nametagArmorStandEntity.getVirtualID(), optDisguiseEntityDependentMetadata.get()));

        spawningPackets.add(new WrapperPlayServerAttachEntity(
                this.nametagArmorStandEntity.getVirtualID(), this.hittableSquidEntity.getVirtualID(), false));

        return Optional.of(spawningPackets);
    }

    @NotNull public WrapperPlayServerDestroyEntities generateDestroyPacketForAllDisguiseRelatedEntities() {
        return new WrapperPlayServerDestroyEntities(
                this.baseDisguiseEntity.getVirtualID(),
                this.nametagArmorStandEntity.getVirtualID(),
                this.hittableSquidEntity.getVirtualID());
    }

    @NotNull public EntityDimensions getBaseDisguiseDimensions() {
        return this.baseDisguiseEntity.getMetadataHolder().getEntityType().getEntityDimensions();
    }
}
