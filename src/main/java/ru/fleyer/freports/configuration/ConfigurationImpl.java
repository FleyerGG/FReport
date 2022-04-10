/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.configuration.file.FileConfiguration
 */
package ru.fleyer.freports.configuration;

import org.bukkit.configuration.file.FileConfiguration;

public interface ConfigurationImpl {
    public FileConfiguration yaml();

    public FileConfiguration yamlLoad();

    public void save();

    public void reloadConfiguration();
}

