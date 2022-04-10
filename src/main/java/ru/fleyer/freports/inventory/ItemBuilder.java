/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  com.mojang.authlib.properties.Property
 *  org.bukkit.Material
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.inventory.ItemFlag
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.inventory.meta.SkullMeta
 */
package ru.fleyer.freports.inventory;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.util.List;
import java.util.UUID;
import ru.fleyer.freports.reflection.ReflectionUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class ItemBuilder {
    private ItemStack item;

    public ItemBuilder(ItemStack item) {
        this.item = item;
    }

    public ItemBuilder() {
        this.item = new ItemStack(Material.LEGACY_SKULL_ITEM, 1, (short) 3);
    }

    public ItemBuilder(Material material) {
        this.item = new ItemStack(material);
    }

    public ItemBuilder(Material material, int amount) {
        this.item = new ItemStack(material, amount);
    }

    public ItemBuilder setDisplayName(String displayName) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(displayName);
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setLore(List<String> lores) {
        ItemMeta meta = this.item.getItemMeta();
        meta.setLore(lores);
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder addEnchant(Enchantment type, int level, boolean bol) {
        ItemMeta meta = this.item.getItemMeta();
        meta.addEnchant(type, level, bol);
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder addItemFlags(ItemFlag flag) {
        ItemMeta meta = this.item.getItemMeta();
        meta.addItemFlags(new ItemFlag[]{flag});
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setSkullTexture(String texture) {
        SkullMeta skull = ((SkullMeta) item.getItemMeta()).clone();
        GameProfile profile = new GameProfile(UUID.randomUUID(), "Steve");
        profile.getProperties().put("textures", new Property("textures", texture));
        ReflectionUtils.setField((Object)skull, "profile", (Object)profile);
        this.item.setItemMeta((ItemMeta)skull);
        return this;
    }

    public ItemBuilder setSkull(String owner) {
        SkullMeta skull = (SkullMeta)this.item.getItemMeta();
        skull.setOwner(owner);
        this.item.setItemMeta((ItemMeta)skull);
        return this;
    }

    public ItemStack done() {
        return this.item;
    }
}

