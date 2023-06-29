package org.cyci.mc.cstr;

import net.analyse.sdk.Analyse;
import net.analyse.sdk.SDK;
import net.analyse.sdk.platform.Platform;
import net.analyse.sdk.request.response.PlayerProfile;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.cyci.mc.cstr.config.ConfigWrapper;
import org.cyci.mc.cstr.config.Lang;
import org.cyci.mc.cstr.scheduler.RewardsScheduler;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class Registry extends JavaPlugin {

    Registry plugin;
    private Platform platform;
    private SDK sdk;

    LuckPerms api;
    public ConfigWrapper messagesFile = new ConfigWrapper(this, "/", "messages.yml");

    public Registry() {
        this.plugin = this;
    }

    @Override
    public void onEnable() {
            // If Analyse is not enabled, disable the plugin.
            if(!Bukkit.getPluginManager().isPluginEnabled("Analyse")) {
                getLogger().severe("Analyse is not enabled!");
                Bukkit.getPluginManager().disablePlugin(this);
                return;
            }
            RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
            if (provider != null) {
                this.api = provider.getProvider();
            }
            // Get the platform instance.
            platform = Analyse.get();
            sdk = platform.getSDK();
            loadMessages();
            new RewardsScheduler(this.plugin, this.sdk, this.api).runTaskTimerAsynchronously(this.plugin, 600, 600);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        
    }
    private void loadMessages() {
        Lang.setFile(this.messagesFile.getConfig());
        for (Lang value : Lang.values())
            this.messagesFile.getConfig().addDefault(value.getPath(), value.getDefault());
        this.messagesFile.getConfig().options().copyDefaults(true);
        this.messagesFile.saveConfig();
    }

}