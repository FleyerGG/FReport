package ru.fleyer.freports.spigot.data.inventory;

import com.rainchat.raingui.menus.ClickItem;
import com.rainchat.raingui.menus.PaginationMenu;
import com.rainchat.raingui.utils.general.Item;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import ru.fleyer.freports.FReports;
import ru.fleyer.freports.spigot.placeholders.ReportInfoPlaceholder;
import ru.fleyer.freports.spigot.placeholders.ReportPlaceholder;
import ru.fleyer.freports.spigot.data.report.Report;
import ru.fleyer.freports.spigot.data.report.ReportInfo;
import ru.fleyer.freports.spigot.data.report.ReportManager;
import ru.fleyer.freports.spigot.utils.RequestBungee;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OpenListInv {
    public static String msg(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    private static FileConfiguration lang = FReports.getInstance().lang().msg();

    public static void openReport(Player player) {
        PaginationMenu paginationMenu = new PaginationMenu(FReports.getInstance(), "Жалобы ||", 5);

        paginationMenu.startUpdate(Bukkit.getScheduler().runTaskTimerAsynchronously(FReports.getInstance(), () -> {
            int page = paginationMenu.getPage();

            paginationMenu.setItem(43, new ClickItem(new Item().material(Material.ARROW)
                    .name("&aСледующая страница")
                    .lore(Arrays.asList(
                            "",
                            "&7Нажмите для перехода",
                            "&7на &e&n%page%&7 страницу".replace("%page%", String.valueOf(page + 2))
                    )), event -> {
                paginationMenu.setPage(paginationMenu.getPage() + 1);
            }));


            paginationMenu.setItem(38, new ClickItem(new Item().material(Material.BARRIER).name("&c&l[Закрыть список]").lore(Arrays.asList(
                    "",
                    "&aНажмите сюда, чтобы закрыть список"
            )), event -> {
                paginationMenu.close(player);
            }));

            paginationMenu.setItem(41, new ClickItem(new Item().material(Material.ARROW)
                    .name("&aПредыдущая страница")
                    .lore(Arrays.asList(
                            "",
                            "&7Нажмите для перехода",
                            "&7на &e&n%page%&7 страницу".replace("%page%", String.valueOf(page + 1))
                    )), event -> {
                paginationMenu.setPage(paginationMenu.getPage() - 1);
            }));

            paginationMenu.setItemSlots(Arrays.asList(
                    10, 11, 12, 13, 14, 15, 16,
                    19, 20, 21, 22, 23, 24, 25,
                    28, 29, 30, 31, 32, 33, 34
            ));

            paginationMenu.guiFill(new ClickItem(new Item()
                    .material(Material.GRAY_STAINED_GLASS_PANE), inventoryClickEvent -> {
            }));


            List<ClickItem> clickItems = new ArrayList<>();

            for (Report report : ReportManager.getHash().values()) {
                ClickItem clickItem = new ClickItem(new Item()
                        .material(Material.PAPER)
                        .name("&7Репорт на &e%report_name%")
                        .lore(List.of(
                                "",
                                "&7Всего пожаловались: &6%report_size%"
                        )).setPlaceholders(new ReportPlaceholder(report)),
                        event -> {
                            openTargetReport(player, report.getName());
                        }

                );
                clickItems.add(clickItem);
            }
            paginationMenu.setItems(clickItems);

        }, 0L, 60L));
        paginationMenu.open(player);
    }


    public static void openTargetReport(Player player, String target) {

        PaginationMenu paginationMenu = new PaginationMenu(FReports.getInstance(), "Репорты", 5);
        int page = paginationMenu.getPage();


        paginationMenu.setItem(43, new ClickItem(new Item().material(Material.ARROW).name("&aСледующая страница")
                .lore(Arrays.asList(
                        "",
                        "&7Нажмите для перехода",
                        "&7на &e&n%page%&7 страницу".replace("%page%", String.valueOf(page + 2))
                )), event -> {
            paginationMenu.setPage(paginationMenu.getPage() + 1);
        }));

        paginationMenu.setItem(41, new ClickItem(new Item().material(Material.ARROW).name("&aПредыдущая страница")
                .lore(Arrays.asList(
                        "",
                        "&7Нажмите для перехода",
                        "&7на &e&n%page%&7 страницу"
                                .replace("%page%", String.valueOf(page + 1))
                )), event -> {
            paginationMenu.setPage(paginationMenu.getPage() - 1);
        }));

        paginationMenu.setItem(6, new ClickItem(new Item().material(Material.BOOK).name("&cОтклонить репорт").lore(Arrays.asList(
                "",
                "&aНажмите сюда, чтобы отклонить репорт"
        )), event -> {
            paginationMenu.close(player);
            ReportManager.removeReport(target);
            new RequestBungee().sendMessageAll(FReports.getInstance().getMessage("cancel_report_player").replace("%admin%", player.getName()).replace("%target%", target), "freport.info-report");
            System.out.println(FReports.getInstance().getMessage("cancel_report_player").replace("%admin%", player.getName()).replace("%target%", target));
            player.sendMessage(msg("&aВы успешно отклонили репорт"));
        }));

        paginationMenu.setItem(4, new ClickItem(new Item().material(Material.BOOK).name("&eТелепортироваться к &c%target%"
                .replace("%target%", target)).lore(Arrays.asList(
                "",
                "&aНажмите сюда, чтобы телепортироваться"
        )), event -> {
            if (new RequestBungee().checkOnline(player, target)) {
                new RequestBungee().teleport(player.getName(), target, FReports.getInstance().getMessage("teleport_player").replace("%target%", target));
                player.sendMessage(msg(lang.getString("messages.teleport_player").replace("%target%", target)));
                player.setGameMode(GameMode.SPECTATOR);
            } else {

                player.sendMessage(msg(lang.getString("messages.player_not_online").replace("%target%", target)));
            }
        }));
        paginationMenu.setItem(2, new ClickItem(new Item().material(Material.BOOK).name("&cВыдать бан").lore(Arrays.asList(
                "",
                "&aНажмите сюда, чтобы выдать бан игроку"
        )), event -> {
            onBansPlayer(player, target);
        }));
        paginationMenu.setItemSlots(Arrays.asList(
                10, 11, 12, 13, 14, 15, 16,
                19, 20, 21, 22, 23, 24, 25,
                28, 29, 30, 31, 32, 33, 34
        ));

        paginationMenu.guiFill(new ClickItem(new Item()
                .material(Material.GRAY_STAINED_GLASS_PANE), inventoryClickEvent -> {
        }));


        List<ClickItem> clickItems = new ArrayList<>();
        if (ReportManager.getReport(target) != null) {
            for (ReportInfo report : ReportManager.getReport(target).getReports()) {
                ClickItem clickItem = new ClickItem(new Item()
                        .material(Material.PAPER)
                        .name("&7Репорт на &e%report_name%")
                        .lore(Arrays.asList(
                                "",
                                "&7Нытик: &6%reportinfo_name%",
                                "&7Причина: &6%reportinfo_reason%",
                                "&7Время: &e%reportinfo_time% назад"

                        )).setPlaceholders(new ReportInfoPlaceholder(report), new ReportPlaceholder(ReportManager.getReport(target))),

                        event -> {
                            onBansPlayer(player, target);
                        }
                );
                clickItems.add(clickItem);
            }
            paginationMenu.setItems(clickItems);
            paginationMenu.open(player);
        }
    }

    public static void onBansPlayer(Player player, String target) {
        PaginationMenu paginationMenu = new PaginationMenu(FReports.getInstance(), "Блокировка %target%".replace("%target%", target), 5);
        int page = paginationMenu.getPage();

        List<ClickItem> clickItems = new ArrayList<>();

        paginationMenu.guiFill(new ClickItem(new Item()
                .material(Material.GRAY_STAINED_GLASS_PANE), inventoryClickEvent -> {
        }));


        paginationMenu.setItemSlots(Arrays.asList(
                10, 11, 12, 13, 14, 15, 16,
                19, 20, 21, 22, 23, 24, 25,
                28, 29, 30, 31, 32, 33, 34
        ));


        paginationMenu.setItem(43, new ClickItem(new Item().material(Material.ARROW)
                .name("&aСледующая страница")
                .lore(Arrays.asList(
                        "",
                        "&7Нажмите для перехода",
                        "&7на &e&n%page%&7 страницу".replace("%page%", String.valueOf(page + 2))
                )), event -> {
            paginationMenu.setPage(paginationMenu.getPage() + 1);
        }));

        paginationMenu.setItem(41, new ClickItem(new Item().material(Material.ARROW).name("&aПредыдущая страница")
                .lore(Arrays.asList(
                        "",
                        "&7Нажмите для перехода",
                        "&7на &e&n%page%&7 страницу"
                                .replace("%page%", String.valueOf(page + 1))
                )), event -> {
            paginationMenu.setPage(paginationMenu.getPage() - 1);
        }));

        paginationMenu.setItem(38, new ClickItem(new Item().material(Material.SKELETON_SKULL).name("&eВернуться в профиль игрока").lore(Arrays.asList(
                "",
                "&aНажмите сюда, чтобы вернуться в профиль игрока"
        )), event -> {
            openTargetReport(player, target);
        }));

        for (String s : FReports.getInstance().lang().msg().getConfigurationSection("ban-player.bans").getKeys(false)) {

            ClickItem clickItem = new ClickItem(new Item().material(Material.PAPER)
                    .name(lang.getString("ban-player.bans." + s + ".name"))
                    .lore(lang.getStringList("ban-player.bans." + s + ".lore")), event -> {
                player.chat(lang.getString("ban-player.bans." + s + ".punish").replace("%target%", target));
                ReportManager.removeReport(target);
                paginationMenu.close(player);
                new RequestBungee().sendMessageAll(FReports.getInstance().getMessage("cancel_report_player").replace("%admin%", player.getName()).replace("%target%", target), "freport.info-report");
                System.out.println(FReports.getInstance().getMessage("cancel_report_player").replace("%admin%", player.getName()).replace("%target%", target));
                player.sendMessage(FReports.getInstance().getMessage("banned_player").replace("%target%", target));

            });
            clickItems.add(clickItem);
        }

        paginationMenu.setItems(clickItems);
        paginationMenu.open(player);
    }


}

