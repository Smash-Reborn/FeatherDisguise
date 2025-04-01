package org.reborn.FeatherDisguise.util;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Log4j2
@NoArgsConstructor(access = AccessLevel.NONE)
public class PacketUtil {

    /** Attempts to send the {@link Packet} declared within the constructor to the {@link Player}.
     * <p>The method will fail if the {@code precondition} {@link Player#isValid()} returns {@code false}.</p>
     * **/
    public static void sendNMSPacket(@NotNull final Player player, @NotNull final Packet<?> packet) {
        if (!player.isValid() || !player.isOnline()) return;

        try {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        } catch (Throwable th) {
            log.warn("Failed to send NMS packet to player ({}).", player.getName(), th);
        }
    }

    /** Attempts to send all {@link Packet} objects within the {@code list} to the {@link Player}.
     * <p>See {@link #sendNMSPacket(Player, Packet)} for failure condition documentation.</p>
     * **/
    public static void sendNMSPackets(@NotNull final Player player, @NotNull final List<Packet<?>> packets) {
        if (packets.isEmpty()) return;
        packets.forEach(packet -> sendNMSPacket(player, packet));
    }

    /** Attempts to send the {@link PacketWrapper} declared within the constructor to the {@link Player}.
     * <p>The method will fail if the {@code precondition} {@link Player#isValid()} returns {@code false}.</p>
     *
     * @param sendSilently determines whether the packet will be 'listenable' by {@code PacketEvents} interceptors.
     * **/
    public static void sendPacketEventsPacket(@NotNull final Player player, @NotNull final PacketWrapper<?> packet, final boolean sendSilently) {
        if (!player.isValid() || !player.isOnline()) return;

        if (!sendSilently) {
            PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);
        }

        else PacketEvents.getAPI().getPlayerManager().sendPacketSilently(player, packet);
    }

    /** Attempts to send all {@link PacketWrapper} objects within the {@code list} to the {@link Player}.
     * <p>See {@link #sendPacketEventsPacket(Player, PacketWrapper, boolean)} for further documentation.</p>
     * **/
    public static void sendPacketEventsPackets(@NotNull final Player player, @NotNull final List<PacketWrapper<?>> packets, final boolean sendSilently) {
        if (packets.isEmpty()) return; // "fuck this shit im out!"
        packets.forEach(packet -> sendPacketEventsPacket(player, packet, sendSilently));
    }
}
