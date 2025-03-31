package org.reborn.FeatherDisguise.commands;

import lombok.extern.log4j.Log4j2;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.reborn.FeatherDisguise.enums.DisguiseType;
import org.reborn.FeatherDisguise.FeatherDisguise;
import org.reborn.FeatherDisguise.util.Constants;
import org.reborn.FeatherDisguise.util.DisguiseUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
public class CommandDisguise implements CommandExecutor, TabCompleter {

    // /disguise <player-name> <disguise-name>

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (strings.length < 1) {
            log.warn("/disguise could not execute because not enough parameters were provided");
            commandSender.sendMessage(ChatColor.RED + "You did not provide enough parameters" + ChatColor.RESET);
            commandSender.sendMessage(ChatColor.GRAY + "Command usage: /disguise <player-name> <disguise-name>" + ChatColor.RESET);
            return false;
        }

        final Player targetPlayer = Bukkit.getPlayerExact(strings[0]);

        if (targetPlayer == null || !targetPlayer.isValid()) {
            log.warn("/disguise could not execute because the player provided is invalid or not online");
            commandSender.sendMessage(ChatColor.RED + "The player you provided was invalid or not online" + ChatColor.RESET);
            return false;
        }

        if (strings.length < 2 || strings[1] == null) {
            log.warn("/disguise could not execute because not enough parameters were provided");
            commandSender.sendMessage(ChatColor.RED + "You did not provide enough parameters" + ChatColor.RESET);
            commandSender.sendMessage(ChatColor.GRAY + "Command usage: /disguise <player-name> <disguise-name>" + ChatColor.RESET);
            return false;
        }

        final Optional<DisguiseType> optDisguiseType = DisguiseUtil.getDisguiseTypeFromString(strings[1]);

        if (!optDisguiseType.isPresent()) {
            log.warn("/disguise could not execute because an invalid disguise type was provided");
            commandSender.sendMessage(ChatColor.RED + "The DisguiseType you provided was invalid or null" + ChatColor.RESET);
            commandSender.sendMessage(ChatColor.GRAY + "Try pressing 'tab' on the second argument to bring up a list of DisguiseTypes" + ChatColor.RESET);
            return false;
        }

        final DisguiseType disguiseType = optDisguiseType.get();
        FeatherDisguise.getStaticInstance().getDisguiseAPI().disguisePlayerAsEntity(targetPlayer, disguiseType);

        log.info("Successfully executed /disguise for player ({})", targetPlayer.getName());
        if (commandSender.getName().equals(targetPlayer.getName())) {
            commandSender.sendMessage(ChatColor.GREEN + "You have been disguised as a " + Constants.removeUndrNCapFrstLtr(disguiseType.name().toLowerCase()) + ChatColor.RESET);
        } else {
            commandSender.sendMessage(ChatColor.GREEN + targetPlayer.getName() + " has been disguised as a " + Constants.removeUndrNCapFrstLtr(disguiseType.name().toLowerCase()) + ChatColor.RESET);
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        final List<String> tabCompList = new ArrayList<>();

        if (strings.length <= 1) {
            Bukkit.getOnlinePlayers().forEach(player -> tabCompList.add(player.getName()));
        } else {
            //tabCompList.add(Constants.EVERYONE_SYN); // @e
            tabCompList.addAll(DisguiseUtil.ALLOWED_DISGUISE_TYPES.keySet());
        }

        return tabCompList;
    }
}
