package org.cyci.mc.cstr.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.logging.Level;

/**
 * @author - Phil
 * @project - CustomRewards
 * @website - https://cyci.org
 * @email - phillip.kinney@cyci.org
 * @created Wednesday - 6/june/2023 - 8:44 PM
 */
public class ConfigWrapper {
    private final JavaPlugin plugin;

    private FileConfiguration config;

    private File configFile;

    private final String folderName;

    private final String fileName;

    public ConfigWrapper(JavaPlugin instance, String folderName, String fileName) {
        this.plugin = instance;
        this.folderName = folderName;
        this.fileName = fileName;
    }

    public void createNewFile(String message, String header) {
        reloadConfig();
        saveConfig();
        loadConfig(header);
        if (message != null)
            this.plugin.getLogger().info(message);
    }

    public FileConfiguration getConfig() {
        if (this.config == null)
            reloadConfig();
        return this.config;
    }

    public void loadConfig(String header) {
        this.config.options().setHeader(Collections.singletonList(header));
        this.config.options().copyDefaults(true);
        saveConfig();
    }

    public void reloadConfig() {
        if (this.configFile == null)
            this.configFile = new File(this.plugin.getDataFolder() + this.folderName, this.fileName);
        this.config = (FileConfiguration) YamlConfiguration.loadConfiguration(this.configFile);
    }

    public void saveConfig() {
        if (this.config == null || this.configFile == null)
            return;
        try {
            getConfig().save(this.configFile);
        } catch (IOException ex) {
            this.plugin.getLogger().log(Level.SEVERE, "Could not save config to " + this.configFile, ex);
        }
    }

}
