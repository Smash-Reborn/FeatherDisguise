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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Log4j2
public class CommandRemoveDisguise implements CommandExecutor, TabCompleter {

    // /removedisguise <player-name>

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (strings.length < 1) {
            log.warn("/removedisguise could not execute because not enough parameters were provided");
            commandSender.sendMessage(ChatColor.RED + "You did not provide enough parameters" + ChatColor.RESET);
            commandSender.sendMessage(ChatColor.GRAY + "Command usage: /removedisguise <player-name>" + ChatColor.RESET);
            return false;
        }

        final Player targetPlayer = Bukkit.getPlayerExact(strings[0]);

        if (targetPlayer == null || !targetPlayer.isValid()) {
            log.warn("/removedisguise could not execute because the player provided is invalid or not online");
            commandSender.sendMessage(ChatColor.RED + "The player you provided was invalid or not online" + ChatColor.RESET);
            return false;
        }

        FeatherDisguise.getStaticInstance().getDisguiseAPI().removeDisguise(targetPlayer, true);

        log.info("Successfully executed /removedisguise for player ({})", targetPlayer.getName());
        if (commandSender.getName().equals(targetPlayer.getName())) {
            commandSender.sendMessage(ChatColor.GREEN + "Attempting to remove any current disguise you have equipped" + ChatColor.RESET);
        } else {
            commandSender.sendMessage(ChatColor.GREEN + targetPlayer.getName() + " has had any current disguise data removed" + ChatColor.RESET);
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        final Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
        final List<String> tabCompList = new ArrayList<>(onlinePlayers.size());
        onlinePlayers.forEach(player -> tabCompList.add(player.getName()));
        return tabCompList;
    }
}
