package org.reborn.FeatherDisguise.distributors.impl;

import com.github.retrooper.packetevents.event.simple.PacketPlaySendEvent;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityAnimation;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.reborn.FeatherDisguise.distributors.DisguiseListenerDistributor;
import org.reborn.FeatherDisguise.distributors.IDisguisePacketDistributor;
import org.reborn.FeatherDisguise.types.AbstractDisguise;
import org.reborn.FeatherDisguise.util.DisguiseUtil;

/** Handles client-bound arm swing animations for disguises.
 * Relative NMS packet is {@link net.minecraft.server.v1_8_R3.PacketPlayOutAnimation}.
 * **/
public class DisguisePacketAnimationDistributor implements IDisguisePacketDistributor {

    @Override
    public void handlePacketInterception(@NotNull PacketPlaySendEvent packetSendEvent, @NotNull PacketWrapper<?> interceptedPacket,
                                         @NotNull AbstractDisguise<?> disguise, @NotNull Player observer, @NotNull DisguiseListenerDistributor disguiseListenerDistributor) {

        if (!(interceptedPacket instanceof WrapperPlayServerEntityAnimation)) return;
        final WrapperPlayServerEntityAnimation animationPacket = (WrapperPlayServerEntityAnimation) interceptedPacket;

        if (!DisguiseUtil.isDisguiseAbleToRenderHandArmSwings(disguise.getDisguiseType()) || disguise.getViewingPlayerIDsMarkedAsHidden().contains(observer.getEntityId())) {
            packetSendEvent.setCancelled(true); // they have a disguise, but it can't render arm swings OR they are marked as hidden for the observer
            return;
        }

        if (animationPacket.getType() != WrapperPlayServerEntityAnimation.EntityAnimationType.SWING_MAIN_ARM) return; // does this really matter for 1.8 or packetevents? idk

        // make the base disguise entity (the thing player see) swing its arm
        animationPacket.setEntityId(disguise.getRelatedEntitiesWrapper().getBaseDisguiseEntity().getVirtualID());
        packetSendEvent.markForReEncode(true);
    }
}
