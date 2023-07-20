package org.cyci.mc.cstr.cmd;

import net.analyse.sdk.SDK;
import net.analyse.sdk.platform.Platform;
import net.analyse.sdk.request.response.PlayerProfile;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.cyci.mc.cstr.Registry;
import org.cyci.mc.cstr.cmd.api.CommandListener;
import org.cyci.mc.cstr.config.Lang;
import org.cyci.mc.cstr.utils.C;
import org.cyci.mc.cstr.utils.Util;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

public class PlayTimeCmd extends CommandListener {


    public PlayTimeCmd() {
        super("playtime", "");
    }

    // This method is called, when somebody uses our command
    @Override
    public void execute(final CommandSender sender, final String[] args) {
        if (!(sender instanceof Player)) return;

        if (args.length == 0) {
            CompletableFuture<PlayerProfile> profileFuture = Registry.getSdk().getPlayer(((Player) sender).getUniqueId().toString());
            profileFuture.thenAcceptAsync(profile -> {
                int playerTotalSessionTime = profile.getTotalSessionTime();
                String timerFormat = Util.splitToComponentTimes(BigDecimal.valueOf(playerTotalSessionTime));
                sender.sendMessage(C.c(Lang.PLAY_TIME.getConfigValue(new String[]{timerFormat, Lang.PREFIX.getConfigValue(null), sender.getName()})));
            });
        } else if (args.length != 0 && sender.hasPermission("emenbee.mod.playertime")) {
            OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
            if (player != null) {
                CompletableFuture<PlayerProfile> profileFuture = Registry.getSdk().getPlayer(player.getUniqueId().toString());
                profileFuture.thenAcceptAsync(profile -> {
                    int playerTotalSessionTime = profile.getTotalSessionTime();
                    String timerFormat = Util.splitToComponentTimes(BigDecimal.valueOf(playerTotalSessionTime));
                    sender.sendMessage(C.c(Lang.PLAY_TIME.getConfigValue(new String[]{timerFormat, Lang.PREFIX.getConfigValue(null),  player.getName()})));
                });
            }
        }
    }
}