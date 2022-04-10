/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package ru.fleyer.freports.commands;

import ru.fleyer.freports.FReports;
import ru.fleyer.freports.inventory.InventoryManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReportsCommand implements CommandExecutor {
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player)) {
            return false;
        }
        if (!commandSender.hasPermission("magmareports.reports")) {
            commandSender.sendMessage(FReports.getInstance().getMessage("no_permission"));
            return false;
        }
        Player p = (Player) commandSender;
        new InventoryManager(1).openList(p);
        return false;
    }
}

