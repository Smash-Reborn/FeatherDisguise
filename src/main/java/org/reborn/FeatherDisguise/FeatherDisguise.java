package org.reborn.FeatherDisguise;

import com.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.bukkit.plugin.java.JavaPlugin;

@Log4j2
public class FeatherDisguise extends JavaPlugin {

    @Getter private static JavaPlugin plugin;

    @Getter private DisguiseAPI disguiseAPI;

    private static final String CYAN_CONSOLE_COLOR = "\033[0;36m";

    @Override
    public void onLoad() {
        log.info(CYAN_CONSOLE_COLOR + "Loading FeatherDisguise plugin");
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().load();
        // essential when loading packetEvents for disguise handling
    }

    @Override
    public void onEnable() {
        log.info(CYAN_CONSOLE_COLOR + "Enabling FeatherDisguise plugin");
        plugin = this;
        PacketEvents.getAPI().init();
        disguiseAPI = new DisguiseAPI(this);
    }

    @Override
    public void onDisable() {
        log.info(CYAN_CONSOLE_COLOR + "Disabling FeatherDisguise plugin");
        disguiseAPI.teardown();
        disguiseAPI = null;
        PacketEvents.getAPI().terminate();
        plugin = null;
    }
}
