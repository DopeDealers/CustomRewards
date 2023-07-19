package org.cyci.mc.cstr.utils;

import java.math.BigDecimal;

public class Util {

    public static String splitToComponentTimes(BigDecimal biggy)
    {
        long longVal = biggy.longValue();
        int hours = (int) longVal / 3600;
        int remainder = (int) longVal - hours * 3600;
        int mins = remainder / 60;
        remainder = remainder - mins * 60;
        int secs = remainder;
        String total = "";
        if (hours > 0) total = total.concat(hours +"hr ");
        if (mins > 0) total = total.concat(mins + "m ");
        if (secs > 0) total = total.concat(secs + "s");

        return total;
    }
}
