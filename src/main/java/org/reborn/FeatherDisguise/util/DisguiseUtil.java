package org.reborn.FeatherDisguise.util;

import com.github.retrooper.packetevents.event.simple.PacketPlaySendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.reborn.FeatherDisguise.DisguiseType;
import org.reborn.FeatherDisguise.types.AbstractDisguise;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@NoArgsConstructor(access = AccessLevel.NONE)
public class DisguiseUtil {

    /** Attempts to play a {@link org.bukkit.Sound} at the given {@link Location} via the {@code sound}
     * fields found within the provided {@link AbstractDisguise}.
     * @apiNote
     * The only reason this exists is for some reason in 1.8, mojank or spigot just didn't bother to
     * add all the sounds to the {@link org.bukkit.Sound} enum. And there are certain entity
     * sounds we need to use that aren't provided. So this is the workaround, that lets
     * us use interchangeable sound references without too much fucking around.
     * **/
    public static void playDisguiseSoundAtLocation(@NotNull Location location, @NotNull AbstractDisguise<?> disguise, boolean deathSound) {
        final float volume = disguise.getDisguiseBaseSoundVolume() + (float) (0.5f * Math.random());
        final float pitch = disguise.getDisguiseBaseSoundPitch() + (float) (0.4f * Math.random());

        if (!deathSound) {
            if (disguise.getDisguiseHurtSoundString() != null) {
                ((CraftWorld) location.getWorld()).getHandle().makeSound(
                        location.getX(), location.getY(), location.getZ(),
                        disguise.getDisguiseHurtSoundString(), volume, pitch);
            }
        }

        else {
            if (disguise.getDisguiseDeathSoundString() != null) {
                ((CraftWorld) location.getWorld()).getHandle().makeSound(
                        location.getX(), location.getY(), location.getZ(),
                        disguise.getDisguiseDeathSoundString(), volume, pitch);
            }
        }
    }

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
