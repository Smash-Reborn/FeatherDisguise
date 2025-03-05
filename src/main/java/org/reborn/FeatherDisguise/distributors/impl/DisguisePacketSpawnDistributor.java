package org.reborn.FeatherDisguise.distributors.impl;

import com.github.retrooper.packetevents.event.simple.PacketPlaySendEvent;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.reborn.FeatherDisguise.distributors.DisguiseListenerDistributor;
import org.reborn.FeatherDisguise.distributors.IDisguisePacketDistributor;
import org.reborn.FeatherDisguise.types.AbstractDisguise;

/** Handles client-bound player spawn packets for disguises.
 * Relative NMS packet is {@link net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn}.
 * **/
public class DisguisePacketSpawnDistributor implements IDisguisePacketDistributor {

    @Override
    public void handlePacketInterception(@NotNull PacketPlaySendEvent packetSendEvent, @NotNull PacketWrapper<?> interceptedPacket,
                                         @NotNull AbstractDisguise<?> disguise, @NotNull Player observer, @NotNull DisguiseListenerDistributor disguiseListenerDistributor) {

        if (!(interceptedPacket instanceof WrapperPlayServerSpawnPlayer)) return;

        /*
         * this works a little differently to our engine version. because in 1.8 there are a bunch of different
         * spawning packet types for different entities, we instead need to handle specifically the one that spawns players.
         * other than that, we can just call our api method to show the disguise accordingly.
         */

        packetSendEvent.setCancelled(true);
        packetSendEvent.markForReEncode(false);

        disguiseListenerDistributor.getFeatherDisguise().getDisguiseAPI().showDisguiseForPlayer(disguise, observer);
    }
}
