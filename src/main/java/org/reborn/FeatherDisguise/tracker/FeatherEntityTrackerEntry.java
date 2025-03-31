package org.reborn.FeatherDisguise.tracker;

import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityTrackerEntry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FeatherEntityTrackerEntry extends EntityTrackerEntry {

    /*
     * ## ACCESSIBLE FIELDS FROM SUPERCLASS ##
     *
     * Entity entityBeingTracked -> [tracker]
     * int trackingDistanceThreshold -> [b]
     * int updateFrequency -> [c]
     *
     * int xPosition -> [xLoc]
     * int yPosition -> [yLoc]
     * int zPosition -> [zLoc]
     *
     * int yawRotation -> [yRot]
     * int pitchRotation -> [xRot]
     * int lastHeadRotation -> [i]
     *
     * double xMotion -> [j]
     * double yMotion -> [k]
     * double zMotion -> [l]
     *
     * int updateCounter -> [m]
     * boolean havePlayersBeenUpdated -> [n]
     * Set<EntityPlayer> playersWhoCanSeeTheTrackedEntity -> [trackedPlayers]
     */

    // ## NON-ACCESSIBlE FIELDS (implemented locally as public) ##
    public double lastTrackedXPosition;
    public double lastTrackedYPosition;
    public double lastTrackedZPosition;

    public boolean hasEntityMovedPosition;
    public boolean isSendingVelocityPackets;

    public int ticksSinceLastForcedTeleportSynchronisation;
    public boolean isFlaggedOnGround;

    @Nullable public Entity passengerEntity;
    public boolean isRidingAnotherEntity;

    public FeatherEntityTrackerEntry(@NotNull final Entity entityBeingTracked, final int trackingDistanceThreshold, final int updateFrequency, final boolean allowVelocityPacketUpdates) {
        super(entityBeingTracked, trackingDistanceThreshold, updateFrequency, allowVelocityPacketUpdates);

        this.isSendingVelocityPackets = allowVelocityPacketUpdates;
        this.isFlaggedOnGround = entityBeingTracked.onGround;
        this.passengerEntity = entityBeingTracked.passenger != null ? entityBeingTracked.passenger : null;
    }


}
