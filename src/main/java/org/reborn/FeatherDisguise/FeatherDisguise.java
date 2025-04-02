package org.reborn.FeatherDisguise;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.util.ColorUtil;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.reborn.FeatherDisguise.commands.CommandDisguise;
import org.reborn.FeatherDisguise.commands.CommandDisguiseList;
import org.reborn.FeatherDisguise.commands.CommandRefreshDisguise;
import org.reborn.FeatherDisguise.commands.CommandRemoveDisguise;
import org.reborn.FeatherDisguise.enums.PacketHandlingType;
import org.reborn.FeatherDisguise.metadata.CachedEntityTypes;
import org.reborn.FeatherDisguise.util.Constants;

@Log4j2
public class FeatherDisguise extends JavaPlugin {

    @Getter private static JavaPlugin plugin;

    @Getter private static FeatherDisguise staticInstance; // godly cringe but im lazy af

    private DisguiseAPI disguiseAPI; // use provided getter instead, not lombok

    @Getter private CachedEntityTypes cachedEntityTypes;

    @Override
    public void onLoad() {
        try {
            log.info(Constants.formattedNeutralText("Now loading " + ColorUtil.toString(NamedTextColor.AQUA) + Constants.PLUGIN_NAME + " " + ColorUtil.toString(NamedTextColor.WHITE) + "plugin"));
            PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
            PacketEvents.getAPI().load();
            // essential when loading packetEvents for disguise handling

        } catch (Exception ex) {
            log.warn("{}{}", Constants.formattedNegativeText("Failed to load " + ColorUtil.toString(NamedTextColor.RED) + Constants.PLUGIN_NAME + " " + ColorUtil.toString(NamedTextColor.WHITE)), ex);
        }
    }

    @Override
    public void onEnable() {
        try {
            log.info(Constants.formattedNeutralText("Now enabling " + ColorUtil.toString(NamedTextColor.AQUA) + Constants.PLUGIN_NAME + " " + ColorUtil.toString(NamedTextColor.WHITE) + "plugin"));
            plugin = this;
            staticInstance = this;
            PacketEvents.getAPI().getSettings().checkForUpdates(false); // todo why does this not work AHHHHHHHHHHH
            PacketEvents.getAPI().init();
            disguiseAPI = new DisguiseAPI(this, PacketHandlingType.CHAD_FEATHER_TRACKER); // todo config file with swappable type
            cachedEntityTypes = new CachedEntityTypes();
            this.registerCommands();
            log.info(Constants.formattedPositiveText("Successfully enabled " + ColorUtil.toString(NamedTextColor.GREEN) + Constants.PLUGIN_NAME + " " + ColorUtil.toString(NamedTextColor.WHITE) + "plugin"));

        } catch (Exception ex) {
            log.warn("{}{}", Constants.formattedNegativeText("Failed to enable " + ColorUtil.toString(NamedTextColor.RED) + Constants.PLUGIN_NAME + " " + ColorUtil.toString(NamedTextColor.WHITE)), ex);
        }
    }

    @Override
    public void onDisable() {
        try {
            log.info(Constants.formattedNeutralText("Now disabling " + ColorUtil.toString(NamedTextColor.AQUA) + Constants.PLUGIN_NAME + " " + ColorUtil.toString(NamedTextColor.WHITE) + "plugin"));
            cachedEntityTypes.teardown();
            cachedEntityTypes = null;
            disguiseAPI.teardown();
            disguiseAPI = null;
            PacketEvents.getAPI().terminate();
            plugin = null;
            log.info(Constants.formattedPositiveText("Successfully disabled " + ColorUtil.toString(NamedTextColor.GREEN) + Constants.PLUGIN_NAME + " " + ColorUtil.toString(NamedTextColor.WHITE) + "plugin"));

        } catch (Exception ex) {
            log.warn("{}{}", Constants.formattedNegativeText("Failed to disable " + ColorUtil.toString(NamedTextColor.RED) + Constants.PLUGIN_NAME + " " + ColorUtil.toString(NamedTextColor.WHITE)), ex);
        }
    }

    @ApiStatus.Internal
    private void registerCommands() {
        if (plugin == null) return; // h u H
        plugin.getServer().getPluginCommand("disguise").setExecutor(new CommandDisguise());
        plugin.getServer().getPluginCommand("removedisguise").setExecutor(new CommandRemoveDisguise());
        plugin.getServer().getPluginCommand("disguiselist").setExecutor(new CommandDisguiseList());
        plugin.getServer().getPluginCommand("refreshdisguise").setExecutor(new CommandRefreshDisguise());
    }

    @NotNull public DisguiseAPI getDisguiseAPI() {
        if (disguiseAPI == null) throw new NullPointerException("DisguiseAPI is invalid or null");
        return disguiseAPI;
    }
}
