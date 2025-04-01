package org.reborn.FeatherDisguise.tracker;

import lombok.extern.log4j.Log4j2;
import net.minecraft.server.v1_8_R3.World;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.reborn.FeatherDisguise.FeatherDisguise;
import org.reborn.FeatherDisguise.util.ITeardown;

import java.util.HashMap;
import java.util.Iterator;

@Log4j2
public class DisguiseTrackerListener implements Listener, ITeardown {

    @ApiStatus.Internal
    @NotNull private final FeatherDisguise featherDisguise;

    @Nullable private HashMap<World, FeatherEntityTracker> worldsHandledByCustomTracker;

    public DisguiseTrackerListener(@NotNull final FeatherDisguise featherDisguise) {
        this.featherDisguise = featherDisguise;
        featherDisguise.getServer().getPluginManager().registerEvents(this, featherDisguise);
        this.implementFeatherEntityTrackerForNMSWorld(featherDisguise.getServer().getWorlds().get(0));

        // todo:
        //  - removeDisguise command doesn't show player when removeDisguise() is called even though it should
        //  - arrows sometimes incorrectly positioned on the ground
    }

    @ApiStatus.Internal
    private void implementFeatherEntityTrackerForNMSWorld(@NotNull final org.bukkit.World bukkitWorld) {
        if (worldsHandledByCustomTracker == null) {
            worldsHandledByCustomTracker = new HashMap<>();
        }

        // get the NMS world, create our custom tracker and override
        // (because we do this within the WorldInitEvent, there will never be any entities existing within that tracker, so it's fine to replace it)
        final WorldServer nmsWorld = ((CraftWorld) bukkitWorld).getHandle();
        final FeatherEntityTracker featherEntityTracker = new FeatherEntityTracker(featherDisguise, nmsWorld);
        nmsWorld.tracker = featherEntityTracker;
        worldsHandledByCustomTracker.put(nmsWorld, featherEntityTracker);

        log.info("World (Name: {}) has been injected with the FeatherDisguise custom entity tracker", nmsWorld.getWorld().getName());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onWorldInitializeHandleReplacingWithCustomEntityTracker(final WorldInitEvent e) {
        this.implementFeatherEntityTrackerForNMSWorld(e.getWorld());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onWorldUnloadHandleTeardownOfCustomEntityTracker(final WorldUnloadEvent e) {
        final WorldServer nmsWorld = ((CraftWorld) e.getWorld()).getHandle();

        if (worldsHandledByCustomTracker != null) {

            // if the hashmap has valid reference data for our world, then we know for certain
            // it was utilising our custom tracker, so we need to teardown and remove data for it
            if (worldsHandledByCustomTracker.containsKey(nmsWorld)) {
                final FeatherEntityTracker featherTracker = worldsHandledByCustomTracker.remove(nmsWorld);
                featherTracker.teardown();
            }

            if (worldsHandledByCustomTracker.isEmpty()) {
                worldsHandledByCustomTracker = null;
            }
        }
    }

    @Override
    public void teardown() {
        HandlerList.unregisterAll(this);

        if (worldsHandledByCustomTracker != null && !worldsHandledByCustomTracker.isEmpty()) {

            // loop over all existing entries, teardown and then remove data
            final Iterator<FeatherEntityTracker> trackerIter = worldsHandledByCustomTracker.values().iterator();
            while (trackerIter.hasNext()) {
                trackerIter.next().teardown();
                trackerIter.remove();
            }
        }

        worldsHandledByCustomTracker = null;
    }
}
