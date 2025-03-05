package org.reborn.FeatherDisguise.distributors.impl;

import com.github.retrooper.packetevents.event.simple.PacketPlaySendEvent;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityTeleport;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.reborn.FeatherDisguise.distributors.DisguiseListenerDistributor;
import org.reborn.FeatherDisguise.distributors.IDisguisePacketDistributor;
import org.reborn.FeatherDisguise.types.AbstractDisguise;
import org.reborn.FeatherDisguise.util.PacketUtil;

/** Handles client-bound entity teleport/position synchronisation updates for disguises.
 * Relative NMS packet is {@link net.minecraft.server.v1_8_R3.PacketPlayOutEntityTeleport}.
 * **/
public class DisguisePacketTeleportDistributor implements IDisguisePacketDistributor {

    @Override
    public void handlePacketInterception(@NotNull PacketPlaySendEvent packetSendEvent, @NotNull PacketWrapper<?> interceptedPacket,
                                         @NotNull AbstractDisguise<?> disguise, @NotNull Player observer, @NotNull DisguiseListenerDistributor disguiseListenerDistributor) {

        if (!(interceptedPacket instanceof WrapperPlayServerEntityTeleport)) return;
        final WrapperPlayServerEntityTeleport teleportPacket = (WrapperPlayServerEntityTeleport) interceptedPacket;

        // if the disguise is flagged as "hidden" for the observing player, why would we bother sending them packets, just early exit
        if (disguise.getViewingPlayerIDsMarkedAsHidden().contains(observer.getEntityId())) {
            packetSendEvent.setCancelled(true);
            return;
        }

        teleportPacket.setEntityId(disguise.getRelatedEntitiesWrapper().getBaseDisguiseEntity().getVirtualID());
        if (disguise.isHeadRotationYawLocked()) {teleportPacket.getValues().setYaw(0f);}
        if (disguise.isHeadRotationPitchLocked()) {teleportPacket.getValues().setPitch(0f);}
        packetSendEvent.markForReEncode(true);

        // todo (pls improve) can't port over the engines way of caching packets that would be too complex for this
        //  so for now we will just have to live with the more inefficient recalculating & instantiating extra packets

        final WrapperPlayServerEntityTeleport squidRelatedTeleportPacket = new WrapperPlayServerEntityTeleport(
                disguise.getRelatedEntitiesWrapper().getHittableSquidEntity().getVirtualID(),
                teleportPacket.getPosition().add(0, disguise.getCalculatedSquidRelatedEntityYPos(teleportPacket.getValues().getPosition().getY()), 0),
                0f, 0f, false);
        // todo is setting the delta to .zero() gonna fuck shit up?

        PacketUtil.sendPacketEventsPacket(observer, squidRelatedTeleportPacket, true);
    }
}
