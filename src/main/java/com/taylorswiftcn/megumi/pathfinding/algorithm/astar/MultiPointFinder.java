package com.taylorswiftcn.megumi.pathfinding.algorithm.astar;

import com.taylorswiftcn.megumi.pathfinding.Main;
import com.taylorswiftcn.megumi.pathfinding.algorithm.SearchPathManager;
import com.taylorswiftcn.megumi.pathfinding.algorithm.waypoint.DjikstraFinder;
import com.taylorswiftcn.megumi.pathfinding.api.PathNavigation;
import com.taylorswiftcn.megumi.pathfinding.file.sub.ConfigFile;
import com.taylorswiftcn.megumi.pathfinding.task.NavigationTask;
import com.taylorswiftcn.megumi.pathfinding.util.MegumiUtil;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MultiPointFinder {

    private Player player;
    private Location origin;
    private Location destination;
    private List<Location> points;
    private LinkedList<Location> path;
    private Entity npc;

    public MultiPointFinder(Player player, Location origin, Location destination) {
        this(player, origin, destination, null);
    }

    public MultiPointFinder(Player player, Location origin, Location destination, Entity npc) {
        this.player = player;
        this.origin = origin;
        this.destination = destination;
        this.points = new ArrayList<>();
        this.path = new LinkedList<>();
        this.npc = npc;
    }

    public void navigation() {
        SearchPathManager.sendNavTitle(player);

        long a = System.currentTimeMillis();

        planning();
        if (path.size() == 0) {
            MegumiUtil.debug(player, "§c寻路失败");
            return;
        }

        NavigationTask task = new NavigationTask(player, path, destination, npc);
        task.runTaskTimerAsynchronously(Main.getInstance(), 0, 10);

        long b = System.currentTimeMillis();

        MegumiUtil.debug(player, "已找到路径用时: " + (b - a) + "ms");
    }

    private void planning() {
        DjikstraFinder finder = new DjikstraFinder(origin, destination);
        points.addAll(finder.route());
        explore();
    }

    public void explore() {
        if (points.size() == 0) return;

        Location current = null;
        for (Location loc : points) {
            if (current == null) {
                current = loc;
                continue;
            }

            int mode = MegumiUtil.getNavMode(current, loc);

            PathNavigation nav = new PathNavigation(current, loc, mode);
            List<Location> locations = nav.start();
            if (locations.size() == 0) {
                path.clear();
                player.sendMessage(ConfigFile.Prefix + "§cError");
                return;
            }

            path.addAll(locations);
            current = loc;
        }
    }
}
