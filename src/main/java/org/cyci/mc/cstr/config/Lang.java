package org.cyci.mc.cstr.config;

import org.bukkit.configuration.file.FileConfiguration;

/**
 * @author - Phil
 * @project - CustomRewards
 * @website - https://cyci.org
 * @email - staff@cyci.org
 * @created Wed - 28/Jun/2023 - 8:44 PM
 */
public enum Lang {
    PREFIX("message.prefix", "&b\u00a7b\u00a7o\uff25\uff2d \u00a7e\u00a7l\u003e"),
    REWARD_GIVEN("message.reward", "{1} &7you have been given the {0} rank");

    private final String path;

    private final String def;

    private static FileConfiguration LANG;

    Lang(String path, String start) {
        this.path = path;
        this.def = start;
    }

    public static void setFile(FileConfiguration config) {
        LANG = config;
    }

    public String getDefault() {
        return this.def;
    }

    public String getPath() {
        return this.path;
    }
    public String setConfigValue(String args) {
        String value = Lang.LANG.getString(this.path, this.def);
        value = value.replace(this.def, args);
        return value;
    }

    public String getConfigValue(String[] args) {
        String value = LANG.getString(this.path, this.def);
        if (args == null)
            return value;
        if (args.length == 0)
            return value;
        for (int i = 0; i < args.length; i++)
            value = value.replace("{" + i + "}", args[i]);
        return value;
    }
    public String getValue() {
        return LANG.getString(this.path, this.def);
    }
    public boolean getBoolean() {
        return LANG.getBoolean(this.path, Boolean.parseBoolean(this.def));
    }

    public int getInt() {
        return LANG.getInt(this.path, Integer.parseInt(this.def));
    }
}
