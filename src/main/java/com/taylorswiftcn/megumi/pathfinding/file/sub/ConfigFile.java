package com.taylorswiftcn.megumi.pathfinding.file.sub;

import com.taylorswiftcn.justwei.util.MegumiUtil;
import com.taylorswiftcn.megumi.pathfinding.Pathfinding;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;

public class ConfigFile {
    private static YamlConfiguration config;

    public static String Prefix;

    public static class Enable {
        public static Boolean dragonCore;
        public static Boolean debug;
        public static Boolean actionBar;
        public static Boolean hologram;
        public static Boolean title;
        public static Boolean autoMode;
    }

    public static class Base {
        public static Integer mode;
        public static Integer openNodeCount;
        public static Integer autoDistance;
    }

    public static class Tips {
        public static String actionBar;
        public static List<String> hologram;
        public static String title_theme;
        public static String title_sub;
    }

    public static void init() {
        config = Pathfinding.getInstance().getFileManager().getConfig();

        Prefix = getString("Prefix");
        Enable.dragonCore = config.getBoolean("Enable.DragonCore");
        Enable.debug = config.getBoolean("Enable.Debug");
        Enable.actionBar = config.getBoolean("Enable.ActionBar");
        Enable.hologram = config.getBoolean("Enable.Hologram");
        Enable.title = config.getBoolean("Enable.Title");
        Enable.autoMode = config.getBoolean("Enable.AutoMode");
        Base.mode = config.getInt("Base.Mode");
        Base.openNodeCount = config.getInt("Base.OpenNodeLimit");
        Base.autoDistance = config.getInt("Base.AutoDistance");
        Tips.actionBar = getString("Tips.ActionBar");
        Tips.hologram = getStringList("Tips.Hologram");
        Tips.title_theme = getString("Tips.Title.Theme");
        Tips.title_sub = getString("Tips.Title.Sub");
    }

    private static String getString(String path) {
        return MegumiUtil.onReplace(config.getString(path));
    }

    private static List<String> getStringList(String path) {
        return MegumiUtil.onReplace(config.getStringList(path));
    }
}
