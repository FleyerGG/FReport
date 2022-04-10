/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.google.common.collect.Iterables
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.messaging.PluginMessageListener
 */
package ru.fleyer.freports;

import com.google.common.collect.Iterables;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class HandlerRequestBungee implements PluginMessageListener {
    public static ArrayList<String> online_players = new ArrayList();

    public void onPluginMessageReceived(String s, Player player, byte[] bytes) {
        try {
            String channel;
            if (!s.equals("nreports_network")) {
                return;
            }
            DataInputStream in = new DataInputStream(new ByteArrayInputStream(bytes));
            switch (channel = in.readUTF()) {
                case "nreports_teleportTo": {
                    Player sender = Bukkit.getPlayerExact(in.readUTF());
                    if (sender == null) {
                        return;
                    }
                    Player p = Bukkit.getPlayerExact(in.readUTF());
                    String message = in.readUTF();
                    if (p == null) {
                        return;
                    }
                    sender.sendMessage(message);
                    sender.teleport(p);
                    sender.setGameMode(GameMode.SPECTATOR);
                    break;
                }
                case "nreport_bukkit_sendmessageall": {
                    String message = in.readUTF();
                    String perm = in.readUTF();
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (!p.hasPermission(perm)) continue;
                        p.sendMessage(message);
                    }
                    break;
                }
                case "nreports_listplayers_bukkit": {
                    String list = in.readUTF();
                    online_players.clear();
                    online_players.addAll(Arrays.asList(list.split(",")));
                    break;
                }
            }
        } catch (Exception exception) {
            // empty catch block
        }
    }

    public void sendList() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(FReports.getInstance(), () -> {
            if (Bukkit.getOnlinePlayers().size() > 0) {
                ByteArrayOutputStream b = new ByteArrayOutputStream();
                DataOutputStream out = new DataOutputStream(b);
                try {
                    out.writeUTF("nreports_listplayers");
                    out.writeUTF(((Player) Iterables.getFirst((Iterable) Bukkit.getOnlinePlayers(), null)).getName());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                if (Bukkit.getOnlinePlayers().size() == 0) {
                    Bukkit.getServer().sendPluginMessage(FReports.getInstance(), "BungeeCord", b.toByteArray());
                } else {
                    ((Player) Iterables.getFirst((Iterable) Bukkit.getOnlinePlayers(), null)).sendPluginMessage(FReports.getInstance(), "BungeeCord", b.toByteArray());
                }
            }
        }, 0L, 10L);
    }
}

