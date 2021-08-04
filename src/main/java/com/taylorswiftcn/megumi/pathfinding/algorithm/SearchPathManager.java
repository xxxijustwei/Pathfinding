package com.taylorswiftcn.megumi.pathfinding.algorithm;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import com.taylorswiftcn.megumi.pathfinding.Main;
import com.taylorswiftcn.megumi.pathfinding.file.sub.ConfigFile;
import com.taylorswiftcn.megumi.pathfinding.util.message.ActionBarUtil;
import com.taylorswiftcn.megumi.pathfinding.util.special.EntityGlowUtil;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class SearchPathManager {

    private static Main plugin = Main.getInstance();
    @Getter
    private static HashMap<UUID, BukkitRunnable> findTask = new HashMap<>();
    @Getter private static List<UUID> visual = new ArrayList<>();
    @Getter private static HashMap<UUID, BukkitRunnable> demo = new HashMap<>();
    @Getter private static HashMap<UUID, Entity> glow = new HashMap<>();
    @Getter private static HashMap<UUID, Hologram> holograms = new HashMap<>();

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

    public static void addDemoTaskID(Player player, BukkitRunnable task) {
        UUID uuid = player.getUniqueId();
        if (demo.containsKey(uuid)) {
            BukkitRunnable current = demo.get(uuid);
            current.cancel();
        }

        demo.put(uuid, task);
    }

    public static void cancelDemo(Player player) {
        UUID uuid = player.getUniqueId();
        if (!demo.containsKey(uuid)) return;
        BukkitRunnable task = demo.get(uuid);
        task.cancel();
        demo.remove(uuid);
        if (glow.containsKey(uuid)) {
            EntityGlowUtil.unGlow(player, glow.get(uuid));
        }
        player.sendMessage(ConfigFile.Prefix + "§a已取消");
    }

    public static void sendNavTitle(Player player) {
        if (!ConfigFile.Enable.title) return;
        player.sendTitle(ConfigFile.Tips.title_theme, ConfigFile.Tips.title_sub, 10, 20, 10);
    }

    public static void sendNavActionBar(Player player, int distance) {
        if (!ConfigFile.Enable.actionBar) return;
        ActionBarUtil.sendActionBar(player, ConfigFile.Tips.actionBar.replace("%s%", String.valueOf(distance)));
    }

    public static void createHologram(Player player, Location target) {
        if (!(ConfigFile.Enable.hologram && Main.hologram)) return;

        UUID uuid = player.getUniqueId();

        new BukkitRunnable() {
            @Override
            public void run() {
                Hologram hologram = HologramsAPI.createHologram(Main.getInstance(), target.clone().add(0, 1.6, 0));
                for (String s : ConfigFile.Tips.hologram) {
                    if (s.startsWith("[item]")) {
                        String id = StringUtils.replace(s, "[item]", "", 1);
                        hologram.appendItemLine(new ItemStack(Material.getMaterial(id)));
                        continue;
                    }
                    if (s.startsWith("[text]")) {
                        String text = StringUtils.replace(s, "[text]", "", 1);
                        BigDecimal dis = BigDecimal.valueOf(player.getLocation().distance(target)).setScale(0, BigDecimal.ROUND_DOWN);
                        hologram.appendTextLine(StringUtils.replace(text, "%s%", dis.toString(), 1));
                    }
                }
                holograms.put(uuid, hologram);
            }
        }.runTask(plugin);
    }

    public static void removeHologram(Player player) {
        if (!(ConfigFile.Enable.hologram && Main.hologram)) return;

        UUID uuid = player.getUniqueId();

        new BukkitRunnable() {
            @Override
            public void run() {
                Hologram hologram = holograms.get(uuid);
                if (hologram == null) return;
                hologram.delete();
                holograms.remove(uuid);
            }
        }.runTask(plugin);
    }

    public static void updateLine(Player player, int dis) {
        if (!(ConfigFile.Enable.hologram && Main.hologram)) return;
        UUID uuid = player.getUniqueId();
        if (!holograms.containsKey(uuid)) return;

        new BukkitRunnable() {
            @Override
            public void run() {
                Hologram hologram = holograms.get(uuid);

                for (int i = 0; i < ConfigFile.Tips.hologram.size(); i++) {
                    String s = ConfigFile.Tips.hologram.get(i);
                    if (s.startsWith("[item]")) continue;

                    if (s.startsWith("[text]")) {
                        String text = StringUtils.replace(s, "[text]", "", 1);
                        ((TextLine) hologram.getLine(i)).setText(StringUtils.replace(text, "%s%", String.valueOf(dis), 1));
                    }
                }
            }
        }.runTask(plugin);
    }
}
