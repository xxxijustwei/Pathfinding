package com.taylorswiftcn.megumi.pathfinding.api;

import com.taylorswiftcn.megumi.pathfinding.Pathfinding;
import com.taylorswiftcn.megumi.pathfinding.algorithm.astar.AStarNavFinder;
import com.taylorswiftcn.megumi.pathfinding.util.PathFindUtil;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class NavigationAPI {

    private static Pathfinding plugin = Pathfinding.getInstance();

    public static void asyncNavPlayer(Player player, Location destination, Entity npc) {
        new BukkitRunnable() {
            @Override
            public void run() {
                int mode = PathFindUtil.getNavMode(player.getLocation(), destination);

                AStarNavFinder nav = new PlayerNavigation(player, destination, mode, npc, -1);
                nav.start();
            }
        }.runTaskAsynchronously(plugin);
    }
}
