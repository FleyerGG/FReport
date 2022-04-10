/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  org.bukkit.Bukkit
 *  org.bukkit.plugin.Plugin
 */
package ru.fleyer.freports.mysql;

import com.google.common.collect.Lists;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import ru.fleyer.freports.FReports;
import ru.fleyer.freports.report.ReportInfo;
import ru.fleyer.freports.report.ReportManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class MySQL {
    private String host;
    private String username;
    private String password;
    private String database;
    private Connection connection;
    private String port;

    public MySQL(String host, String username, String password, String database, String port) {
        this.host = host;
        this.username = username;
        this.password = password;
        this.database = database;
        this.port = port;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        }
        catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        this.execute("CREATE TABLE IF NOT EXISTS `Report` (`id` int NOT NULL AUTO_INCREMENT, `username` varchar(255) NOT NULL, PRIMARY KEY (`id`)) DEFAULT CHARSET=utf8 COLLATE utf8_bin AUTO_INCREMENT=0");
        this.execute("CREATE TABLE IF NOT EXISTS `ReportInfo` (`id` int NOT NULL AUTO_INCREMENT, `username` varchar(255) NOT NULL, `sender` varchar(255) NOT NULL, `reason` varchar(255) NOT NULL, `time` LONG NOT NULL, PRIMARY KEY (`id`)) DEFAULT CHARSET=utf8 COLLATE utf8_bin AUTO_INCREMENT=0");
    }

    public Connection getConnection() {
        try {
            if (this.connection != null && !this.connection.isClosed() && this.connection.isValid(20)) {
                return this.connection;
            }
            Properties p = new Properties();
            p.setProperty("user", this.username);
            p.setProperty("password", this.password);
            p.setProperty("useUnicode", "true");
            p.setProperty("characterEncoding", "cp1251");
            p.setProperty("autoReconnect", "true");
            this.connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, p);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return this.connection;
    }

    public void execute(String query) {
        try {
            PreparedStatement statement = this.getConnection().prepareStatement(query);
            statement.execute();
            statement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        Bukkit.getScheduler().runTaskTimerAsynchronously((Plugin)FReports.getInstance(), () -> {
            try {
                ReportManager.getHash().clear();
                ResultSet resultSet = FReports.getInstance().getMySQL().getConnection().createStatement().executeQuery("SELECT * FROM `Report`");
                while (resultSet.next()) {
                    ArrayList list = Lists.newArrayList();
                    ResultSet rs = FReports.getInstance().getMySQL().getConnection().createStatement().executeQuery("SELECT * FROM `ReportInfo` WHERE `username`='" + resultSet.getString("username") + "'");
                    while (rs.next()) {
                        list.add(new ReportInfo(rs.getString("sender"), rs.getString("reason"), rs.getLong("time")));
                    }
                    ReportManager.addHash(resultSet.getString("username"), list);
                    rs.close();
                }
                resultSet.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }, 0L, 60L);
    }

    public void disconnect() {
        try {
            this.getConnection().close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

