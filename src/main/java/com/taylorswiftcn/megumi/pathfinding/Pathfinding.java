package com.taylorswiftcn.megumi.pathfinding;

import com.taylorswiftcn.justwei.util.MegumiUtil;
import com.taylorswiftcn.megumi.pathfinding.commands.MainCommand;
import com.taylorswiftcn.megumi.pathfinding.file.FileManager;
import com.taylorswiftcn.megumi.pathfinding.file.sub.ConfigFile;
import com.taylorswiftcn.megumi.pathfinding.listener.DemoListener;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class Pathfinding extends JavaPlugin {
    @Getter private static Pathfinding instance;

    @Getter private FileManager fileManager;

    @Getter private HashMap<String, Location> coord;

    public static boolean dragonCore;
    public static boolean citizens;
    public static boolean hologram;

    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();

        instance = this;

        Bukkit.getConsoleSender().sendMessage("###########################");

        coord = new HashMap<>();

        fileManager = new FileManager(this);
        fileManager.init();

        initHook();

        getCommand("pf").setExecutor(new MainCommand());
        Bukkit.getPluginManager().registerEvents(new DemoListener(), this);

        long end = System.currentTimeMillis();

        Bukkit.getConsoleSender().sendMessage("§3加载成功! 用时 %time% ms".replace("%time%", String.valueOf(end - start)));
        Bukkit.getConsoleSender().sendMessage("§e作者: justwei");
        Bukkit.getConsoleSender().sendMessage("###########################");
    }

    @Override
    public void onDisable() {
        getLogger().info("卸载成功!");
    }

    public String getVersion() {
        String packet = Bukkit.getServer().getClass().getPackage().getName();
        return packet.substring(packet.lastIndexOf('.') + 1);
    }

    private void initHook() {
        dragonCore = ConfigFile.Enable.dragonCore && Bukkit.getPluginManager().getPlugin("DragonCore") != null;
        citizens = Bukkit.getPluginManager().getPlugin("Citizens") != null;
        hologram = Bukkit.getPluginManager().getPlugin("HolographicDisplays") != null;

        Bukkit.getConsoleSender().sendMessage("§3插件 DragonCore Hook: " + dragonCore);
        Bukkit.getConsoleSender().sendMessage("§3插件 Citizens Hook: " + citizens);
        Bukkit.getConsoleSender().sendMessage("§3插件 HolographicDisplays Hook: " + hologram);
    }

    public void reload() {
        coord.clear();

        fileManager.init();
        dragonCore = ConfigFile.Enable.dragonCore && Bukkit.getPluginManager().getPlugin("DragonCore") != null;
    }
}
