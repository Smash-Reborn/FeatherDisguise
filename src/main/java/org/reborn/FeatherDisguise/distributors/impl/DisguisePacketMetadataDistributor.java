package org.reborn.FeatherDisguise.distributors.impl;

import com.github.retrooper.packetevents.event.simple.PacketPlaySendEvent;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.reborn.FeatherDisguise.distributors.DisguiseListenerDistributor;
import org.reborn.FeatherDisguise.distributors.IDisguisePacketDistributor;
import org.reborn.FeatherDisguise.metadata.types.AbstractMetadataHolder;
import org.reborn.FeatherDisguise.metadata.EntityMetadataIndexes;
import org.reborn.FeatherDisguise.types.AbstractDisguise;

import java.util.List;
import java.util.Optional;

/** Handles client-bound metadata updates for disguises.
 * Relative NMS packet is {@link net.minecraft.server.v1_8_R3.PacketPlayOutEntityMetadata}.
 * **/
public class DisguisePacketMetadataDistributor implements IDisguisePacketDistributor {

    @Override
    public void handlePacketInterception(@NotNull PacketPlaySendEvent packetSendEvent, @NotNull PacketWrapper<?> interceptedPacket,
                                         @NotNull AbstractDisguise<?> disguise, @NotNull Player observer, @NotNull DisguiseListenerDistributor disguiseListenerDistributor) {

        if (!(interceptedPacket instanceof WrapperPlayServerEntityMetadata)) return;
        final WrapperPlayServerEntityMetadata metadataPacket = (WrapperPlayServerEntityMetadata) interceptedPacket;

        // if the disguise is flagged as "hidden" for the observing player, why would we bother sending them packets, just early exit
        if (disguise.isDisguiseAndRelatedEntitiesHiddenForViewer(observer)) {
            packetSendEvent.setCancelled(true);
            return;
        }

        final EntityPlayer nmsDisguiseOwner = ((CraftPlayer) disguise.getOwningBukkitPlayer()).getHandle();

        // update relevant metadata fields for the base disguise entity
        disguise.getRelatedEntitiesWrapper().getBaseDisguiseEntity().getMetadataHolder().setOnFire(nmsDisguiseOwner.isBurning());
        disguise.getRelatedEntitiesWrapper().getBaseDisguiseEntity().getMetadataHolder().setSneaking(nmsDisguiseOwner.isSneaking());
        disguise.getRelatedEntitiesWrapper().getBaseDisguiseEntity().getMetadataHolder().setSprinting(nmsDisguiseOwner.isSprinting());
        disguise.getRelatedEntitiesWrapper().getBaseDisguiseEntity().getMetadataHolder().setPerformingAction((nmsDisguiseOwner.getDataWatcher().getByte(0) & 1 << AbstractMetadataHolder.EntityBitMaskType.IS_DOING_ACTION.getBitID()) != 0);
        disguise.getRelatedEntitiesWrapper().getBaseDisguiseEntity().getMetadataHolder().setInvisible(nmsDisguiseOwner.isInvisible());

        // todo (pls improve) can't port over the engines way of caching packets that would be too complex for this
        //  so for now we will just have to live with the more inefficient initializing lists and resending metadata packets
        final Optional<List<EntityData>> optMarkedMetadata = disguise.getRelatedEntitiesWrapper().getBaseDisguiseEntity().getMetadataHolder().getConstructedListOfMetadata(EntityMetadataIndexes.ENTITY_GENERIC);
        if (!optMarkedMetadata.isPresent()) return; // uihhhhhhhh, excuse me wtf are you actually even catching here????

        metadataPacket.setEntityId(disguise.getRelatedEntitiesWrapper().getBaseDisguiseEntity().getVirtualID());
        metadataPacket.setEntityMetadata(optMarkedMetadata.get());
        packetSendEvent.markForReEncode(true);
    }
}
