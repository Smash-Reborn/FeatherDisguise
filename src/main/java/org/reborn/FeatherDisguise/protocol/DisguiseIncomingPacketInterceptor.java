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

@AllArgsConstructor @Log4j2
public class DisguiseIncomingPacketInterceptor extends SimplePacketListenerAbstract {

    @NotNull private final DisguiseListenerDistributor disguiseListenerDistributor;

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

        // todo get hittable data
        //  make sure is valid
        //  mark for reencode & modify entityID value to the owning player
    }
}
