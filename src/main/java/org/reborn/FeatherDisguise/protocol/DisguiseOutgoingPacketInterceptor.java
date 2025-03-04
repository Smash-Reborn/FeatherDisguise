package org.reborn.FeatherDisguise.protocol;

import com.github.retrooper.packetevents.event.SimplePacketListenerAbstract;
import com.github.retrooper.packetevents.event.simple.PacketPlaySendEvent;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.reborn.FeatherDisguise.distributors.DisguiseListenerDistributor;
import org.reborn.FeatherDisguise.util.DisguiseUtil;

@AllArgsConstructor @Log4j2
public class DisguiseOutgoingPacketInterceptor extends SimplePacketListenerAbstract {

    @NotNull private final DisguiseListenerDistributor disguiseListenerDistributor;

    /* Server -> Client
     * (server is sending packet(s) to player client(s) */
    @Override
    public void onPacketPlaySend(final PacketPlaySendEvent e) {
        if (e.isCancelled()) return;

        // if the packets getting sent are not the ones we are handling, exit early here
        if (!DisguiseUtil.isAllowedToHandleOutgoingPacketInterception(e)) return;

        // player(s) who will receive the packet
        // (assume that receivers will be everyone who in the NMS entity tracker can "see" the owningPlayer (one who is disguised))
        final Player obvseringPlayer = e.getPlayer();
        if (obvseringPlayer == null) return;

        // we need to obtain a packet wrapper for the packets being sent.
        // if we return null, that means it's not one of the packets we care about, so we can exit early
        final PacketWrapper<?> packetBeingSent = DisguiseUtil.constructPacketWrapperFromEvent(e);
        if (packetBeingSent == null) return;

        // next we need to obtain the entityID from the packet wrapper we just created.
        // if the entityID matches our invalidID, assume we couldn't find an entityID, so we can exit early cos nothing else will work if we continue
        final int entityID = DisguiseUtil.getEntityIDFromPacketEventWrapper(e.getPacketType(), packetBeingSent);
        if (entityID == DisguiseUtil.INVALID_DISGUISE_ENTITY_ID) {
            log.warn("Disguise entityID is invalid or null. Unable to handle packet interception for ({})", packetBeingSent);
            return;
        }

        // todo retrieve disguise
        //  check if valid
        //  make sure not the observer

        //disguiseListenerDistributor.handleOutgoingInterceptedPackets(e, packetBeingSent, disguise, obvseringPlayer);
    }
}
