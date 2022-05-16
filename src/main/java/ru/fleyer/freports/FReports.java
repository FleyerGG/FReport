package ru.fleyer.freports;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import ru.fleyer.freports.spigot.resource.commands.ReloadConfigs;
import ru.fleyer.freports.spigot.resource.commands.ReportCommand;
import ru.fleyer.freports.spigot.resource.commands.ReportsCommand;
import ru.fleyer.freports.spigot.resource.configuration.ConfigurationGeneration;
import ru.fleyer.freports.spigot.data.mysql.MySQL;
import ru.fleyer.freports.spigot.data.report.ReportManager;

public class FReports extends JavaPlugin {
    private static FReports instance;
    private ConfigurationGeneration config;
    private ConfigurationGeneration lang;
    private MySQL mysql;

    public void onEnable() {
        instance = this;
        instance.saveDefaultConfig();
        config = new ConfigurationGeneration(FReports.getInstance(), "config.yml");
        lang = new ConfigurationGeneration(FReports.getInstance(), "lang.yml");

        mysql = new MySQL(config.yaml().getString("mysql.host"),
                config.yaml().getString("mysql.username"),
                config.yaml().getString("mysql.password"),
                config.yaml().getString("mysql.database"),
                config.yaml().getString("mysql.port"));
        mysql.update();

        new HandlerRequestBungee().sendList();

        getCommand("frl").setExecutor(new ReloadConfigs());
        getCommand("report").setExecutor(new ReportCommand());
        getCommand("reports").setExecutor(new ReportsCommand());

        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        Bukkit.getMessenger().registerIncomingPluginChannel(this, "namespace:nreports_network", new HandlerRequestBungee());

    }

    public void onDisable() {
        ReportManager.getHash().clear();
        mysql.disconnect();
    }

    public MySQL getMySQL() {
        return mysql;
    }

    public static FReports getInstance() {
        return instance;
    }

    public String getMessage(String message) {
        return ChatColor.translateAlternateColorCodes('&', lang.msg().getString("messages." + message));
    }

    public ConfigurationGeneration config() {
        return config;
    }

    public ConfigurationGeneration lang() {
        return lang;
    }

}