package com.taylorswiftcn.megumi.pathfinding.api;

import com.taylorswiftcn.megumi.pathfinding.Pathfinding;
import com.taylorswiftcn.megumi.pathfinding.algorithm.PathCallback;
import com.taylorswiftcn.megumi.pathfinding.util.FinderUtil;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;

public class NavigationAPI {

    private static Pathfinding plugin = Pathfinding.getInstance();

    public static void onNav2Player(Player player, Location destination, Entity npc) {
        int mode = FinderUtil.getNavMode(player.getLocation(), destination);

        PlayerNavigation nav = new PlayerNavigation(player, destination, mode, npc, -1);
        nav.start();
    }

    public static void getNavPath(Location origin, Location destination, int mode, PathCallback<List<Location>> callback) {
        PathNavigation navigation = new PathNavigation(origin, destination, mode);
        navigation.getPath(callback);
    }
}
