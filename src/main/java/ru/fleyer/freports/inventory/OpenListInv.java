package ru.fleyer.freports.inventory;

import com.google.common.collect.Lists;
import com.rainchat.raingui.menus.ClickItem;
import com.rainchat.raingui.menus.PaginationMenu;
import com.rainchat.raingui.utils.general.Item;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import ru.fleyer.freports.FReports;
import ru.fleyer.freports.placeholders.ReportInfoPlaceholder;
import ru.fleyer.freports.placeholders.ReportPlaceholder;
import ru.fleyer.freports.report.Report;
import ru.fleyer.freports.report.ReportInfo;
import ru.fleyer.freports.report.ReportManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OpenListInv {

    public static void openReport(Player player) {
        PaginationMenu paginationMenu = new PaginationMenu(FReports.getInstance(), "Реоп", 4);


        paginationMenu.setItemSlots(Arrays.asList(
                10, 11, 12, 13, 14, 15, 16,
                19, 20, 21, 22, 23, 24, 25
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
                            "&7Всего репортов: %report_size%"
                    )).setPlaceholders(new ReportPlaceholder(report)),

                    inventoryClickEvent -> {
                openTargetReport(player, report.getName());
                    }
            );
            clickItems.add(clickItem);
        }
        paginationMenu.setItems(clickItems);
        paginationMenu.open(player);
    }


    public static void openTargetReport(Player player, String target) {
        PaginationMenu paginationMenu = new PaginationMenu(FReports.getInstance(), "Репорты", 4);


        paginationMenu.setItemSlots(Arrays.asList(
                10, 11, 12, 13, 14, 15, 16,
                19, 20, 21, 22, 23, 24, 25
        ));

        paginationMenu.guiFill(new ClickItem(new Item()
                .material(Material.GRAY_STAINED_GLASS_PANE), inventoryClickEvent -> {
        }));


        List<ClickItem> clickItems = new ArrayList<>();

        for (ReportInfo report : ReportManager.getReport(target).getReports()) {
            ClickItem clickItem = new ClickItem(new Item()
                    .material(Material.PAPER)
                    .name("&7Репорт на &e%report_name%")
                    .lore(Arrays.asList(
                            "&7Кто подал жалобу: %reportinfo_name%",
                            "&7Причена: %reportinfo_reason%"
                    )).setPlaceholders(new ReportInfoPlaceholder(report), new ReportPlaceholder(ReportManager.getReport(target))),

                    inventoryClickEvent -> {

                    }
            );
            clickItems.add(clickItem);
        }
        paginationMenu.setItems(clickItems);
        paginationMenu.open(player);
    }


}

