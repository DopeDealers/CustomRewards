package org.cyci.mc.cstr.utils;

import net.analyse.sdk.request.response.PlayerProfile;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.cyci.mc.cstr.Registry;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

public class Util {

    public static String splitToComponentTimes(BigDecimal biggy) {
        long longVal = biggy.longValue();
        int hours = (int) longVal / 3600;
        int remainder = (int) longVal - hours * 3600;
        int mins = remainder / 60;
        remainder = remainder - mins * 60;
        int secs = remainder;
        String total = "";
        if (hours > 0) total = total.concat(hours + "hr ");
        if (mins > 0) total = total.concat(mins + "m ");
        if (secs > 0) total = total.concat(secs + "s");

        return total;
    }
}
