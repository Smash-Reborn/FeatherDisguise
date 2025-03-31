package org.reborn.FeatherDisguise.types.hostile;

import lombok.extern.log4j.Log4j2;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.EntityTrackerEntry;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityTeleport;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.reborn.FeatherDisguise.enums.DisguiseType;
import org.reborn.FeatherDisguise.metadata.EntityDimensions;
import org.reborn.FeatherDisguise.metadata.types.hostile.MagmaCubeMetadataHolder;
import org.reborn.FeatherDisguise.types.AbstractDisguise;
import org.reborn.FeatherDisguise.util.DisguiseUtil;
import org.reborn.FeatherDisguise.util.PacketUtil;

@Log4j2
public class DisguiseMagmaCube extends AbstractDisguise<MagmaCubeMetadataHolder> {

    public DisguiseMagmaCube(@NotNull Player player) {
        super(DisguiseType.MAGMA_CUBE, new MagmaCubeMetadataHolder(), player);
    }

    @Override
    @NotNull public Sound getDisguiseHurtSound() {
        return Sound.MAGMACUBE_WALK2;
    }

    @Override
    @NotNull public Sound getDisguiseDeathSound() {
        return Sound.LAVA_POP;
    }

    @Override
    public float getDisguiseBaseSoundPitch() {
        return 0.75f;
    }

    public void setCubeSize(final int size) {
        if (this.getRelatedEntitiesWrapper().getBaseDisguiseEntity().getMetadataHolder().getSize() == size) return; // don't bother if the size is already the same

        this.getRelatedEntitiesWrapper().getBaseDisguiseEntity().getMetadataHolder().setSize(size);
        this.setCachedEntityDimensions(new EntityDimensions(
                this.getRelatedEntitiesWrapper().getBaseDisguiseEntity().getMetadataHolder().getEntityType().getEntityDimensions().getWidth() * size,
                this.getRelatedEntitiesWrapper().getBaseDisguiseEntity().getMetadataHolder().getEntityType().getEntityDimensions().getHeight() * size));
        // use the already cached dimensions of the base slime, and just multiply via the size, works the same in nms
        this.recalculateSquidEntityYPosModifier(); // now recalculate, ez pz

        this.getRelatedEntitiesWrapper().sendUpdateMetadataForBaseDisguiseEntityToAllViewingPlayers();

        // now we will send a teleport packet to all viewing clients, synchronising the new position
        //      (the reason we call the players entity tracker is that we want to use the calculated
        //       vars within the entry to determine their absolute tick position. if we use the nms
        //       locXYZ or lastXYZ, it is still not accurate to the current position, meaning at certain
        //       synchronisation ticks or deltas, the disguise will jerk instead of smoothly interpolating.
        //       doing it this way ensures the fake teleport packet we are sending mimics the one the tracker
        //       would have also sent out, removing any of the weird jerkiness or de-synchronisation)
        final EntityPlayer nmsPlayer = ((CraftPlayer) this.getOwningBukkitPlayer()).getHandle();
        final EntityTrackerEntry nmsPlayerEntityTracker =
                ((CraftWorld) this.getOwningBukkitPlayer().getWorld()).getHandle().tracker.trackedEntities.get(nmsPlayer.getId());

        if (nmsPlayerEntityTracker == null) { // if the player is in the world, this should never ever happen, but just in case...
            log.warn("Unable to fetch player ({}) entity tracker entry. Cannot update absolute position for disguise related entities ({})",
                    this.getOwningBukkitPlayer().getName(), this.getDisguiseType());
            return;
        }

        final PacketPlayOutEntityTeleport fakeTPPacket = new PacketPlayOutEntityTeleport(
                nmsPlayer.getId(),
                nmsPlayerEntityTracker.xLoc,
                nmsPlayerEntityTracker.yLoc,
                nmsPlayerEntityTracker.zLoc,
                (byte) nmsPlayerEntityTracker.yRot,
                (byte) nmsPlayerEntityTracker.xRot,
                nmsPlayer.onGround);
        DisguiseUtil.getPlayersInWorldExcluding(this.getOwningBukkitPlayer())
                .forEach(viewer -> PacketUtil.sendNMSPacket(viewer, fakeTPPacket));
    }
}
