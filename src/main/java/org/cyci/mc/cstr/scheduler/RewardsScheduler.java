package org.cyci.mc.cstr.scheduler;

import net.analyse.sdk.SDK;
import net.analyse.sdk.obj.AnalysePlayer;
import net.analyse.sdk.platform.Platform;
import net.analyse.sdk.request.response.PlayerProfile;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.data.DataMutateResult;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.InheritanceNode;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.cyci.mc.cstr.Registry;
import org.cyci.mc.cstr.config.Lang;
import org.cyci.mc.cstr.utils.C;

import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * @author - Phil
 * @project - CustomRewards
 * @website - https://cyci.org
 * @email - staff@cyci.org
 * @created Wed - 28/Jun/2023 - 8:19 PM
 */
public class RewardsScheduler extends BukkitRunnable {
    Registry plugin;
    SDK sdk;
    LuckPerms api;
    Platform platform;

    public RewardsScheduler(Registry plugin, SDK sdk, LuckPerms api, Platform platform) {
        this.plugin = plugin;
        this.sdk = sdk;
        this.api = api;
        this.platform = platform;
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            CompletableFuture<PlayerProfile> profileFuture = this.sdk.getPlayer(player.getUniqueId().toString());
            profileFuture.thenAcceptAsync(profile -> {
                int playerTotalSessionTime = profile.getTotalSessionTime();
                User role = this.api.getPlayerAdapter(Player.class).getUser(player);
                Player playerGet = Bukkit.getPlayer(profile.getUuid());
                if (playerGet == null || playerGet.hasPermission("emenbee.donor.rewards")) {
                    return;
                }
                if (playerTotalSessionTime >= 14400 && (role.getPrimaryGroup().equals("default") || !role.getPrimaryGroup().contains("member"))) {
                    givePlayerReward(playerGet, role, "Member");
                } else if (playerTotalSessionTime >= 259200 && ((role.getPrimaryGroup().equals("member") || role.getPrimaryGroup().equals("default")) && !role.getPrimaryGroup().contains("veteran"))) {
                    givePlayerReward(playerGet, role, "Veteran");
                }
            });
        }
    }
    private void givePlayerReward(Player player, User role, String reward) {
        InheritanceNode node = InheritanceNode.builder(reward).value(true).build();
        DataMutateResult result = role.data().add(node);
        this.api.getUserManager().saveUser(role);
        if (result.wasSuccessful()) {
            player.sendMessage(C.c(Lang.REWARD_GIVEN.getConfigValue(new String[]{reward, Lang.PREFIX.getConfigValue(null)})));
        }
    }
}

