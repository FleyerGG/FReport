/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.plugin.Plugin
 */
package ru.fleyer.freports.configuration;

import java.io.File;
import java.io.IOException;
import ru.fleyer.freports.configuration.ConfigurationImpl;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class ConfigurationGeneration implements ConfigurationImpl {
    private FileConfiguration config;
    private String fileName;
    private Plugin plugin;

    public ConfigurationGeneration(Plugin plugin, String fileName) {
        this.plugin = plugin;
        this.fileName = fileName;
        this.config = this.yamlLoad();
    }

    @Override
    public FileConfiguration yaml() {
        return this.config;
    }

    @Override
    public FileConfiguration yamlLoad() {
        File file = new File(this.plugin.getDataFolder(), this.fileName);
        if (this.plugin.getResource(this.fileName) == null) {
            try {
                YamlConfiguration.loadConfiguration((File)file).save(file);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return YamlConfiguration.loadConfiguration((File)file);
        }
        if (!file.exists()) {
            this.plugin.saveResource(this.fileName, false);
        }
        return YamlConfiguration.loadConfiguration((File)file);
    }

    @Override
    public void save() {
        try {
            this.config.save(this.fileName);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void reloadConfiguration() {
        this.config = this.yamlLoad();
    }
}

