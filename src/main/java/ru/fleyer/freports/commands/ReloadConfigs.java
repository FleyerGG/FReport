package ru.fleyer.freports.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import ru.fleyer.freports.FReports;


public class ReloadConfigs implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args) {
        if (cmd.getName().equalsIgnoreCase("frl") && sender.hasPermission("freport.admin")){

            FReports.getInstance().lang().reloadConfiguration();
            FReports.getInstance().config().reloadConfiguration();
            sender.sendMessage(ChatColor.GREEN + "Configs Reloaded");
        }else {
            sender.sendMessage(FReports.getInstance().getMessage("no_permission"));
        }


        return false;
    }
}
