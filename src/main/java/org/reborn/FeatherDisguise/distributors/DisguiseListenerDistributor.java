package org.reborn.FeatherDisguise.distributors;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.simple.PacketPlaySendEvent;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.reborn.FeatherDisguise.FeatherDisguise;
import org.reborn.FeatherDisguise.distributors.impl.*;
import org.reborn.FeatherDisguise.enums.PacketHandlingType;
import org.reborn.FeatherDisguise.protocol.DisguiseIncomingPacketInterceptor;
import org.reborn.FeatherDisguise.protocol.DisguiseOutgoingPacketInterceptor;
import org.reborn.FeatherDisguise.types.AbstractDisguise;
import org.reborn.FeatherDisguise.util.ITeardown;

import java.util.HashMap;

@Log4j2
public class DisguiseListenerDistributor implements ITeardown, Listener {

    @Getter @NotNull private final FeatherDisguise featherDisguise;

    @ApiStatus.Internal
    @NotNull private final PacketHandlingType packetHandlingType;

    private HashMap<DisguisePacketDistributorType, IDisguisePacketDistributor> disguisePacketDistributors;

    private DisguiseOutgoingPacketInterceptor outgoingPacketInterceptor;
    private DisguiseIncomingPacketInterceptor incomingPacketInterceptor;

    public DisguiseListenerDistributor(@NotNull final FeatherDisguise featherDisguise, @NotNull final PacketHandlingType packetHandlingType) {
        this.featherDisguise = featherDisguise;
        this.packetHandlingType = packetHandlingType;
        this.initializePacketDistributors();

        featherDisguise.getServer().getPluginManager().registerEvents(this, featherDisguise);

        this.outgoingPacketInterceptor = new DisguiseOutgoingPacketInterceptor(this);
        this.incomingPacketInterceptor = new DisguiseIncomingPacketInterceptor(this);
        PacketEvents.getAPI().getEventManager().registerListeners(this.outgoingPacketInterceptor, this.incomingPacketInterceptor);
    }

    public void handleOutgoingInterceptedPackets(@NotNull final PacketPlaySendEvent packetSendEvent, @NotNull final PacketWrapper<?> interceptedPacket,
                                                 @NotNull final AbstractDisguise<?> disguise, @NotNull final Player observer) {

        // boutta make you exit this world through intensive care
        if (disguisePacketDistributors == null || disguisePacketDistributors.isEmpty()) {
            log.warn("Unable to handle packet distribution for intercepted disguise packets because the distribution map is invalid or null");
            return;
        }

        final DisguisePacketDistributorType distributorType;
        switch (packetSendEvent.getPacketType()) {
            default:
                distributorType = DisguisePacketDistributorType.UNKNOWN_OR_INVALID;
                break;
            case ENTITY_RELATIVE_MOVE_AND_ROTATION:
            case ENTITY_RELATIVE_MOVE:
            case ENTITY_ROTATION:
                distributorType = DisguisePacketDistributorType.RELATIVE_POSITION_ROTATION;
                break;
            case ENTITY_HEAD_LOOK:
                distributorType = DisguisePacketDistributorType.HEAD_ROTATION;
                break;
            case ENTITY_TELEPORT:
                distributorType = DisguisePacketDistributorType.TELEPORT_POSITION_ROTATION;
                break;
            case ENTITY_ANIMATION:
                distributorType = DisguisePacketDistributorType.ANIMATION;
                break;
            case ENTITY_EQUIPMENT:
                distributorType = DisguisePacketDistributorType.EQUIPMENT;
                break;
            case ENTITY_METADATA:
                distributorType = DisguisePacketDistributorType.METADATA;
                break;
            case ENTITY_VELOCITY:
                distributorType = DisguisePacketDistributorType.VELOCITY;
                break;
            case ATTACH_ENTITY:
                distributorType = DisguisePacketDistributorType.ATTACH_RIDING_OR_LEASH;
                break;
            case SPAWN_PLAYER:
                distributorType = DisguisePacketDistributorType.SPAWNING_PLAYER;
                break;
            case DESTROY_ENTITIES:
                distributorType = DisguisePacketDistributorType.DESTROY_PLAYER;
                break;
        }

        if (distributorType == DisguisePacketDistributorType.UNKNOWN_OR_INVALID || !disguisePacketDistributors.containsKey(distributorType)) {
            log.warn("How the fuck are you even getting to this point in the code???? If you are, congrats but also we are super fucked");
            return;
        }

        try {
            disguisePacketDistributors.get(distributorType).handlePacketInterception(packetSendEvent, interceptedPacket, disguise, observer, this);
        } catch (Exception ex) {
            log.warn("Failed to handle packet interception for ({}) type, (pervert: {})", distributorType, observer.getName(), ex);
        }
    }


    @ApiStatus.Internal
    private void initializePacketDistributors() {
        this.disguisePacketDistributors = new HashMap<>(this.packetHandlingType == PacketHandlingType.VIRGIN_PACKET_INTERCEPTION ? 10 : 4);

        // only allow these distributors to be loaded if we are fully using interceptors
        if (this.packetHandlingType == PacketHandlingType.VIRGIN_PACKET_INTERCEPTION) {
            this.disguisePacketDistributors.put(DisguisePacketDistributorType.RELATIVE_POSITION_ROTATION, new DisguisePacketPosRotDistributor());
            this.disguisePacketDistributors.put(DisguisePacketDistributorType.TELEPORT_POSITION_ROTATION, new DisguisePacketTeleportDistributor());
            this.disguisePacketDistributors.put(DisguisePacketDistributorType.HEAD_ROTATION, new DisguisePacketHeadRotDistributor());
            this.disguisePacketDistributors.put(DisguisePacketDistributorType.METADATA, new DisguisePacketMetadataDistributor());
            this.disguisePacketDistributors.put(DisguisePacketDistributorType.SPAWNING_PLAYER, new DisguisePacketSpawnDistributor());
            this.disguisePacketDistributors.put(DisguisePacketDistributorType.DESTROY_PLAYER, new DisguisePacketDestroyDistributor());
        }

        // regardless of the handling type, we always want these to be loaded
        this.disguisePacketDistributors.put(DisguisePacketDistributorType.VELOCITY, new DisguisePacketVelocityDistributor());
        this.disguisePacketDistributors.put(DisguisePacketDistributorType.ANIMATION, new DisguisePacketAnimationDistributor());
        this.disguisePacketDistributors.put(DisguisePacketDistributorType.EQUIPMENT, new DisguisePacketEquipmentDistributor());
        this.disguisePacketDistributors.put(DisguisePacketDistributorType.ATTACH_RIDING_OR_LEASH, new DisguisePacketAttachDistributor());
    }

    @EventHandler
    public void onDisguisedPlayerQuitRemoveData(PlayerQuitEvent e) {
        featherDisguise.getDisguiseAPI().removeDisguise(e.getPlayer(), false);
    }

    @EventHandler
    public void onDisguisedPlayerKickedRemoveData(PlayerKickEvent e) {
        featherDisguise.getDisguiseAPI().removeDisguise(e.getPlayer(), false);
    }

    @EventHandler
    public void onDisguisedPlayerChangeWorldsUpdateData(PlayerChangedWorldEvent e) {
        featherDisguise.getDisguiseAPI().hideDisguiseForPlayers(e.getPlayer(), e.getFrom().getPlayers(), false);
        // only need to remove the disguise from the previous world, spawning distributor handles the new worlds spawning packets
    }

    @Override
    public void teardown() {
        if (disguisePacketDistributors != null) {
            disguisePacketDistributors.clear();
            disguisePacketDistributors = null;
        }

        HandlerList.unregisterAll(this);

        PacketEvents.getAPI().getEventManager().unregisterListeners(outgoingPacketInterceptor, incomingPacketInterceptor);
        outgoingPacketInterceptor = null;
        incomingPacketInterceptor = null;
    }
}
