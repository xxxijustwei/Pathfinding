package com.taylorswiftcn.megumi.pathfinding.file;

import com.taylorswiftcn.justwei.file.JustConfiguration;
import com.taylorswiftcn.megumi.pathfinding.file.sub.ConfigFile;
import com.taylorswiftcn.megumi.pathfinding.file.sub.MessageFile;
import com.taylorswiftcn.megumi.pathfinding.Pathfinding;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class FileManager extends JustConfiguration {
    private Pathfinding plugin;
    @Getter private YamlConfiguration config;
    @Getter private YamlConfiguration message;
    @Getter private YamlConfiguration coord;

    public FileManager(Pathfinding plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    public void init() {
        config = initFile("config.yml");
        message = initFile("message.yml");
        coord = initFile("coord.yml");

        ConfigFile.init();
        MessageFile.init();
        load();
    }

    public void load() {
        ConfigurationSection section = plugin.getFileManager().getCoord().getConfigurationSection("");
        if (section == null) return;
        for (String s : section.getKeys(false)) {
            String world = section.getString(s + ".World");
            double x = section.getDouble(s + ".X");
            double y = section.getDouble(s + ".Y");
            double z = section.getDouble(s + ".Z");

            plugin.getCoord().put(s, new Location(Bukkit.getWorld(world), x, y, z));
        }
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
