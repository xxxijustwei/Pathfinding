package com.taylorswiftcn.megumi.pathfinding.algorithm;

import com.taylorswiftcn.megumi.pathfinding.Main;
import com.taylorswiftcn.megumi.pathfinding.file.sub.ConfigFile;
import com.taylorswiftcn.megumi.pathfinding.util.special.EntityGlowUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class SearchPathManager {

    private static Main plugin = Main.getInstance();
    @Getter private static HashMap<UUID, BukkitRunnable> findTask = new HashMap<>();
    @Getter private static List<UUID> visual = new ArrayList<>();
    @Getter private static HashMap<UUID, Integer> particle = new HashMap<>();
    @Getter private static HashMap<UUID, Entity> glow = new HashMap<>();

    public static void switchVisual(Player player) {
        UUID uuid = player.getUniqueId();
        if (visual.contains(uuid)) {
            visual.remove(uuid);
            player.sendMessage(ConfigFile.Prefix + "§a可视化已关闭");
        }
        else {
            visual.add(uuid);
            player.sendMessage(ConfigFile.Prefix + "§a可视化已开启");
        }
    }

    public static void addDemoTaskID(Player player, int tid) {
        UUID uuid = player.getUniqueId();
        if (particle.containsKey(uuid)) {
            int id = particle.get(uuid);
            Bukkit.getScheduler().cancelTask(id);
        }

        particle.put(uuid, tid);
    }

    public static void cancelDemo(Player player) {
        UUID uuid = player.getUniqueId();
        if (!particle.containsKey(uuid)) return;
        int id = particle.get(uuid);
        Bukkit.getScheduler().cancelTask(id);
        if (glow.containsKey(uuid)) {
            EntityGlowUtil.unGlow(player, glow.get(uuid));
        }
        player.sendMessage(ConfigFile.Prefix + "§a已取消");
    }
}
