package ru.fleyer.freports.spigot.utils;

import org.bukkit.ChatColor;

import java.util.List;

public class ListUtils {
    public static List<String> replaceAll(List<String> list, String... values) {
        for (int i = 0; i < list.size(); ++i) {
            int u = 0;
            while (u < values.length) {
                list.set(i, list.get(i).replace(values[u++], values[u++]));
            }
        }
        return ListUtils.colorList(list);
    }

    public static List<String> colorList(List<String> list) {
        for (int i = 0; i < list.size(); ++i) {
            list.set(i, ChatColor.translateAlternateColorCodes((char) '&', (String) list.get(i)));
        }
        return list;
    }
}

