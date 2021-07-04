package com.taylorswiftcn.megumi.pathfinding.file.sub;

import com.taylorswiftcn.megumi.pathfinding.Main;
import com.taylorswiftcn.megumi.pathfinding.util.MegumiUtil;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;

public class ConfigFile {
    private static YamlConfiguration config;

    public static String Prefix;

    public static Boolean dragonCore;
    public static Boolean debug;

    public static Integer model;

    public static String navigationBar;
    public static String hint;
    public static String title_theme;
    public static String title_sub;

    public static void init() {
        config = Main.getInstance().getFileManager().getConfig();

        Prefix = getString("Prefix");
        dragonCore = config.getBoolean("DragonCore");
        debug = config.getBoolean("Debug");
        model = config.getInt("Model");
        navigationBar = getString("NavigationBar");
        hint = getString("Hint");
        title_theme = getString("Title.Theme");
        title_sub = getString("Title.Sub");
    }

    private static String getString(String path) {
        return MegumiUtil.onReplace(config.getString(path));
    }

    private static List<String> getStringList(String path) {
        return MegumiUtil.onReplace(config.getStringList(path));
    }
}
