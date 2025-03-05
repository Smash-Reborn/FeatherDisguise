package org.reborn.FeatherDisguise.commands;

import lombok.extern.log4j.Log4j2;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.reborn.FeatherDisguise.FeatherDisguise;
import org.reborn.FeatherDisguise.util.Constants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Log4j2
public class CommandRefreshDisguise implements CommandExecutor, TabCompleter {

    // /refreshdisguise
    // /refreshdisguise @e
    // /refreshdisguise <player-name>

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        // should only be allowed to be usable in-game
        if (!(commandSender instanceof Player)) {
            log.warn("/refreshdisguise can only be executed inside the server");
            return false;
        }

        final Player executor = (Player) commandSender;

        // assume they typed no parameters, so they want to refresh all
        if (strings.length < 1 || strings[0].equalsIgnoreCase(Constants.EVERYONE_SYN)) {
            FeatherDisguise.getStaticInstance().getDisguiseAPI().refreshAllDisguisesForPlayer(executor);
            log.info("Successfully executed /refreshdisguise for player ({})", executor.getName());
            executor.sendMessage(ChatColor.GREEN + "Attempting to refresh ALL disguises" + ChatColor.RESET);
            return true;
        }

        // ok so at this point, they typed a parameter so they obviously wanna specifically refresh for a singular person
        final Player targetPlayer = Bukkit.getPlayerExact(strings[0]);

        if (targetPlayer == null || !targetPlayer.isValid()) {
            log.warn("/refreshdisguise could not execute because the player provided is invalid or not online");
            commandSender.sendMessage(ChatColor.RED + "The player you provided was invalid or not online" + ChatColor.RESET);
            return false;
        }

        FeatherDisguise.getStaticInstance().getDisguiseAPI().refreshDisguiseForPlayer(executor, targetPlayer);
        log.info("Successfully executed /refreshdisguise for player ({})", targetPlayer.getName());
        executor.sendMessage(ChatColor.GREEN + "Attempting to refresh disguise for " + targetPlayer.getName() + ChatColor.RESET);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        final Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
        final List<String> tabCompList = new ArrayList<>(onlinePlayers.size() + 1); // account for the '@e'
        tabCompList.add(Constants.EVERYONE_SYN);
        onlinePlayers.forEach(player -> tabCompList.add(player.getName()));
        return tabCompList;
    }
}
