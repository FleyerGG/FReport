/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.google.common.collect.TreeBasedTable
 */
package ru.fleyer.freports.cooldown;

import com.google.common.collect.TreeBasedTable;

public class CooldownManager {
    public static TreeBasedTable<String, String, Cooldown> accs = TreeBasedTable.create();

    public static void createCooldown(String playerName, String name, long time) {
        Cooldown c = new Cooldown(playerName.toLowerCase(), name, time);
        accs.put(playerName.toLowerCase(), name, c);
    }

    public static boolean hasCdw(String playerName, String name) {
        Cooldown c = (Cooldown) accs.get((Object) playerName.toLowerCase(), (Object) name);
        if (c == null) {
            return false;
        }
        if (c.isLeft()) {
            accs.remove((Object) playerName.toLowerCase(), (Object) name);
            return false;
        }
        return true;
    }

    public static long getLeftTime(String playerName, String name) {
        return ((Cooldown) accs.get((Object) playerName.toLowerCase(), (Object) name)).getLeftTime();
    }
}

