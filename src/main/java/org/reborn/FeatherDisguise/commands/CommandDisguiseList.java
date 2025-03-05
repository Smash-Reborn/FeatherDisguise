package org.reborn.FeatherDisguise.commands;

import lombok.extern.log4j.Log4j2;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.reborn.FeatherDisguise.util.DisguiseUtil;

@Log4j2
public class CommandDisguiseList implements CommandExecutor {

    // /disguiselist

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        commandSender.sendMessage(ChatColor.GREEN + "DisguiseTypes: " + ChatColor.RESET + DisguiseUtil.LIST_ALLOWED_DISGUISE_TYPES);
        return true;
    }
}
