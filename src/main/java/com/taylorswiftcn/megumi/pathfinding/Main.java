package com.taylorswiftcn.megumi.pathfinding;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.taylorswiftcn.megumi.pathfinding.commands.MainCommand;
import com.taylorswiftcn.megumi.pathfinding.file.FileManager;
import com.taylorswiftcn.megumi.pathfinding.file.sub.ConfigFile;
import com.taylorswiftcn.megumi.pathfinding.util.MegumiUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class Main extends JavaPlugin {
    @Getter private static Main instance;

    @Getter private FileManager fileManager;

    @Getter private ProtocolManager pm;

    @Getter private HashMap<String, Location> coord;

    public static boolean dragonCore;

    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();

        instance = this;

        MegumiUtil.log("###########################");

        pm = ProtocolLibrary.getProtocolManager();
        coord = new HashMap<>();

        fileManager = new FileManager(this);
        fileManager.init();

        load();

        dragonCore = ConfigFile.dragonCore && Bukkit.getPluginManager().getPlugin("DragonCore") != null;

        MegumiUtil.log(String.format("§3插件 DragonCore Hook: %s", dragonCore));

        /*WayPointHandler.init();*/

        getCommand("pf").setExecutor(new MainCommand());
        /*Bukkit.getPluginManager().registerEvents(new DemoListener(), this);*/

        long end = System.currentTimeMillis();

        MegumiUtil.log("§3加载成功! 用时 %time% ms".replace("%time%", String.valueOf(end - start)));
        MegumiUtil.log("§e作者: Megumi");
        MegumiUtil.log("§eQQ: 17328768735");
        MegumiUtil.log("###########################");
    }

    @Override
    public void onDisable() {
        getLogger().info("卸载成功!");
    }

    public String getVersion() {
        String packet = Bukkit.getServer().getClass().getPackage().getName();
        return packet.substring(packet.lastIndexOf('.') + 1);
    }

    public void reload() {
        coord.clear();

        fileManager.init();
        load();
    }

    public void load() {
        ConfigurationSection section = fileManager.getCoord().getConfigurationSection("");
        if (section == null) return;
        for (String s : section.getKeys(false)) {
            String world = section.getString(s + ".World");
            double x = section.getDouble(s + ".X");
            double y = section.getDouble(s + ".Y");
            double z = section.getDouble(s + ".Z");

            coord.put(s, new Location(Bukkit.getWorld(world), x, y, z));
        }
    }
}
