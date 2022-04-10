/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.HandlerList
 *  org.bukkit.event.Listener
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.inventory.InventoryCloseEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitTask
 */
package ru.fleyer.freports.inventory;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.ConcurrentHashMap;

public class InventoryBuilder implements Listener {
    private int rows;
    private Plugin plugin;
    private Inventory inv;
    private ConcurrentHashMap<ItemStack, Runnable> items;
    private ConcurrentHashMap<Player, BukkitTask> update;
    private static ConcurrentHashMap<Integer, ItemStack> positions;
    private static ConcurrentHashMap<String, InventoryBuilder> inventory;
    private static Listener listener;

    public InventoryBuilder(String title, int rows, Plugin plugin) {
        this.inv = Bukkit.createInventory(null, rows, (title == null ? " " : title));
        this.items = new ConcurrentHashMap();
        positions = new ConcurrentHashMap();
        this.update = new ConcurrentHashMap();
        this.plugin = plugin;
        this.rows = rows;
        this.create();
        this.events();
    }

    //было
    public String getTitle() {
        return this.inv.getType().name();
    }

    public InventoryBuilder setItem(ItemStack item, int position, Runnable run) {
        if (!this.items.containsKey(item)) {
            this.items.put(item, run);
        }
        this.setItem(item, position);
        return this;
    }

    public InventoryBuilder setItem(ItemStack item, int position) {
        if (!positions.containsKey(position)) {
            positions.put(position, item);
        }
        this.inv.setItem(position, item);
        return this;
    }

    public InventoryBuilder create() {
        if (!inventory.contains(this)) {
            inventory.put(inv.getType().name(), this);
        }
        return this;
    }

    public ConcurrentHashMap<Player, BukkitTask> getUpds() {
        return this.update;
    }

    public InventoryBuilder startUpdate(Player player, BukkitTask task) {
        this.update.put(player, task);
        this.openInventory(player);
        return this;
    }

    public InventoryBuilder openInventory(Player player) {
        player.openInventory(this.inv);
        return this;
    }

    public void clearAll() {
        this.inv.clear();
        this.getPositions().clear();
        this.items.clear();
    }

    public ConcurrentHashMap<Integer, ItemStack> getPositions() {
        return positions;
    }

    public void next(int position) {
        if (this.items.get(positions.get(position)) == null) {
            return;
        }
        this.items.get(positions.get(position)).run();
    }

    private void events() {
        if (listener != null) {
            HandlerList.unregisterAll(listener);
        }
        listener = new Listener() {

            @EventHandler
            public void onClick(InventoryClickEvent e) {
                InventoryBuilder builder = InventoryBuilder.getInvs().get(e.getInventory().getType().name());
                if (builder == null) {
                    return;
                }
                if (e.getInventory().getType().name().equals(builder.getTitle())) {
                    e.setCancelled(true);
                    for (Integer slot : builder.getPositions().keySet()) {
                        if (e.getSlot() == slot) {
                            builder.next(slot);
                        }
                    }
                }
            }

            @EventHandler
            public void onClose(InventoryCloseEvent e) {
                InventoryBuilder builder = InventoryBuilder.getInvs().get(e.getInventory().getType().name());
                if (builder == null) {
                    return;
                }
                Player p = (Player) e.getPlayer();
                if (builder.getUpds().containsKey(p)) {
                    builder.getUpds().get(p).cancel();
                    builder.getUpds().clear();
                }
            }

            @EventHandler
            public void onCloseOut(PlayerQuitEvent e) {
                Player p = e.getPlayer();
                p.closeInventory();
            }
        };
        Bukkit.getPluginManager().registerEvents(listener, this.plugin);
    }

    public static ConcurrentHashMap<String, InventoryBuilder> getInvs() {
        return inventory;
    }

    static {
        inventory = new ConcurrentHashMap();
        listener = null;
    }
}

