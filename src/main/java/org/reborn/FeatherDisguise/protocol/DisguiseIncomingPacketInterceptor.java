package org.reborn.FeatherDisguise.protocol;

import com.github.retrooper.packetevents.event.SimplePacketListenerAbstract;
import com.github.retrooper.packetevents.event.simple.PacketPlayReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.reborn.FeatherDisguise.distributors.DisguiseListenerDistributor;
import org.reborn.FeatherDisguise.types.AbstractDisguise;

import java.util.Optional;

@AllArgsConstructor @Log4j2
public class DisguiseIncomingPacketInterceptor extends SimplePacketListenerAbstract {

    @NotNull private final DisguiseListenerDistributor disguiseListenerDistributor;

    /* Client -> Server
     * (client is sending packet(s) to the server */
    @Override
    public void onPacketPlayReceive(final PacketPlayReceiveEvent e) {
        if (e.isCancelled()) return;

        // we only want to listen for INTERACT_ENTITY packets
        // (this is the packet player clients send for melee attacks, right-clicks, etc)
        if (e.getPacketType() != PacketType.Play.Client.INTERACT_ENTITY) return;

        final WrapperPlayClientInteractEntity interactEntityPacket = new WrapperPlayClientInteractEntity(e);
        if (interactEntityPacket.getAction() != WrapperPlayClientInteractEntity.InteractAction.ATTACK) return;
        // we only care about intercepting and handling melee attacks in these situations

        final Player playerWhoSentPacket = e.getPlayer();
        if (playerWhoSentPacket == null) return;

        final int entityID = interactEntityPacket.getEntityId();

        final Optional<AbstractDisguise<?>> optDisguiseFromHittableID = disguiseListenerDistributor.getFeatherDisguise().getDisguiseAPI().getDisguiseFromRelatedHittableEntityID(entityID);
        if (!optDisguiseFromHittableID.isPresent()) return;

        final AbstractDisguise<?> activeDisguise = optDisguiseFromHittableID.get();
        if (playerWhoSentPacket.getEntityId() == activeDisguise.getRelatedEntitiesWrapper().getBaseDisguiseEntity().getVirtualID()) return;

        /*
         * ---> at this point we can confirm a few things
         * - the packet is the INTERACT_ENTITY packet & it's the ATTACK action
         * - we have a valid disguise related entity that was being attacked & it has disguise data we can reference
         * - the player sending the attacking packet ISN'T attacking themselves or their disguise
         */

        // re-encode the packet -> modify the entityID to the owning player so the network manager can handle it like its attacked the player
        interactEntityPacket.setEntityId(activeDisguise.getOwningBukkitPlayer().getEntityId());
        e.markForReEncode(true);
    }
}
