package org.reborn.FeatherDisguise.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/*
 * im too lazy to manage using one type of packet, esp given the fact im not
 * migrating our metadata system and instead using nms for it. (since packet events doesn't provide a
 * convenient converting system for metadata)
 * so this is my hacky ass workaround. we will define what packet type it is and when sending
 * packets, handle accordingly to this bridger class
 */
@AllArgsConstructor @Getter
public class PacketRef<T> {

    private boolean isPacketEventsPacket;

    @NotNull private final T packetObject;
}
