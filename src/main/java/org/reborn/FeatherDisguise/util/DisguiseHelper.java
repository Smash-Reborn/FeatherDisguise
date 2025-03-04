package org.reborn.FeatherDisguise.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.reborn.FeatherDisguise.DisguiseType;

@Log4j2
@NoArgsConstructor(access = AccessLevel.NONE)
public class DisguiseHelper {

    /** @return {@code true} if the {@link DisguiseType} is able to render items in the hand-slots. **/
    public static boolean isDisguiseAbleToRenderItemsInHandSlots(@NotNull DisguiseType disguiseType) {
        return
                disguiseType == DisguiseType.GIANT ||
                disguiseType == DisguiseType.SKELETON ||
                disguiseType == DisguiseType.WITCH ||
                disguiseType == DisguiseType.WITHER_SKELETON ||
                disguiseType == DisguiseType.ZOMBIE ||
                disguiseType == DisguiseType.ZOMBIE_VILLAGER;
        // tfw "enhanced switch cases not available in java 8" ahhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh
    }

    /** @return {@code true} if the {@link DisguiseType} is able to render items in armor-slots. **/
    public static boolean isDisguiseAbleToRenderItemsInArmorSlots(@NotNull DisguiseType disguiseType) {
        return
                disguiseType == DisguiseType.GIANT ||
                disguiseType == DisguiseType.SKELETON ||
                disguiseType == DisguiseType.WITHER_SKELETON ||
                disguiseType == DisguiseType.ZOMBIE ||
                disguiseType == DisguiseType.ZOMBIE_VILLAGER;
    }

    /** @return {@code true} if the {@link DisguiseType} is allowed to handle incoming velocity packets.
     * (this refers to the fix where entities like squids "jitter" around on the client. something-something mojanK code) **/
    public static boolean isDisguiseAllowedToHandleVelocityPackets(@NotNull DisguiseType disguiseType) {
        return disguiseType == DisguiseType.SQUID; // L O L
    }

    /** @return {@code true} if the {@link DisguiseType} is able to render arm swing animations on the entity model. **/
    public static boolean isDisguiseAbleToRenderHandArmSwings(@NotNull DisguiseType disguiseType) {
        return
                disguiseType == DisguiseType.SKELETON ||
                disguiseType == DisguiseType.ZOMBIE ||
                disguiseType == DisguiseType.ENDERMAN ||
                disguiseType == DisguiseType.GIANT ||
                disguiseType == DisguiseType.WITHER_SKELETON ||
                disguiseType == DisguiseType.ZOMBIE_VILLAGER;
    }

    /** @return {@link WorldServer} object from the {@link Player} in the constructor. **/
    @NotNull public static WorldServer getNMSWorldFromBukkitPlayer(@NotNull Player bukkitPlayer) {
        return ((CraftWorld) bukkitPlayer.getWorld()).getHandle();
    }
}
