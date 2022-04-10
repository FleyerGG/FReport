/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 */
package ru.fleyer.freports.utils;

import java.util.List;
import org.bukkit.ChatColor;

public class ListUtils {
    public static List<String> replaceAll(List<String> list, String ... values) {
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
            list.set(i, ChatColor.translateAlternateColorCodes((char)'&', (String)list.get(i)));
        }
        return list;
    }
}

