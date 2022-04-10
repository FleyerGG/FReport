/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  net.md_5.bungee.BungeeCord
 *  net.md_5.bungee.api.config.ServerInfo
 *  net.md_5.bungee.api.connection.ProxiedPlayer
 *  net.md_5.bungee.api.event.PluginMessageEvent
 *  net.md_5.bungee.api.plugin.Listener
 *  net.md_5.bungee.api.plugin.Plugin
 *  net.md_5.bungee.event.EventHandler
 */
package ru.fleyer.freports.bungee;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.io.*;
import java.util.concurrent.TimeUnit;

public class Manager extends Plugin implements Listener {
    public void onEnable() {
        this.getProxy().getPluginManager().registerListener(this, this);
        BungeeCord.getInstance().registerChannel("BungeeCord");
    }


    @EventHandler
    public void onRead(PluginMessageEvent e) throws IOException {
        String channel;
        if (!e.getTag().equals("BungeeCord")) {
            return;
        }
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(e.getData()));
        switch (channel = in.readUTF()) {
            case "nreports_teleport": {
                String sender1 = in.readUTF();
                String player1 = in.readUTF();
                String message = in.readUTF();
                ProxiedPlayer sender = this.getProxy().getPlayer(sender1);
                ProxiedPlayer player = this.getProxy().getPlayer(player1);
                if (player == null) {
                    return;
                }
                if (!player.getServer().getInfo().getName().equals(sender.getServer().getInfo().getName())) {
                    sender.connect(player.getServer().getInfo());
                }
                getProxy().getScheduler().schedule(this, new Runnable() {
                    @Override
                    public void run() {
                        ByteArrayOutputStream b = new ByteArrayOutputStream();
                        DataOutputStream out = new DataOutputStream(b);
                        try {
                            out.writeUTF("nreports_teleportTo");
                            out.writeUTF(sender.getName());

                            out.writeUTF(player.getName());
                            out.writeUTF(message);
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                        player.getServer().sendData("nreports_network", b.toByteArray());
                    }
                }, 1, TimeUnit.SECONDS);
                break;
            }
            case "nreports_listplayers": {
                ByteArrayOutputStream b = new ByteArrayOutputStream();
                DataOutputStream out = new DataOutputStream(b);
                StringBuffer sb = new StringBuffer();
                for (ServerInfo s : this.getProxy().getServers().values()) {
                    for (ProxiedPlayer pl : s.getPlayers()) {
                        sb.append(pl.getName()).append(",");
                    }
                }
                out.writeUTF("nreports_listplayers_bukkit");
                out.writeUTF(sb.substring(0, sb.length() - 1));
                this.getProxy().getPlayer(in.readUTF()).getServer().sendData("nreports_network", b.toByteArray());
                break;
            }
            case "nreports_broadcast": {
                this.getProxy().broadcast(in.readUTF());
                break;
            }
            case "nreports_sendmessageall": {
                String message = in.readUTF();
                String permission = in.readUTF();
                for (ServerInfo serv : this.getProxy().getServers().values()) {
                    if (serv.getPlayers().size() <= 0) continue;
                    ByteArrayOutputStream b = new ByteArrayOutputStream();
                    DataOutputStream out = new DataOutputStream(b);
                    out.writeUTF("nreport_bukkit_sendmessageall");
                    out.writeUTF(message);
                    out.writeUTF(permission);
                    serv.sendData("nreports_network", b.toByteArray());
                }
                break;
            }
        }
    }
}

