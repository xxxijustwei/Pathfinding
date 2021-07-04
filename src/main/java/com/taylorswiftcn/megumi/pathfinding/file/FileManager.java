package com.taylorswiftcn.megumi.pathfinding.file;

import com.taylorswiftcn.megumi.pathfinding.file.sub.ConfigFile;
import com.taylorswiftcn.megumi.pathfinding.file.sub.MessageFile;
import com.taylorswiftcn.megumi.pathfinding.util.MegumiUtil;
import com.taylorswiftcn.megumi.pathfinding.Main;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class FileManager {
    private Main plugin;
    @Getter private YamlConfiguration config;
    @Getter private YamlConfiguration message;
    @Getter private YamlConfiguration coord;

    public FileManager(Main plugin) {
        this.plugin = plugin;
    }

    public void init() {
        config = initFile("config.yml");
        message = initFile("message.yml");
        coord = initFile("coord.yml");

        ConfigFile.init();
        MessageFile.init();
    }

    private YamlConfiguration initFile(String name) {
        File file = new File(plugin.getDataFolder(), name);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            MegumiUtil.copyFile(plugin.getResource(name), file);
            MegumiUtil.log(String.format("§3File: 已生成 %s 文件", name));
        }
        else MegumiUtil.log(String.format("§3File: 已加载 %s 文件", name));
        return YamlConfiguration.loadConfiguration(file);
    }

    public void add(String id, Location location) {
        coord.set(id + ".World", location.getWorld().getName());
        coord.set(id + ".X", location.getX());
        coord.set(id + ".Y", location.getY());
        coord.set(id + ".Z", location.getZ());
        save();
    }

    public void del(String id) {
        coord.set(id, null);
        save();
    }

    private void save() {
        File file = new File(plugin.getDataFolder(), "coord.yml");
        try {
            coord.save(file);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
