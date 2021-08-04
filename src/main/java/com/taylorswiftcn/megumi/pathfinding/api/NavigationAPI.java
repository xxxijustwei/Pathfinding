package com.taylorswiftcn.megumi.pathfinding.api;

import com.taylorswiftcn.megumi.pathfinding.Main;
import com.taylorswiftcn.megumi.pathfinding.algorithm.astar.AStarNavFinder;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class NavigationAPI {

    private static Main plugin = Main.getInstance();

    public static void startNavPlayer(Player player, Location destination, Entity npc) {
        new BukkitRunnable() {
            @Override
            public void run() {
                AStarNavFinder nav = new PlayerNavigation(player, destination, npc, -1);
                nav.start();
            }
        }.runTaskAsynchronously(plugin);
    }
}
