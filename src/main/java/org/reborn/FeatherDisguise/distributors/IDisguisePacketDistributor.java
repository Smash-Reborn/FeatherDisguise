package org.reborn.FeatherDisguise.distributors;

import com.github.retrooper.packetevents.event.simple.PacketPlaySendEvent;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.reborn.FeatherDisguise.types.AbstractDisguise;

public interface IDisguisePacketDistributor {

    void handlePacketInterception(@NotNull final PacketPlaySendEvent packetSendEvent, @NotNull final PacketWrapper<?> interceptedPacket,
                                  @NotNull final AbstractDisguise<?> disguise, @NotNull final Player observer);
}
