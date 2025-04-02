package org.reborn.FeatherDisguise.wrapper;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.player.Equipment;
import com.github.retrooper.packetevents.protocol.player.EquipmentSlot;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.*;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.reborn.FeatherDisguise.metadata.types.AbstractMetadataHolder;
import org.reborn.FeatherDisguise.metadata.EntityType;
import org.reborn.FeatherDisguise.metadata.modal.LivingEntityMetadataHolder;
import org.reborn.FeatherDisguise.metadata.types.passive.ArmorStandMetadataHolder;
import org.reborn.FeatherDisguise.metadata.types.neutral.PlayerMetadataHolder;
import org.reborn.FeatherDisguise.types.AbstractDisguise;
import org.reborn.FeatherDisguise.util.DisguiseUtil;
import org.reborn.FeatherDisguise.util.PacketUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Log4j2
public class DisguiseRelatedEntityWrapper<E extends AbstractMetadataHolder<?>> {

    @NotNull private final AbstractDisguise<?> owningDisguise;

    @NotNull private final PlayerMetadataHolder cachedOwningDisguisePlayerMetadata;

    @Getter @NotNull private final VirtualDisguiseEntityData<E> baseDisguiseEntity;

    @Getter @NotNull private final VirtualDisguiseEntityData<LivingEntityMetadataHolder<EntityType<EntitySquid>>> hittableSquidEntity;
    @Getter @NotNull private final VirtualDisguiseEntityData<ArmorStandMetadataHolder> nametagArmorStandEntity;

    public DisguiseRelatedEntityWrapper(@NotNull final AbstractDisguise<?> owningDisguise, @NotNull final E baseDisguiseEntityMetadataHolder) {
        this.owningDisguise = owningDisguise;
        this.cachedOwningDisguisePlayerMetadata = new PlayerMetadataHolder();
        final World bukkitWorld = owningDisguise.getOwningBukkitPlayer().getWorld();
        this.baseDisguiseEntity = new VirtualDisguiseEntityData<>(baseDisguiseEntityMetadataHolder, bukkitWorld);

        this.hittableSquidEntity = new VirtualDisguiseEntityData<>(new LivingEntityMetadataHolder<>(EntityType.SQUID), bukkitWorld);
        this.nametagArmorStandEntity = new VirtualDisguiseEntityData<>(new ArmorStandMetadataHolder(), bukkitWorld);

        this.modifyRelatedEntitiesAndPrepareForSpawning();
    }

    @ApiStatus.Internal
    private void modifyRelatedEntitiesAndPrepareForSpawning() {

        // squid which is hittable, appears on the top position of all disguises
        this.hittableSquidEntity.getMetadataHolder().setInvisible(true);

        // armor-stand which displays the name-tag for the disguise owner
        this.nametagArmorStandEntity.getMetadataHolder().setInvisible(true);
        this.nametagArmorStandEntity.getMetadataHolder().setMarker(true);
        this.nametagArmorStandEntity.getMetadataHolder().setRemovedBaseplate(true);
        this.nametagArmorStandEntity.getMetadataHolder().setSmall(true);
        this.nametagArmorStandEntity.getMetadataHolder().setCustomName(this.owningDisguise.getDisguiseNametag());
        this.nametagArmorStandEntity.getMetadataHolder().setCustomNameVisible(true);
    }

    @NotNull public Optional<List<PacketWrapper<?>>> generateSpawningPacketsForAllDisguiseRelatedEntities() {
        final Location disguisedPlayerCurrentPosRot = this.owningDisguise.getOwningBukkitPlayer().getLocation().clone();
        final List<PacketWrapper<?>> extraPackets = this.owningDisguise.extraPacketsToProvideDuringEntitySpawning();
        final List<PacketWrapper<?>> spawningPackets = new ArrayList<>(extraPackets == null ? 7 : extraPackets.size() + 7);
        // base disguise spawn
        // base disguise head rotation
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

        // need to send an extra head rotation packet to make sure the head is correctly positioned when viewing clients see the disguise for the first time
        spawningPackets.add(new WrapperPlayServerEntityHeadLook(
                this.baseDisguiseEntity.getVirtualID(),
                this.owningDisguise.isHeadRotationYawLocked() ? 0f : disguisedPlayerCurrentPosRot.getYaw()));

        // send an equipment packet for the hand slot if the player is holding an item & the disguise is allowed to show items in the hand
        if (DisguiseUtil.isDisguiseAbleToRenderItemsInHandSlots(this.owningDisguise.getDisguiseType()) &&
                this.owningDisguise.getOwningBukkitPlayer().getEquipment() != null && this.owningDisguise.getOwningBukkitPlayer().getEquipment().getItemInHand().getType() != Material.AIR) {
            spawningPackets.add(new WrapperPlayServerEntityEquipment(this.baseDisguiseEntity.getVirtualID(),
                    Collections.singletonList(
                            new Equipment(EquipmentSlot.MAIN_HAND,
                                    SpigotConversionUtil.fromBukkitItemStack(this.owningDisguise.getOwningBukkitPlayer().getItemInHand())))));
        }

        if (extraPackets != null) {
            spawningPackets.addAll(extraPackets); // some entities might need to send something extra, we can @ Override and do so here
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

    @NotNull public Optional<List<PacketWrapper<?>>> generateSpawningPacketsForDisguisePlayerOwner() {
        final Location disguisedPlayerCurrentPosRot = this.owningDisguise.getOwningBukkitPlayer().getLocation().clone();
        final EntityPlayer nmsPlayer = ((CraftPlayer) this.owningDisguise.getOwningBukkitPlayer()).getHandle();
        final List<PacketWrapper<?>> spawningPackets = new ArrayList<>(1);

        // [!] make sure to synchronise the cached player metadata holder
        //     (this way when we spawn the player entity back, they will have the correct metadata clientside)
        cachedOwningDisguisePlayerMetadata.cheekySynchroniseNMSPlayerMetadataToHolder(nmsPlayer);

        // todo do we need player info?

        final Optional<List<EntityData>> optMetadata = cachedOwningDisguisePlayerMetadata.getConstructedListOfMetadata();
        if (!optMetadata.isPresent()) {
            return Optional.empty(); // exit early if we cannot construct metadata. we don't want to crash clients!
        }

        spawningPackets.add(new WrapperPlayServerSpawnPlayer(
                this.owningDisguise.getOwningBukkitPlayer().getEntityId(), this.owningDisguise.getOwningBukkitPlayer().getUniqueId(),
                SpigotConversionUtil.fromBukkitLocation(disguisedPlayerCurrentPosRot).getPosition(),
                disguisedPlayerCurrentPosRot.getYaw(), disguisedPlayerCurrentPosRot.getPitch(),
                optMetadata.get()));

        return Optional.of(spawningPackets);
    }

    @NotNull public WrapperPlayServerDestroyEntities generateDestroyPacketForDisguisePlayerOwner() {
        return new WrapperPlayServerDestroyEntities(this.owningDisguise.getOwningBukkitPlayer().getEntityId());
    }

    @NotNull public WrapperPlayServerEntityMetadata updateRelevantFieldsAndReturnMetadataPacketForBaseEntity() {
        final EntityPlayer nmsDisguiseOwner = ((CraftPlayer) owningDisguise.getOwningBukkitPlayer()).getHandle();

        // update relevant metadata fields for the base disguise entity
        baseDisguiseEntity.getMetadataHolder().setOnFire(nmsDisguiseOwner.isBurning());
        baseDisguiseEntity.getMetadataHolder().setSneaking(nmsDisguiseOwner.isSneaking());
        baseDisguiseEntity.getMetadataHolder().setSprinting(nmsDisguiseOwner.isSprinting());
        baseDisguiseEntity.getMetadataHolder().setPerformingAction((nmsDisguiseOwner.getDataWatcher().getByte(0) & 1 << AbstractMetadataHolder.EntityBitMaskType.IS_DOING_ACTION.getBitID()) != 0);
        baseDisguiseEntity.getMetadataHolder().setInvisible(nmsDisguiseOwner.isInvisible() || nmsDisguiseOwner.isSpectator());

        final Optional<List<EntityData>> optListConstructedMetadata = baseDisguiseEntity.getMetadataHolder().getConstructedListOfMetadata();
        if (!optListConstructedMetadata.isPresent()) {
            throw new DisguiseMetadataException("Failed to retrieve constructed list of metadata");
        }

        return new WrapperPlayServerEntityMetadata(baseDisguiseEntity.getVirtualID(), optListConstructedMetadata.get());
    }

    public void sendUpdateMetadataForBaseDisguiseEntityToAllViewingPlayers() {
        final Optional<List<EntityData>> optMetadata = this.baseDisguiseEntity.getMetadataHolder().getConstructedListOfMetadata();
        if (!optMetadata.isPresent()) {
            log.warn("Unable to construct metadata packet for base disguise entity ({}) for ({}). Aborting sending metadata packet",
                    this.owningDisguise.getDisguiseType(), this.owningDisguise.getOwningBukkitPlayer().getName());
            return; // abandon ship boys, we r fucked
        }

        final WrapperPlayServerEntityMetadata metadataPacket = new WrapperPlayServerEntityMetadata(
                this.baseDisguiseEntity.getVirtualID(), optMetadata.get());
        DisguiseUtil.getPlayersInWorldExcluding(this.owningDisguise.getOwningBukkitPlayer())
                .forEach(viewer -> PacketUtil.sendPacketEventsPacket(viewer, metadataPacket, true));
    }

}
