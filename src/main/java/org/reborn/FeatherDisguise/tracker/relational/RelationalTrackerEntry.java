package org.reborn.FeatherDisguise.tracker.relational;

import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntity;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityTeleport;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.reborn.FeatherDisguise.util.Teardown;

import java.util.List;

public interface RelationalTrackerEntry extends Teardown {

    @Nullable List<Packet<?>> generateSpawningPacketsForRelationalEntities();

    @Nullable List<Integer> listRelationalEntityIDsForDestroyPacket();

    @Nullable List<Integer> listInteractableEntityIDsForInteractPacket();

    @NotNull Location getPositionRotationForSpawnPackets();

    @Nullable List<PacketPlayOutEntity.PacketPlayOutRelEntityMove> relativePositionPackets(@NotNull final Entity trackedEntity,
                                                                                           final int decodedPosX, final int decodedPosY, final int decodedPosZ, final boolean isOnGround);

    @Nullable List<PacketPlayOutEntity.PacketPlayOutEntityLook> relativeRotationPackets(@NotNull final Entity trackedEntity,
                                                                                        final int decodedYaw, final int decodedPitch, final boolean isOnGround);

    @Nullable List<PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook> relativePositionRotationPackets(@NotNull final Entity trackedEntity,
                                                                                                       final int decodedPosX, final int decodedPosY, final int decodedPosZ,
                                                                                                       final int decodedYaw, final int decodedPitch, final boolean isOnGround);

    @Nullable List<PacketPlayOutEntityTeleport> teleportPositionSyncPackets(@NotNull final Entity trackedEntity,
                                                                            final int posX, final int posY, final int posZ,
                                                                            final int yaw, final int pitch, final boolean isOnGround);
}
