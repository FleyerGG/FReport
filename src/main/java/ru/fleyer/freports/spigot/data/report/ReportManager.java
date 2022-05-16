package ru.fleyer.freports.spigot.data.report;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import ru.fleyer.freports.FReports;

import java.sql.*;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentMap;

public class ReportManager {
    private static ConcurrentMap<String, Report> hash = Maps.newConcurrentMap();

    public static boolean addReport(String player, String sender, String reason) {
        Report report = hash.get(player);
        if (report == null) {
            long time = System.currentTimeMillis();
            report = new Report(player, Lists.newArrayList(new ReportInfo(sender, reason, time)));
            try {


                PreparedStatement queue = FReports.getInstance().getMySQL().getConnection().prepareStatement("INSERT INTO ReportInfo(username,sender,reason,time) VALUES (?,?,?,?)");
                PreparedStatement queue1 = FReports.getInstance().getMySQL().getConnection().prepareStatement("INSERT INTO Report(username) VALUES (?)");

                queue.setString(1, player);
                queue.setString(2, sender);
                queue.setString(3, reason);
                queue.setString(4, String.valueOf(time));
                queue.execute();

                queue1.setString(1, player);
                queue1.execute();
                hash.put(player, report);
                return true;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                return false;
            }


        } else {
            long time = System.currentTimeMillis();
            report.add(sender, reason, time);
            try {

                PreparedStatement queue = FReports.getInstance().getMySQL().getConnection().prepareStatement("INSERT INTO ReportInfo(username,sender,reason,time) VALUES (?,?,?,?)");
                queue.setString(1, player);
                queue.setString(2, sender);
                queue.setString(3, reason);
                queue.setString(4, String.valueOf(time));
                queue.execute();
                hash.put(player, report);
                return true;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                return false;
            }


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

