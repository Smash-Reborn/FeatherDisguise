package org.reborn.FeatherDisguise.distributors.impl;

import com.github.retrooper.packetevents.event.simple.PacketPlaySendEvent;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityAnimation;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.reborn.FeatherDisguise.distributors.DisguiseListenerDistributor;
import org.reborn.FeatherDisguise.distributors.IDisguisePacketDistributor;
import org.reborn.FeatherDisguise.types.AbstractDisguise;
import org.reborn.FeatherDisguise.util.DisguiseUtil;

/** Handles client-bound arm swing/hurt animations for disguises.
 * Relative NMS packet is {@link net.minecraft.server.v1_8_R3.PacketPlayOutAnimation}.
 * **/
public class DisguisePacketAnimationDistributor implements IDisguisePacketDistributor {

    @Override
    public void handlePacketInterception(@NotNull PacketPlaySendEvent packetSendEvent, @NotNull PacketWrapper<?> interceptedPacket,
                                         @NotNull AbstractDisguise<?> disguise, @NotNull Player observer, @NotNull DisguiseListenerDistributor disguiseListenerDistributor) {

        if (!(interceptedPacket instanceof WrapperPlayServerEntityAnimation)) return;
        final WrapperPlayServerEntityAnimation animationPacket = (WrapperPlayServerEntityAnimation) interceptedPacket;

        // if the disguise is flagged as "hidden" for the observing player, why would we bother sending them packets, just early exit
        if (disguise.isDisguiseAndRelatedEntitiesHiddenForViewer(observer)) {
            packetSendEvent.setCancelled(true);
            return;
        }

        if (!allowedAnimationTypes(animationPacket.getType())) return; // only handle ARM_SWING or HURT_ANIMATION

        if (animationPacket.getType() == WrapperPlayServerEntityAnimation.EntityAnimationType.SWING_MAIN_ARM) {
            if (!DisguiseUtil.isDisguiseAbleToRenderHandArmSwings(disguise.getDisguiseType())) {
                packetSendEvent.setCancelled(true); // they have a disguise, but it can't render arm swings
                return; // get cucked
            }
        }

        // make the base disguise entity (the thing player see) swing its arm
        animationPacket.setEntityId(disguise.getRelatedEntitiesWrapper().getBaseDisguiseEntity().getVirtualID());
        packetSendEvent.markForReEncode(true);
    }

    @ApiStatus.Internal
    private boolean allowedAnimationTypes(WrapperPlayServerEntityAnimation.EntityAnimationType animationType) {
        return animationType == WrapperPlayServerEntityAnimation.EntityAnimationType.SWING_MAIN_ARM || animationType == WrapperPlayServerEntityAnimation.EntityAnimationType.HURT;
    }
}
