package org.cyci.mc.cstr.scheduler;

import net.analyse.sdk.SDK;
import net.analyse.sdk.request.response.PlayerProfile;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.data.DataMutateResult;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.types.InheritanceNode;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.cyci.mc.cstr.Registry;
import org.cyci.mc.cstr.config.Lang;
import org.cyci.mc.cstr.utils.C;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
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
    

    public RewardsScheduler(Registry plugin, SDK sdk, LuckPerms api) {
        this.plugin = plugin;
        this.sdk = sdk;
        this.api = api;
    }

    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            CompletableFuture<PlayerProfile> profileFuture = this.sdk.getPlayer(player.getUniqueId().toString());
            profileFuture.thenAccept(profile -> {
                int playerTotalSessionTime = profile.getTotalSessionTime();
                List<PlayerProfile.Statistic> playerStatistics = profile.getStatistics();
                User role = this.api.getPlayerAdapter(Player.class).getUser(player);
                //User role = Objects.requireNonNull(this.api.getUserManager().getUser(profile.getUuid()));
                Player playerGet = Bukkit.getPlayer(profile.getUuid());
                if (playerTotalSessionTime >= 14400 && role.getPrimaryGroup().equals("default") && !role.getPrimaryGroup().contains("member")) {
                    InheritanceNode node = InheritanceNode.builder("Member").value(true).build();
                    DataMutateResult result = role.data().add(node);
                    this.api.getUserManager().saveUser(role);
                    if (result.wasSuccessful()) {
                        assert playerGet != null;
                        playerGet.sendMessage(C.c(Lang.REWARD_GIVEN.getConfigValue(new String[]{"Member", Lang.PREFIX.getConfigValue(null)})));
                    }
                } else if (playerTotalSessionTime >= 259200 && role.getPrimaryGroup().equals("member") && !role.getPrimaryGroup().contains("veteran")) {
                    InheritanceNode node = InheritanceNode.builder("Veteran").value(true).build();
                    DataMutateResult result = role.data().add(node);
                    this.api.getUserManager().saveUser(role);
                    if (result.wasSuccessful()) {
                        assert playerGet != null;
                        playerGet.sendMessage(C.c(Lang.REWARD_GIVEN.getConfigValue(new String[]{"Veteran", Lang.PREFIX.getConfigValue(null)})));
                    }
                } else return;
            });
        });
    }
}
