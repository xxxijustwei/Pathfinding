package com.taylorswiftcn.megumi.pathfinding.util;

import com.taylorswiftcn.megumi.pathfinding.Pathfinding;
import org.bukkit.Bukkit;

public class SchedulerUtil {

    private static final Pathfinding plugin = Pathfinding.getInstance();

    public static void run(Runnable runnable) {
        Bukkit.getScheduler().runTask(plugin, runnable);
    }

    public static void runAsync(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
    }

    public static void runLater(Runnable runnable, long tick) {
        Bukkit.getScheduler().runTaskLater(plugin, runnable, tick);
    }

    public static void runLaterAsync(Runnable runnable, long tick) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, runnable, tick);
    }
}
