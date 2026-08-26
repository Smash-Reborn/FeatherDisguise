package org.reborn.FeatherDisguise.tracker.relational;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntity;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityTeleport;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
public abstract class RelationalTrackerEntryImpl implements RelationalTrackerEntry {

    @Override
    @Nullable public List<Integer> listInteractableEntityIDsForInteractPacket() {
        return null;
    }

    @Override
    @Nullable public List<PacketPlayOutEntity.PacketPlayOutRelEntityMove> relativePositionPackets(@NotNull Entity trackedEntity, int decodedPosX, int decodedPosY, int decodedPosZ, boolean isOnGround) {
        return null;
    }

    @Override
    @Nullable public List<PacketPlayOutEntity.PacketPlayOutEntityLook> relativeRotationPackets(@NotNull Entity trackedEntity, int decodedYaw, int decodedPitch, boolean isOnGround) {
        return null;
    }

    @Override
    @Nullable public List<PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook> relativePositionRotationPackets(@NotNull Entity trackedEntity, int decodedPosX, int decodedPosY, int decodedPosZ, int decodedYaw, int decodedPitch, boolean isOnGround) {
        return null;
    }

    @Override
    @Nullable public List<PacketPlayOutEntityTeleport> teleportPositionSyncPackets(@NotNull Entity trackedEntity, int posX, int posY, int posZ, int yaw, int pitch, boolean isOnGround) {
        return null;
    }

    @Override
    public void teardown() { }
}
