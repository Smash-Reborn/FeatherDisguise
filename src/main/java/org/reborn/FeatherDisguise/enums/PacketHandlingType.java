package org.reborn.FeatherDisguise.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor @Getter
public enum PacketHandlingType {

    /** Makes {@code FeatherDisguises} fully utilise {@code PacketEvents} interception listeners
     * when attempting to handle outgoing & incoming packets. This means packets are modified or
     * created at the end of the sending process, after the servers done all the technical math work.
     * @apiNote
     * Because Java 8 hasn't completely removed the performance hit of most "slow" operations (mainly reflection),
     * using {@code PacketEvents} for only its interceptor system is generally suboptimal compared to the {@link #CHAD_FEATHER_TRACKER}
     * way.
     * <p></p>
     * Furthermore, because the listeners call events for every {@link org.bukkit.entity.Player} that are sent/receiving packets,
     * that means you are creating an O(n) time-complexity situation, where (n) represents the amount of players currently in the
     * same world. The more players you have, the most events get called and the more packets you have to modify + create. This
     * causes the {@code interception} method to become worse the more players you are trying to process.
     * **/
    VIRGIN_PACKET_INTERCEPTION("interception"),

    /** Makes {@code FeatherDisguises} fully utilise our custom rewritten {@link org.reborn.FeatherDisguise.tracker.FeatherEntityTracker}
     * system. This works by overriding the {@code tracker} object within the {@link net.minecraft.server.v1_8_R3.WorldServer} and injecting
     * our own {@code tracker} that handles both normal spigot entity packets & our {@code disguise} related packets. This means we handle
     * not only the math work but also how we create and distribute packets (at the earliest and most important level) meaning it is much
     * more efficient than {@link #VIRGIN_PACKET_INTERCEPTION}.
     * @apiNote
     * The {@link org.reborn.FeatherDisguise.tracker.FeatherEntityTracker} & {@link org.reborn.FeatherDisguise.tracker.FeatherEntityTrackerEntry} classes
     * are just rewritten and more optimised versions of their superclasses. They do the exact same thing as the parents with the added functionality
     * to directly handle {@code disguise} related math & packet work.
     * <p></p>
     * This means for the more "heavy" packets ({@code relative pos, relative mot, teleport, velocity & metadata}) the math work
     * is done all in one go and therefore doesn't require any {@code PacketEvents} interception. This completely removes the
     * O(n) time-complexity weakness of the {@link #VIRGIN_PACKET_INTERCEPTION}, however, you still need to process all the math
     * as well as the tracker logic for handling packets. Regardless, using the tracker version is a much more optimal experience.
     * **/
    CHAD_FEATHER_TRACKER("tracker");

    @NotNull private final String shortenedName;
}
