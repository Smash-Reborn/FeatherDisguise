package org.reborn.FeatherDisguise.distributors.impl;

import com.github.retrooper.packetevents.event.simple.PacketPlaySendEvent;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityRelativeMove;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityRelativeMoveAndRotation;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityRotation;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.reborn.FeatherDisguise.distributors.DisguiseListenerDistributor;
import org.reborn.FeatherDisguise.distributors.IDisguisePacketDistributor;
import org.reborn.FeatherDisguise.types.AbstractDisguise;
import org.reborn.FeatherDisguise.util.PacketUtil;

/** Handles client-bound entity relative positions & rotations updates for disguises.
 * Relative NMS packets are
 * {@link net.minecraft.server.v1_8_R3.PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook},
 * {@link net.minecraft.server.v1_8_R3.PacketPlayOutEntity.PacketPlayOutRelEntityMove} &
 * {@link net.minecraft.server.v1_8_R3.PacketPlayOutEntity.PacketPlayOutEntityLook}.
 * **/
public class DisguisePacketPosRotDistributor implements IDisguisePacketDistributor {

    @Override
    public void handlePacketInterception(@NotNull PacketPlaySendEvent packetSendEvent, @NotNull PacketWrapper<?> interceptedPacket,
                                         @NotNull AbstractDisguise<?> disguise, @NotNull Player observer, @NotNull DisguiseListenerDistributor disguiseListenerDistributor) {

        // if the disguise is flagged as "hidden" for the observing player, why would we bother sending them packets, just early exit
        if (disguise.getViewingPlayerIDsMarkedAsHidden().contains(observer.getEntityId())) {
            packetSendEvent.setCancelled(true);
            return;
        }

        // have to use older switch cases since java 1.shitter doesn't have enhanced o_O
        switch (packetSendEvent.getPacketType()) {
            default:
                break; // vai r u gae, who sais i em gae, u r g ae

            case ENTITY_RELATIVE_MOVE_AND_ROTATION:
                if (!(interceptedPacket instanceof WrapperPlayServerEntityRelativeMoveAndRotation)) return;
                final WrapperPlayServerEntityRelativeMoveAndRotation relPosRotPacket = (WrapperPlayServerEntityRelativeMoveAndRotation) interceptedPacket;

                // modify the original outgoing packet, make it handle the base disguise entity
                relPosRotPacket.setEntityId(disguise.getRelatedEntitiesWrapper().getBaseDisguiseEntity().getVirtualID());
                if (disguise.isHeadRotationYawLocked()) {relPosRotPacket.setYaw(0f);}
                if (disguise.isHeadRotationPitchLocked()) {relPosRotPacket.setPitch(0f);}
                packetSendEvent.markForReEncode(true);

                final WrapperPlayServerEntityRelativeMoveAndRotation squidRelPosRotPacket = new WrapperPlayServerEntityRelativeMoveAndRotation(
                        disguise.getRelatedEntitiesWrapper().getHittableSquidEntity().getVirtualID(),
                        relPosRotPacket.getDeltaX(), relPosRotPacket.getDeltaY(), relPosRotPacket.getDeltaZ(),
                        0f, 0f, false);

                PacketUtil.sendPacketEventsPacket(observer, squidRelPosRotPacket, true);
                break;

            case ENTITY_RELATIVE_MOVE:
                if (!(interceptedPacket instanceof WrapperPlayServerEntityRelativeMove)) return;
                final WrapperPlayServerEntityRelativeMove relPosPacket = (WrapperPlayServerEntityRelativeMove) interceptedPacket;

                // modify the original outgoing packet, make it handle the base disguise entity
                relPosPacket.setEntityId(disguise.getRelatedEntitiesWrapper().getBaseDisguiseEntity().getVirtualID());
                packetSendEvent.markForReEncode(true);

                final WrapperPlayServerEntityRelativeMove squidRelatedEntityPacket = new WrapperPlayServerEntityRelativeMove(
                        disguise.getRelatedEntitiesWrapper().getHittableSquidEntity().getVirtualID(),
                        relPosPacket.getDeltaX(), relPosPacket.getDeltaY(), relPosPacket.getDeltaZ(), false);

                PacketUtil.sendPacketEventsPacket(observer, squidRelatedEntityPacket, true);
                break;

            case ENTITY_ROTATION:
                if (!(interceptedPacket instanceof WrapperPlayServerEntityRotation)) return;
                final WrapperPlayServerEntityRotation relRotPacket = (WrapperPlayServerEntityRotation) interceptedPacket;

                relRotPacket.setEntityId(disguise.getRelatedEntitiesWrapper().getBaseDisguiseEntity().getVirtualID());
                if (disguise.isHeadRotationYawLocked()) {relRotPacket.setYaw(0f);}
                if (disguise.isHeadRotationPitchLocked()) {relRotPacket.setPitch(0f);}
                packetSendEvent.markForReEncode(true);
                // for rotation packets we only need to handle for the base disguise entity
                // (related disguise entities do not care about handling rotations)
                break;
        }
    }
}
