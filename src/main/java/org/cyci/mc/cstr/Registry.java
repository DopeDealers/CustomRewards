package org.cyci.mc.cstr;

import net.analyse.sdk.Analyse;
import net.analyse.sdk.SDK;
import net.analyse.sdk.platform.Platform;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.cyci.mc.cstr.cmd.api.CommandListener;
import org.cyci.mc.cstr.cmd.api.CommandRegistry;
import org.cyci.mc.cstr.config.ConfigWrapper;
import org.cyci.mc.cstr.config.Lang;
import org.cyci.mc.cstr.scheduler.RewardsScheduler;
import org.cyci.mc.cstr.utils.C;

public final class Registry extends JavaPlugin {

    private static Registry plugin;
    private Platform platform;
    private static SDK sdk;

    LuckPerms api;
    public ConfigWrapper messagesFile = new ConfigWrapper(this, "/", "messages.yml");

    public Registry() {
        this.plugin = this;
    }

    public static Registry getInstance() {
        return Registry.plugin;
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
            this.platform = Analyse.get();
            sdk = platform.getSDK();
            new CommandRegistry();
            // load messages for the config
            loadMessages();
            // Activate rewards Scheduler.
            new RewardsScheduler(this.plugin, this.sdk, this.api, this.platform).runTaskTimerAsynchronously(this.plugin, 600, 600);
    }

    @Override
    public void onDisable() {
    }
    private void loadMessages() {
        Lang.setFile(this.messagesFile.getConfig());
        for (Lang value : Lang.values())
            this.messagesFile.getConfig().addDefault(value.getPath(), value.getDefault());
        this.messagesFile.getConfig().options().copyDefaults(true);
        this.messagesFile.saveConfig();
    }
    public boolean onCommand(final CommandSender s, final Command c, final String string, final String[] args) {
        final CommandListener command = CommandRegistry.getCommand(c.getName());
        if (command == null) {
            return false;
        }
        if (s instanceof Player) {
            final Player player = (Player)s;
            if (command.getPermission().equals("") || player.hasPermission(command.getPermission())) {
                command.execute(s, args);
            }
            else {
                player.sendMessage(C.c(Lang.NO_PERM.getConfigValue( new String[] { command.getPermission(), Lang.PREFIX.getConfigValue(null)} )));
            }
            return true;
        }
        command.execute(s, args);
        return true;
    }
    public static SDK getSdk() {
        return sdk;
    }

}