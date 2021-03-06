package ru.fleyer.freports.spigot.utils;

import com.google.common.collect.Iterables;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.fleyer.freports.FReports;
import ru.fleyer.freports.HandlerRequestBungee;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class RequestBungee {


    public void teleport(String sender, String player, String errorMessage) {
        if (!FReports.getInstance().config().yaml().getBoolean("bungeecord_enable_plugin")) {
            if (Bukkit.getPlayerExact(sender) == null) {
                return;
            }
            if (Bukkit.getPlayerExact(player) == null) {
                return;
            }
            Bukkit.getPlayerExact(sender).teleport(Bukkit.getPlayerExact(player));
            return;
        }
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("nreports_teleport");
            out.writeUTF(sender);
            out.writeUTF(player);
            out.writeUTF(errorMessage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (Bukkit.getOnlinePlayers().size() == 0) {
            Bukkit.getServer().sendPluginMessage(FReports.getInstance(), "BungeeCord", b.toByteArray());
        } else {
            ((Player) Iterables.getFirst((Iterable) Bukkit.getOnlinePlayers(), null)).sendPluginMessage(FReports.getInstance(), "BungeeCord", b.toByteArray());
        }
    }

    public boolean checkOnline(Player p, String player) {
        if (!FReports.getInstance().config().yaml().getBoolean("bungeecord_enable_plugin")) {
            return Bukkit.getPlayerExact(player) != null;
        }
        return HandlerRequestBungee.online_players.contains(player);
    }


    public void sendMessageAll(String message, String permission) {
        if (!FReports.getInstance().config().yaml().getBoolean("bungeecord_enable_plugin")) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (!p.hasPermission(permission)) continue;
                p.sendMessage(message);
            }
            return;
        }
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("nreports_sendmessageall");
            out.writeUTF(message);
            out.writeUTF(permission);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (Bukkit.getOnlinePlayers().size() == 0) {
            Bukkit.getServer().sendPluginMessage(FReports.getInstance(), "BungeeCord", b.toByteArray());
        } else {
            ((Player) Iterables.getFirst((Iterable) Bukkit.getOnlinePlayers(), null)).sendPluginMessage(FReports.getInstance(), "BungeeCord", b.toByteArray());
        }
    }
}

