package com.taylorswiftcn.megumi.pathfinding.algorithm.waypoint;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.taylorswiftcn.megumi.pathfinding.Main;
import com.taylorswiftcn.megumi.pathfinding.algorithm.waypoint.constructor.WorldMap;
import com.taylorswiftcn.megumi.pathfinding.util.MegumiUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class WayPointHandler {

    private static Main plugin = Main.getInstance();
    @Getter
    private static HashMap<String, WorldMap> worlds = new HashMap<>();

    @Getter private static HashMap<UUID, List<Hologram>> grams = new HashMap<>();
    @Getter private static HashMap<UUID, List<BukkitRunnable>> tasks = new HashMap<>();

    public static void init() {
        worlds.clear();

        File file = new File(plugin.getDataFolder(), "point");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            return;
        }

        if (!file.isDirectory()) return;

        FileFilter filter = pathname -> {
            String name = pathname.getName();
            return name.endsWith(".yml");
        };
        File[] childes = file.listFiles(filter);

        if (childes == null || childes.length == 0) return;

        for (File child : childes) {
            String name = child.getName().replace(".yml", "");
            if (Bukkit.getWorld(name) == null) continue;
            worlds.put(name, new WorldMap(child));
        }
    }

    public static void addWorld(String name) {
        File file = new File(plugin.getDataFolder(), String.format("point/%s.yml", name));

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            MegumiUtil.copyFile(plugin.getResource("points.yml"), file);
        }

        worlds.put(name, new WorldMap(file));
    }

    public static void close(Player player) {
        stop(player);
        clearHologram(player);
    }

    public static void addHologram(Player player, Hologram hologram) {
        List<Hologram> list = grams.getOrDefault(player.getUniqueId(), new ArrayList<>());
        list.add(hologram);

        grams.put(player.getUniqueId(), list);
    }

    private static void clearHologram(Player player) {
        if (!grams.containsKey(player.getUniqueId())) return;

        for (Hologram hologram : grams.get(player.getUniqueId())) {
            hologram.delete();
        }

        grams.remove(player.getUniqueId());
    }

    public static void addTask(Player player, BukkitRunnable run) {
        List<BukkitRunnable> list = tasks.getOrDefault(player.getUniqueId(), new ArrayList<>());
        list.add(run);

        tasks.put(player.getUniqueId(), list);
    }

    public static void stop(Player player) {
        tasks.get(player.getUniqueId()).forEach(BukkitRunnable::cancel);

        tasks.remove(player.getUniqueId());
    }
}
