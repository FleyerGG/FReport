/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 */
package ru.fleyer.freports.report;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import ru.fleyer.freports.FReports;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentMap;

public class ReportManager {
    private static ConcurrentMap<String, Report> hash = Maps.newConcurrentMap();

    public static void addReport(String player, String sender, String reason) {
        Report report = hash.get(player);
        if (report == null) {
            long time = System.currentTimeMillis();
            report = new Report(player, Lists.newArrayList(new ReportInfo(sender, reason, time)));
            FReports.getInstance().getMySQL().execute("INSERT INTO `ReportInfo` (`username`, `sender`, `reason`, `time`) VALUES ('" + player + "','" + sender + "','" + reason + "','" + time + "')");
            FReports.getInstance().getMySQL().execute("INSERT INTO `Report` (`username`) VALUES ('" + player + "')");
            hash.put(player, report);
        } else {
            long time = System.currentTimeMillis();
            report.add(sender, reason, time);
            FReports.getInstance().getMySQL().execute("INSERT INTO `ReportInfo` (`username`, `sender`, `reason`, `time`) VALUES ('" + player + "','" + sender + "','" + reason + "','" + time + "')");
            hash.put(player, report);
        }
    }

    public static void addHash(String player, ArrayList<ReportInfo> list) {
        hash.put(player, new Report(player, list));
    }

    public static void removeReport(String player) {
        FReports.getInstance().getMySQL().execute("DELETE FROM `Report` WHERE `username`='" + player + "'");
        FReports.getInstance().getMySQL().execute("DELETE FROM `ReportInfo` WHERE `username`='" + player + "'");
        hash.remove(player);
    }

    public static ConcurrentMap<String, Report> getHash() {
        return hash;
    }

    public static Report getReport(String player) {
        return hash.get(player);
    }
}

