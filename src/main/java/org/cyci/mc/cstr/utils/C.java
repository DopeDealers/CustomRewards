package org.cyci.mc.cstr.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

/**
 * @author - Phil
 * @project - CustomRewards
 * @website - https://cyci.org
 * @email - staff@cyci.org
 * @created Wed - 28/Jun/2023 - 8:41 PM
 */
public class C {
    public static Component c(String input) {
        return LegacyComponentSerializer.legacy('&').deserialize(input);
    }
}
