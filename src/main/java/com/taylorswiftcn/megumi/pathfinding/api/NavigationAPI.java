package com.taylorswiftcn.megumi.pathfinding.api;

import com.taylorswiftcn.megumi.pathfinding.Main;
import com.taylorswiftcn.megumi.pathfinding.algorithm.astar.AStarNavFinder;
import com.taylorswiftcn.megumi.pathfinding.file.sub.ConfigFile;
import com.taylorswiftcn.megumi.pathfinding.util.MegumiUtil;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class NavigationAPI {

    private static Main plugin = Main.getInstance();

    public static void asyncNavPlayer(Player player, Location destination, Entity npc) {
        new BukkitRunnable() {
            @Override
            public void run() {
                int mode = MegumiUtil.getNavMode(player.getLocation(), destination);

                AStarNavFinder nav = new PlayerNavigation(player, destination, mode, npc, -1);
                nav.start();
            }
        }.runTaskAsynchronously(plugin);
    }
}
