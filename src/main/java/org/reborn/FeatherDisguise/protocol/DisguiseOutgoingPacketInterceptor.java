package org.reborn.FeatherDisguise.protocol;

import com.github.retrooper.packetevents.event.SimplePacketListenerAbstract;
import com.github.retrooper.packetevents.event.simple.PacketPlaySendEvent;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.reborn.FeatherDisguise.distributors.DisguiseListenerDistributor;
import org.reborn.FeatherDisguise.types.AbstractDisguise;
import org.reborn.FeatherDisguise.util.DisguiseUtil;

import java.util.Optional;

@AllArgsConstructor @Log4j2
public class DisguiseOutgoingPacketInterceptor extends SimplePacketListenerAbstract {

    @NotNull private final DisguiseListenerDistributor disguiseListenerDistributor;

    // todo
    //  - the only lead i have on this bug so far is its got something to do with spawn/destroy packets & spectator mode (maybe the plyaerInfo packet too?)
    //    (this only relates to interceptor mode, tracker mode doesn't have these issues)


    /* Server -> Client
     * (server is sending packet(s) to player client(s) */
    @Override
    public void onPacketPlaySend(final PacketPlaySendEvent e) {
        if (e.isCancelled()) return;

        // if the packets getting sent are not the ones we are handling, exit early here
        if (!DisguiseUtil.isAllowedToHandleOutgoingPacketInterception(e, disguiseListenerDistributor.getFeatherDisguise().getDisguiseAPI().getPacketHandlingType())) return;

        // player(s) who will receive the packet
        // (assume that receivers will be everyone who in the NMS entity tracker can "see" the owningPlayer (one who is disguised))
        final Player observingPlayer = e.getPlayer();
        if (observingPlayer == null) return;

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

        final Optional<AbstractDisguise<?>> optDisguise = disguiseListenerDistributor.getFeatherDisguise().getDisguiseAPI().getDisguiseFromEntityID(entityID);
        if (!optDisguise.isPresent()) return;

        // we do not want to send packets to the observing player if it's also the disguised player.
        // that would absolutely fuck the disguised players client up. only VIEWING players should be getting these packets
        final AbstractDisguise<?> disguise = optDisguise.get();
        if (disguise.getOwningBukkitPlayer().getEntityId() == observingPlayer.getEntityId()) return;

        /*
         * --> ok at this point we can confirm a few things:
         * - the observer who is getting sent packets is a player and is not the disguise owner
         * - we have a valid entityID from the packet we were sending, and that entityID has a valid disguise loaded (which means they are a player)
         * - the disguise manager has confirmed the disguise is able to modify/send whatever packets we need to observers
         */

        disguiseListenerDistributor.handleOutgoingInterceptedPackets(e, packetBeingSent, disguise, observingPlayer);
    }
}
