package ru.fleyer.freports.spigot.resource.configuration;

import org.bukkit.configuration.file.FileConfiguration;

public interface ConfigurationImpl {
    public FileConfiguration yaml();
    public FileConfiguration msg();

    public FileConfiguration yamlLoad();

    public void save();

    public void reloadConfiguration();
}

