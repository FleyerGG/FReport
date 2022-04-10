/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package ru.fleyer.freports.inventory;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import ru.fleyer.freports.FReports;
import ru.fleyer.freports.report.Report;
import ru.fleyer.freports.report.ReportInfo;
import ru.fleyer.freports.report.ReportManager;
import ru.fleyer.freports.utils.ListUtils;
import ru.fleyer.freports.utils.RequestBungee;
import ru.fleyer.freports.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;


public class InventoryManager {
    private int page;

    public InventoryManager() {
    }

    public InventoryManager(int page) {
        this.page = page;
    }

    public void openList(Player player) {
        InventoryBuilder inv = new InventoryBuilder("Жалобы | Страница {page}".replace("{page}", String.valueOf(this.page)), 54, FReports.getInstance());
        inv.startUpdate(player, new BukkitRunnable() {

            public void run() {

                int j;
                inv.clearAll();
                ArrayList list = Lists.newArrayList();
                list.addAll(ReportManager.getHash().values());
                ArrayList ints = Lists.newArrayList();
                for (j = 10; j < 17; ++j) {
                    ints.add(j);
                }
                for (j = 19; j < 26; ++j) {
                    ints.add(j);
                }
                for (j = 28; j < 35; ++j) {
                    ints.add(j);
                }
                for (j = 37; j < 44; ++j) {
                    ints.add(j);
                }
                int pages = (int) Math.ceil((double) list.size() / 28.0);
                if (InventoryManager.this.page >= pages & InventoryManager.this.page > 1) {
                    int p1;
                    p1 = InventoryManager.this.page;
                    inv.setItem(new ItemBuilder().setSkull("MHF_ArrowLeft").setDisplayName(FReports.getInstance().config().yaml().getString(color("inventories.list-clans-page.back-item-page.name"))).setLore(ListUtils.replaceAll(FReports.getInstance().config().yaml().getStringList("inventories.list-clans-page.back-item-page.lore"), "{page}", String.valueOf(InventoryManager.this.page - 1))).done(), 0, () -> {
                        int p1x = p1 - 1;
                        new InventoryManager(p1x).openList(player);
                    });
                }

                if (InventoryManager.this.page < pages) {
                    int p1;
                    p1 = InventoryManager.this.page;
                    inv.setItem(new ItemBuilder().setSkull("MHF_ArrowRight").setDisplayName(FReports.getInstance().config().yaml().getString(color("inventory-list-reports.next-item-page.name"))).setLore(ListUtils.replaceAll(FReports.getInstance().config().yaml().getStringList("inventory-list-reports.next-item-page.lore"), "{page}", String.valueOf(InventoryManager.this.page + 1))).done(), 0, () -> {
                        int p1x = p1 + 1;
                        new InventoryManager(p1x).openList(player);
                    });
                }
                inv.setItem(new ItemBuilder(Material.BARRIER).setDisplayName(InventoryManager.this.color(FReports.getInstance().config().yaml().getString("inventory-list-reports.item-close.name"))).setLore(ListUtils.colorList(FReports.getInstance().config().yaml().getStringList("inventory-list-reports.item-close.lore"))).done(), 49, () -> player.closeInventory());
                inv.setItem(new ItemBuilder().setSkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWRhMjM4MGRjM2ZlMGFmZjIzMDE2ZDViMmMxMTczMWRhYmNhM2EyNTk4YmRjYWI1MzJjNzRkZTc5MzgzYWZlIn19fQ==").setDisplayName(InventoryManager.this.color(FReports.getInstance().config().yaml().getString("inventory-list-reports.item-stats.name"))).setLore(ListUtils.replaceAll(FReports.getInstance().config().yaml().getStringList("inventory-list-reports.item-stats.lore"), "{count}", String.valueOf(list.size()))).done(), 4);
                InventoryManager.this.page = (InventoryManager.this.page - 1) * 28;
                int p1 = InventoryManager.this.page;
                for (int i = 0; p1 < list.size() && i != 28; ++p1, ++i) {
                    Report r = (Report) list.get(p1);
                    //                     inv.setItem(new ItemBuilder().setSkull(r.getName()).setDisplayName(FReports.getInstance().config().yaml().getString(color("inventory-list-reports.format-item-skull.name").replace("{prefix}", FReports.getInstance().getPrefixPlayer(r.getName()))).replace("{target}", r.getName())).setLore(InventoryManager.this.listSettings(ListUtils.replaceAll(FReports.getInstance().config().yaml().getStringList("inventory-list-reports.format-item-skull.lore"), "{prefix}",  FReports.getInstance().getPrefixPlayer(r.getName()), "{target}", r.getName(), "{time}", TimeUtils.getTime(r.getReports().get(r.getReports().size() - 1).getTime()), "{reports-count}", String.valueOf(r.getReports().size())), r)).done(), (Integer)ints.get(i), () -> {
                    inv.setItem(new ItemBuilder().setSkull(r.getName()).setDisplayName(InventoryManager.this.color(FReports.getInstance().config().yaml().getString("inventory-list-reports.format-item-skull.name")
                                    .replace("{prefix}", r.getName()).replace("{target}", r.getName())))
                            .setLore(InventoryManager.this.listSettings(ListUtils.replaceAll(FReports.getInstance().config().yaml().getStringList("inventory-list-reports.format-item-skull.lore"), "{prefix}", r.getName(), "{target}", r.getName(), "{time}", TimeUtils.getTime(r.getReports().get(r.getReports().size() - 1).getTime()), "{reports-count}", String.valueOf(r.getReports().size())), r)).done(), (Integer) ints.get(i), () -> {
                        if (ReportManager.getReport(r.getName()) == null) {
                            player.closeInventory();
                        } else {
                            new InventoryManager().openProfilePlayer(player, "%target%".replace("%target%", r.getName()));
                        }
                    });
                }
                InventoryManager.this.page = (int) (Math.ceil((double) InventoryManager.this.page / 28.0) + 1.0);
            }
        }.runTaskTimer(FReports.getInstance(), 0L, 10L));
    }

    public List<String> listSettings(List<String> list, Report report) {
        ArrayList l = Lists.newArrayList();
        for (int i = 0; i < list.size(); ++i) {
            String text = list.get(i);
            if (text.contains("{reports}")) {
                for (ReportInfo inf : report.getReports()) {
                    l.add(this.color(FReports.getInstance().config().yaml().getString("inventory-list-reports.reports-format").replace("{prefix}", inf.getSender())).replace("{reporter}", inf.getSender()).replace("{reason}", inf.getReason()));
                }
                continue;
            }
            l.add(text);
        }
        return l;
    }

    public void openProfilePlayer(Player player, String playerName) {

        InventoryBuilder inv = new InventoryBuilder(this.color(FReports.getInstance().config().yaml().getString("inventory-profile-player.title").replace("{prefix}", playerName).replace("{target}", playerName)), 54, FReports.getInstance());
        inv.startUpdate(player, new BukkitRunnable() {

            public void run() {
                ArrayList<ReportInfo> reportsInfo = ReportManager.getReport(playerName).getReports();
                int j = 0;
                for (int i = 36; i <= 53; ++i) {
                    if (j >= reportsInfo.size()) {
                        inv.setItem(new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE, 1).setDisplayName(" ").done(), i);
                        continue;
                    }
                    ReportInfo r = reportsInfo.get(j);
                    inv.setItem(new ItemBuilder(Material.PAPER).setDisplayName(InventoryManager.this.color(FReports.getInstance().config().yaml().getString("inventory-profile-player.format-item-report.name").replace("{number}", String.valueOf(j + 1)))).setLore(ListUtils.replaceAll(FReports.getInstance().config().yaml().getStringList("inventory-profile-player.format-item-report.lore"), "{time}", TimeUtils.getTime(r.getTime()), "{prefix}", r.getSender(), "{reporter}", r.getSender(), "{reason}", r.getReason())).done(), i);
                    ++j;
                }
                inv.setItem(new ItemBuilder().setSkull("MHF_ArrowLeft").setDisplayName(InventoryManager.this.color(FReports.getInstance().config().yaml().getString("inventory-profile-player.back-item.name"))).setLore(ListUtils.colorList(FReports.getInstance().config().yaml().getStringList("inventory-profile-player.back-item.lore"))).done(), 4, () -> new InventoryManager(1).openList(player));
                inv.setItem(new ItemBuilder().setSkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzc0NzJkNjA4ODIxZjQ1YTg4MDUzNzZlYzBjNmZmY2I3ODExNzgyOWVhNWY5NjAwNDFjMmEwOWQxMGUwNGNiNCJ9fX0===").setDisplayName(InventoryManager.this.color(FReports.getInstance().config().yaml().getString("inventory-profile-player.cancel-item.name"))).setLore(ListUtils.colorList(FReports.getInstance().config().yaml().getStringList("inventory-profile-player.cancel-item.lore"))).done(), 20, () -> {
                    if (ReportManager.getReport(playerName) != null) {
                        ReportManager.removeReport(playerName);
                        new RequestBungee().sendMessageAll(FReports.getInstance().getMessage("cancel_report_player").replace("{admin}", player.getName()).replace("{target}", playerName), "magmareports.info-other");
                        new InventoryManager(1).openList(player);
                    } else {
                        new InventoryManager(1).openList(player);
                    }
                });
                inv.setItem(new ItemBuilder().setSkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzhiZThhYmQ2NmQwOWE1OGNlMTJkMzc3NTQ0ZDcyNmQyNWNhZDdlOTc5ZThjMjQ4MTg2NmJlOTRkM2IzMmYifX19").setDisplayName(InventoryManager.this.color(FReports.getInstance().config().yaml().getString("inventory-profile-player.teleport-item.name").replace("{target}", playerName))).setLore(ListUtils.replaceAll(FReports.getInstance().config().yaml().getStringList("inventory-profile-player.teleport-item.lore"), "{status}", new RequestBungee().checkOnline(player, playerName) ? InventoryManager.this.color(FReports.getInstance().config().yaml().getString("inventory-profile-player.teleport-item.status.online_player")) : InventoryManager.this.color(FReports.getInstance().config().yaml().getString("inventory-profile-player.teleport-item.status.offline_player")))).done(), 22, () -> {
                    if (new RequestBungee().checkOnline(player, playerName)) {
                        new RequestBungee().teleport(player.getName(), playerName, FReports.getInstance().getMessage("teleport_player").replace("{target}", playerName));
                        player.setGameMode(GameMode.SPECTATOR);
                    }
                });
                inv.setItem(new ItemBuilder().setSkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODZkMzVhOTYzZDU5ODc4OTRiNmJjMjE0ZTMyOGIzOWNkMjM4MjQyNmZmOWM4ZTA4MmIwYjZhNmUwNDRkM2EzIn19fQ").setDisplayName(InventoryManager.this.color(FReports.getInstance().config().yaml().getString("inventory-profile-player.ban-item.name"))).setLore(ListUtils.colorList(FReports.getInstance().config().yaml().getStringList("inventory-profile-player.ban-item.lore"))).done(), 24, () -> {
                    if (ReportManager.getReport(playerName) != null) {
                        new InventoryManager().openBanMenu(player, playerName);
                        InventoryManager.this.openBanMenu(player, playerName);
                    } else {
                        new InventoryManager(1).openList(player);
                    }
                });
                inv.openInventory(player);
            }

        }.runTaskTimer(FReports.getInstance(), 0L, 10L));

    }

    public void openBanMenu(Player p, String player) {
        InventoryBuilder inv = new InventoryBuilder("Test", 27, FReports.getInstance());
        int[] positions = new int[]{11, 12, 13, 14, 15};
        int i = 1;
        int o = 0;
        while (i <= 5) {
            int finalI = i;
            inv.setItem(new ItemBuilder().setSkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjQ1NGFkMjA4NzRjMWE3NGE2NWM4OTY1NzFlYmE2YzRjYjIyOTk0YmE1MjVkOTJjN2RjNjRhNjEyYWI1YyJ9fX0=").setDisplayName(this.color(FReports.getInstance().config().yaml().getString("inventory-ban-player.bans." + i + ".name").replace("{prefix}", player)).replace("{target}", player)).setLore(ListUtils.colorList(FReports.getInstance().config().yaml().getStringList("inventory-ban-player.bans." + i + ".lore"))).done(), positions[o], () -> {
                if (ReportManager.getReport(player) != null) {
                    ReportManager.removeReport(player);
                    new RequestBungee().sendMessageAll(FReports.getInstance().getMessage("banned_player").replace("{admin}", p.getName()).replace("{target}", player), "magmareports.info-other");
                    new InventoryManager(1).openList(p);
                    Bukkit.dispatchCommand(p, this.color(FReports.getInstance().config().yaml().getString("inventory-ban-player.bans." + finalI + ".punish").replace("{target}", player)));
                } else {
                    new InventoryManager(1).openList(p);
                }
            });
            ++i;
            ++o;
        }
        inv.setItem(new ItemBuilder().setSkull("MHF_ArrowLeft").setDisplayName(this.color(FReports.getInstance().config().yaml().getString("inventory-ban-player.back-item.name"))).setLore(ListUtils.colorList(FReports.getInstance().config().yaml().getStringList("inventory-ban-player.back-item.lore"))).done(), 4, () -> {
            if (ReportManager.getReport(player) != null) {
                new InventoryManager().openProfilePlayer(p, player);
            } else {
                new InventoryManager(1).openList(p);
            }
        });
        inv.openInventory(p);
    }

    public String color(String path) {
        return String.valueOf(ChatColor.translateAlternateColorCodes('&', path));
    }
}

