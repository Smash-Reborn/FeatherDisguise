package org.reborn.FeatherDisguise.distributors.impl;

import com.github.retrooper.packetevents.event.simple.PacketPlaySendEvent;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDestroyEntities;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.reborn.FeatherDisguise.distributors.DisguiseListenerDistributor;
import org.reborn.FeatherDisguise.distributors.IDisguisePacketDistributor;
import org.reborn.FeatherDisguise.types.AbstractDisguise;

/** Handles client-bound destroy packets for disguises.
 * Relative NMS packet is {@link net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy}.
 * **/
public class DisguisePacketDestroyDistributor implements IDisguisePacketDistributor {

    @Override
    public void handlePacketInterception(@NotNull PacketPlaySendEvent packetSendEvent, @NotNull PacketWrapper<?> interceptedPacket,
                                         @NotNull AbstractDisguise<?> disguise, @NotNull Player observer, @NotNull DisguiseListenerDistributor disguiseListenerDistributor) {

        if (!(interceptedPacket instanceof WrapperPlayServerDestroyEntities)) return;
        final WrapperPlayServerDestroyEntities destroyPacket = (WrapperPlayServerDestroyEntities) interceptedPacket;

        // at this point, we can assume that the server is trying to send destroy packets for the disguise owning PLAYER.
        // (this is most likely because the tracker calculated they left some sort of render distance or chunk tracked section)
        // our player is disguised. situations like this are a bit annoying because it's difficult to determine why exactly a destroy
        // packet might have been sent. in most cases however, we can just handle it by cancelling the player destroy packet, then
        // just calling our method to hide the disguise for the specific player.

        packetSendEvent.setCancelled(true);
        packetSendEvent.markForReEncode(false);

        disguiseListenerDistributor.getFeatherDisguise().getDisguiseAPI().hideDisguiseForPlayer(disguise, observer, false);
        // DON'T spawn the player entity back in when hiding the disguise, because remember we assume in these cases the
        // servers chunk tracker is probably trying to temporarily de-load the entity for the tracked player. this might fuck up other things but idk fuck this shit
    }
}
