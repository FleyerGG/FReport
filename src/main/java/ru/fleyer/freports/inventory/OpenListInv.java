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
import ru.fleyer.freports.report.Report;
import ru.fleyer.freports.report.ReportInfo;

import java.net.http.WebSocket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OpenListInv {

    public void openList (Player player){
        PaginationMenu paginationMenu = new PaginationMenu(FReports.getInstance(),"WorldStats", 4);


        paginationMenu.setItemSlots(Arrays.asList(
                10,11,12,13,14,15,16,
                19,20,21,22,23,24,25
        ));

        paginationMenu.guiFill(new ClickItem(new Item()
                .material(Material.GRAY_STAINED_GLASS_PANE), inventoryClickEvent -> {}));



        List<ClickItem> clickItems = new ArrayList<>();

        for (World world: Bukkit.getWorlds()) {
            ClickItem clickItem =  new ClickItem(new Item()
                    .material(Material.PAPER)
                    .name("&7Stats &e%world_name%")
                    .lore(Arrays.asList(
                            "&7Difficulty: %world_difficulty%",
                            "&7Time: %world_time%"
                    )),

                    inventoryClickEvent -> {
                        player.teleport(world.getSpawnLocation());
                    }
            );
            clickItems.add(clickItem);
        }
        paginationMenu.setItems(clickItems);
        paginationMenu.open(player);
    }

    public List<String> listSettings(List<String> list, Report report) {
        ArrayList l = Lists.newArrayList();
        for (int i = 0; i < list.size(); ++i) {
            String text = list.get(i);
            if (text.contains("{reports}")) {
                for (ReportInfo inf : report.getReports()) {
                    l.add(FReports.getInstance().config().yaml().getString("inventory-list-reports.reports-format").replace("{prefix}", inf.getSender()).replace("{reporter}", inf.getSender()).replace("{reason}", inf.getReason()));
                }
                continue;
            }
            l.add(text);
        }
        return l;
    }
}

