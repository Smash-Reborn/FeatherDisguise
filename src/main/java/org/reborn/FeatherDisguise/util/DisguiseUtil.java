package org.reborn.FeatherDisguise.util;

import com.github.retrooper.packetevents.event.simple.PacketPlaySendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.reborn.FeatherDisguise.DisguiseType;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@NoArgsConstructor(access = AccessLevel.NONE)
public class DisguiseUtil {

    /** @return List of {@link Player} within the {@link org.bukkit.World}. **/
    @NotNull public static List<Player> getPlayersInWorldExcluding(@NotNull Player toExclude) {
        final List<Player> allPlayers = new ArrayList<>(toExclude.getWorld().getPlayers());
        allPlayers.remove(toExclude);
        return allPlayers;
    }

    /** Represents an invalid disguise {@code entityID}. Instead of using {@link Integer}, we can
     * make methods within this class return this var instead and prevent any un-necessary unboxing.
     * **/
    public static final int INVALID_DISGUISE_ENTITY_ID = -53915;

    /** @return {@code true} if the {@link DisguiseType} is able to render items in the hand-slots. **/
    public static boolean isDisguiseAbleToRenderItemsInHandSlots(@NotNull DisguiseType disguiseType) {
        switch (disguiseType) {
            default:
                return false;
            case GIANT:
            case SKELETON:
            case WITCH:
            case WITHER_SKELETON:
            case ZOMBIE:
            case ZOMBIE_VILLAGER:
                return true;
        }
    }

    /** @return {@code true} if the {@link DisguiseType} is able to render items in armor-slots. **/
    public static boolean isDisguiseAbleToRenderItemsInArmorSlots(@NotNull DisguiseType disguiseType) {
        switch (disguiseType) {
            default:
                return false;
            case GIANT:
            case SKELETON:
            case WITHER_SKELETON:
            case ZOMBIE:
            case ZOMBIE_VILLAGER:
                return true;
        }
    }

    /** @return {@code true} if the {@link DisguiseType} is allowed to handle incoming velocity packets.
     * (this refers to the fix where entities like squids "jitter" around on the client. something-something mojanK code) **/
    public static boolean isDisguiseAllowedToHandleVelocityPackets(@NotNull DisguiseType disguiseType) {
        return disguiseType == DisguiseType.SQUID; // L O L
    }

    /** @return {@code true} if the {@link DisguiseType} is able to render arm swing animations on the entity model. **/
    public static boolean isDisguiseAbleToRenderHandArmSwings(@NotNull DisguiseType disguiseType) {
        switch (disguiseType) {
            default:
                return false;
            case SKELETON:
            case ZOMBIE:
            case ENDERMAN:
            case GIANT:
            case WITHER_SKELETON:
            case ZOMBIE_VILLAGER:
                return true;
        }
    }

    /** @return {@code true} if the {@link PacketPlaySendEvent#getPacketType()} is allowed to be handled by one of our interceptors. **/
    public static boolean isAllowedToHandleOutgoingPacketInterception(@NotNull final PacketPlaySendEvent e) {
        switch (e.getPacketType()) {
            default:
                return false;
            case ENTITY_RELATIVE_MOVE_AND_ROTATION:
            case ENTITY_RELATIVE_MOVE:
            case ENTITY_ROTATION:
            case ENTITY_TELEPORT:
            case ENTITY_HEAD_LOOK:
            case ENTITY_ANIMATION:
            case ENTITY_EQUIPMENT:
            case ENTITY_METADATA:
            case ENTITY_VELOCITY:
            case SPAWN_PLAYER:
            case DESTROY_ENTITIES:
                return true;
        }
    }

    /** @return {@link PacketWrapper} corresponding to the packet being sent out
     * via the {@link PacketPlaySendEvent}. If there is no relevant packet to construct
     * then the method will instead return {@code null}.
     * **/
    @Nullable public static PacketWrapper<?> constructPacketWrapperFromEvent(@NotNull final PacketPlaySendEvent e) {
        switch (e.getPacketType()) {
            default:
                return null;
            case ENTITY_RELATIVE_MOVE_AND_ROTATION:
                return new WrapperPlayServerEntityRelativeMoveAndRotation(e);
            case ENTITY_RELATIVE_MOVE:
                return new WrapperPlayServerEntityRelativeMove(e);
            case ENTITY_ROTATION:
                return new WrapperPlayServerEntityRotation(e);
            case ENTITY_TELEPORT:
                return new WrapperPlayServerEntityTeleport(e);
            case ENTITY_HEAD_LOOK:
                return new WrapperPlayServerEntityHeadLook(e);
            case ENTITY_ANIMATION:
                return new WrapperPlayServerEntityAnimation(e);
            case ENTITY_EQUIPMENT:
                return new WrapperPlayServerEntityEquipment(e);
            case ENTITY_METADATA:
                return new WrapperPlayServerEntityMetadata(e);
            case ENTITY_VELOCITY:
                return new WrapperPlayServerEntityVelocity(e);
            case SPAWN_PLAYER:
                return new WrapperPlayServerSpawnPlayer(e);
            case DESTROY_ENTITIES:
                return new WrapperPlayServerDestroyEntities(e);
        }
    }

    /** @return integer relevant {@code entityID} from the corresponding {@link PacketWrapper} fields.
     * If there is no relevant field to obtain an id, then the method will instead return {@link #INVALID_DISGUISE_ENTITY_ID}.
     * @apiNote
     * This exists because {@code packetEvents} for some reason has no easy or universal way to obtain
     * an {@code entityID} from clientbound relevant entity packets. So this is our hacky workaround for now.
     * **/
    public static int getEntityIDFromPacketEventWrapper(@NotNull final PacketType.Play.Server packetType, @NotNull final PacketWrapper<?> packetWrapper) {
        // some cheeky casting we are about to do, using the packetType to avoid if else or slower loops
        switch (packetType) {
            default:
                return INVALID_DISGUISE_ENTITY_ID;
            case ENTITY_RELATIVE_MOVE_AND_ROTATION:
                return ((WrapperPlayServerEntityRelativeMoveAndRotation) packetWrapper).getEntityId();
            case ENTITY_RELATIVE_MOVE:
                return ((WrapperPlayServerEntityRelativeMove) packetWrapper).getEntityId();
            case ENTITY_ROTATION:
                return ((WrapperPlayServerEntityRotation) packetWrapper).getEntityId();
            case ENTITY_TELEPORT:
                return ((WrapperPlayServerEntityTeleport) packetWrapper).getEntityId();
            case ENTITY_HEAD_LOOK:
                return ((WrapperPlayServerEntityHeadLook) packetWrapper).getEntityId();
            case ENTITY_ANIMATION:
                return ((WrapperPlayServerEntityAnimation) packetWrapper).getEntityId();
            case ENTITY_EQUIPMENT:
                return ((WrapperPlayServerEntityEquipment) packetWrapper).getEntityId();
            case ENTITY_METADATA:
                return ((WrapperPlayServerEntityMetadata) packetWrapper).getEntityId();
            case ENTITY_VELOCITY:
                return ((WrapperPlayServerEntityVelocity) packetWrapper).getEntityId();
            case SPAWN_PLAYER:
                return ((WrapperPlayServerSpawnPlayer) packetWrapper).getEntityId();
            case DESTROY_ENTITIES:
                return ((WrapperPlayServerDestroyEntities) packetWrapper).getEntityIds()[0];
        }
    }
}
