package org.reborn.FeatherDisguise.distributors.impl;

import com.github.retrooper.packetevents.event.simple.PacketPlaySendEvent;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerAttachEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.reborn.FeatherDisguise.distributors.DisguiseListenerDistributor;
import org.reborn.FeatherDisguise.distributors.IDisguisePacketDistributor;
import org.reborn.FeatherDisguise.types.AbstractDisguise;

public class DisguisePacketAttachDistributor implements IDisguisePacketDistributor {

    @Override
    public void handlePacketInterception(@NotNull PacketPlaySendEvent packetSendEvent, @NotNull PacketWrapper<?> interceptedPacket,
                                         @NotNull AbstractDisguise<?> disguise, @NotNull Player observer, @NotNull DisguiseListenerDistributor disguiseListenerDistributor) {

        if (!(interceptedPacket instanceof WrapperPlayServerAttachEntity)) return;
        final WrapperPlayServerAttachEntity attachPacket = (WrapperPlayServerAttachEntity) interceptedPacket;

        // if the disguise is flagged as "hidden" for the observing player, why would we bother sending them packets, just early exit
        if (disguise.isDisguiseAndRelatedEntitiesHiddenForViewer(observer)) {
            packetSendEvent.setCancelled(true);
            return;
        }

        // whatever is attached (riding/leashed) is now attached to our disguise!
        attachPacket.setHoldingId(disguise.getRelatedEntitiesWrapper().getBaseDisguiseEntity().getVirtualID());
        packetSendEvent.markForReEncode(true);
    }
}
