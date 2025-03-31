package org.reborn.FeatherDisguise.protocol;

import com.github.retrooper.packetevents.event.SimplePacketListenerAbstract;
import com.github.retrooper.packetevents.event.simple.PacketPlaySendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfo;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.reborn.FeatherDisguise.distributors.DisguiseListenerDistributor;
import org.reborn.FeatherDisguise.types.AbstractDisguise;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor @Log4j2
public class DisguiseOutgoingPlayerInfoPacketInterceptor extends SimplePacketListenerAbstract {

    @NotNull private final DisguiseListenerDistributor disguiseListenerDistributor;

    @Override
    public void onPacketPlaySend(final PacketPlaySendEvent e) {
        if (e.isCancelled()) return;

        if (e.getPacketType() != PacketType.Play.Server.PLAYER_INFO) return;

        final Player observingPlayer = e.getPlayer();
        if (observingPlayer == null) return;

        final WrapperPlayServerPlayerInfo playerInfoPacket = new WrapperPlayServerPlayerInfo(e);

        //log.info("PlayerInfoPacket (({})) ---> observer: ({})", playerInfoPacket.getAction(), observingPlayer.getName());
        //playerInfoPacket.getPlayerDataList().forEach(data ->
        //        log.info("updateGamemodePacket data ---> observer: ({}) ---> ({}), ({})", observingPlayer.getName(), data.getUserProfile().getName(), data.getUserProfile().getUUID()));

        if (playerInfoPacket.getAction() != WrapperPlayServerPlayerInfo.Action.UPDATE_GAME_MODE) return;
        //log.info("updateGamemodePacket for ({})", playerInfoPacket.getPlayerDataList());

        final List<WrapperPlayServerPlayerInfo.PlayerData> playerDataList = playerInfoPacket.getPlayerDataList();

        if (playerDataList.isEmpty()) return; // unlikely lol

        final Optional<AbstractDisguise<?>> optDisguise = disguiseListenerDistributor.getFeatherDisguise().getDisguiseAPI().getDisguiseFromEntityID(observingPlayer.getEntityId());
        if (!optDisguise.isPresent()) return;

        //log.info("markedFalse & setCncld for ({})", observingPlayer.getName());

//        boolean shouldCancel = false;
//        for (final WrapperPlayServerPlayerInfo.PlayerData playerData : playerDataList) {
//            final int playerEntityID = Bukkit.getPlayer(playerData.getUserProfile().getUUID()).getEntityId();
//            final Optional<AbstractDisguise<?>> optDisguise = disguiseListenerDistributor.getFeatherDisguise().getDisguiseAPI().getDisguiseFromEntityID(playerEntityID);
//
//            if (!optDisguise.isPresent()) continue;
//
//            final AbstractDisguise<?> disguise = optDisguise.get();
//            if (disguise.getOwningBukkitPlayer().getEntityId() != playerEntityID) continue;
//
//            shouldCancel = true;
//        }
//
//        if (!shouldCancel) return;

        //e.markForReEncode(false);
        //e.setCancelled(true);
    }
}
