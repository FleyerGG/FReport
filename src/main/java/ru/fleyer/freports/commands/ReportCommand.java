/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.permissions.PermissibleBase
 *  org.bukkit.permissions.ServerOperator
 */
package ru.fleyer.freports.commands;

import com.ubivashka.vk.spigot.VKAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissibleBase;
import ru.fleyer.freports.FReports;
import ru.fleyer.freports.cooldown.CooldownManager;
import ru.fleyer.freports.report.ReportManager;
import ru.fleyer.freports.utils.NumberStringUtils;
import ru.fleyer.freports.utils.RequestBungee;
import ru.fleyer.freports.utils.TimeUtils;

public class ReportCommand implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String l, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        if (!sender.hasPermission("freport.report")) {

            sender.sendMessage(FReports.getInstance().getMessage("no_permission"));
            return false;
        }
        if (CooldownManager.hasCdw(sender.getName(), "report")) {
            sender.sendMessage(FReports.getInstance().getMessage("cooldown_report_command").replace("{time}",
                    TimeUtils.getTime(CooldownManager.getLeftTime(sender.getName(), "report"))));
            return false;
        }
        if (args.length < 2) {
            sender.sendMessage(FReports.getInstance().getMessage("usage_report_command"));
            return false;
        }
        /*if (!new RequestBungee().checkOnline((Player)sender, args[0])) {
            sender.sendMessage(FReports.getInstance().getMessage("player_not_online").replace("%target%", args[0]));
            return false;
        }*/
        PermissibleBase pb = new PermissibleBase(Bukkit.getOfflinePlayer(args[0]));
        if (pb.hasPermission("freport.protec")) {
            sender.sendMessage(FReports.getInstance().getMessage("protection_player"));
            return false;
        }
        if (sender.getName().equalsIgnoreCase(args[0].toLowerCase())) {
            sender.sendMessage(FReports.getInstance().getMessage("sender_instanceof_player"));
            return false;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 1; i < args.length; ++i) {
            sb.append(args[i]).append(" ");
        }
        if (sb.substring(0, sb.length() - 1).length() < 3) {
            sender.sendMessage(FReports.getInstance().getMessage("little_reason"));
            return false;
        }
        if (ReportManager.getReport(args[0]) != null && ReportManager.getReport(args[0]).getReports().size() == 18) {
            sender.sendMessage(FReports.getInstance().getMessage("limit_reports").replace("%target%", args[0]));
            return false;
        }
        ReportManager.addReport(args[0], sender.getName(), sb.substring(0, sb.length() - 1));
        VKAPI.getInstance().getVKUtil().sendMSGtoPeer(FReports.getInstance().config().yaml().getInt("VK.peer"),("@online #ticket\n" +
                "⚠ Поступила жалоба ⚠\n" +
                "\n👤 Игрок: %player%" +
                "\n❗ Нарушитель: %target%" +
                "\n💬 Причина: %reason%\n" +
                "\n📗 Сервер: " + FReports.getInstance().config().yaml().getString("VK.server")).replace("%player%", sender.getName())
                                            .replace("%target%",args[0])
                                            .replace("%reason%",sb.substring(0, sb.length() - 1)));

        sender.sendMessage(FReports.getInstance().getMessage("successful_send_report").replace("%target%", args[0])
                                                                                        .replace("{reason}", sb.substring(0, sb.length() - 1)));
        new RequestBungee().sendMessageAll(FReports.getInstance().getMessage("info_reports_player").replace("%target%", args[0]).replace("{reports}",
                NumberStringUtils.getFormat(ReportManager.getReport(args[0]).getReports().size(),
                        "репорт", "репорта", "репортов")), "freport.info-report");


        CooldownManager.createCooldown(sender.getName(), "report", TimeUtils.longTime(FReports.getInstance().config().yaml().getString("cooldown-report-command")));
        return false;
    }
}

