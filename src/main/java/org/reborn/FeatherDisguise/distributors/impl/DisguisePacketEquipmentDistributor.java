package org.reborn.FeatherDisguise.distributors.impl;

import com.github.retrooper.packetevents.event.simple.PacketPlaySendEvent;
import com.github.retrooper.packetevents.protocol.player.Equipment;
import com.github.retrooper.packetevents.protocol.player.EquipmentSlot;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityEquipment;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.reborn.FeatherDisguise.distributors.DisguiseListenerDistributor;
import org.reborn.FeatherDisguise.distributors.IDisguisePacketDistributor;
import org.reborn.FeatherDisguise.types.AbstractDisguise;
import org.reborn.FeatherDisguise.util.DisguiseUtil;

import java.util.Collections;

/** Handles client-bound equipment item changes for disguises. In our use-case, we only really
 * care about the main-hand item being correctly updated on disguise that can render that.
 * Relative NMS packet is {@link net.minecraft.server.v1_8_R3.PacketPlayOutEntityEquipment}.
 * **/
public class DisguisePacketEquipmentDistributor implements IDisguisePacketDistributor {

    @Override
    public void handlePacketInterception(@NotNull PacketPlaySendEvent packetSendEvent, @NotNull PacketWrapper<?> interceptedPacket,
                                         @NotNull AbstractDisguise<?> disguise, @NotNull Player observer, @NotNull DisguiseListenerDistributor disguiseListenerDistributor) {

        if (!(interceptedPacket instanceof WrapperPlayServerEntityEquipment)) return;
        final WrapperPlayServerEntityEquipment equipmentPacket = (WrapperPlayServerEntityEquipment) interceptedPacket;

        // are they even able to display items in their hand slot? don't bother modifying the packet if they can't
        if (!DisguiseUtil.isDisguiseAbleToRenderItemsInHandSlots(disguise.getDisguiseType()) || disguise.getViewingPlayerIDsMarkedAsHidden().contains(observer.getEntityId())) {
            packetSendEvent.setCancelled(true); // they have a disguise, but it can't display items, just outright cancel the packet, so it won't send for the wrong entity
            return;
        }

        if (disguise.getOwningBukkitPlayer().getEquipment() == null) return;
        final ItemStack itemInMainHand = disguise.getOwningBukkitPlayer().getEquipment().getItemInHand();

        // the equipment packet works serverside by running some checking method (detectEquipmentUpdates()) every update. this checks to see
        // if any of the equipment in the players slots has been modified and if it has, broadcasts a ClientboundSetEquipmentPacket() to all viewing clients.
        // the problem with this is, i don't think it's possible to keep track externally of what equipment changes have been sent to what player, given
        // the fact entities render on the client depending on render distance. for example, if a client has x disguise deloaded because of distance and we send an
        // equipment packet, there have been cases in the past where the hand item doesn't correctly update. so it's impossible for us to correctly at all times determine
        // who can see what held item. furthermore, i don't think there is a way for us to determine via the packet which slots have/haven't been updated (?)

        // (copied from engine code, my own research. unsure if it applies to 1.8 but ill keep it here anyways)

        // we only want to modify the outgoing packet if the main hand is being modified.
        // we don't care about other slots currently so just ignore otherwise
        // (yes this is cringe but i don't think there's any other faster workaround)
        boolean foundMainHand = false;
        for (final Equipment equipment : equipmentPacket.getEquipment()) {
            if (equipment.getSlot() == EquipmentSlot.MAIN_HAND) {
                foundMainHand = true;
                break;
            }
        }

        if (!foundMainHand) return;

        // we are going to modify the outgoing packet
        // (set the equipment in the packet to only modify what's in the main hand & change the entityID)
        equipmentPacket.setEntityId(disguise.getRelatedEntitiesWrapper().getBaseDisguiseEntity().getVirtualID());
        equipmentPacket.setEquipment(Collections.singletonList(new Equipment(EquipmentSlot.MAIN_HAND, SpigotConversionUtil.fromBukkitItemStack(itemInMainHand))));
        packetSendEvent.markForReEncode(true);
    }
}
