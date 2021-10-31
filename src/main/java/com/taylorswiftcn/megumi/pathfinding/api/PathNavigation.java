package com.taylorswiftcn.megumi.pathfinding.api;


import com.taylorswiftcn.megumi.pathfinding.algorithm.PathCallback;
import com.taylorswiftcn.megumi.pathfinding.algorithm.astar.AStarFinder;
import com.taylorswiftcn.megumi.pathfinding.util.SchedulerUtil;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PathNavigation extends AStarFinder {

    public PathNavigation(Location origin, Location destination, int mode) {
        this(origin, destination, mode, -1);
    }

    public PathNavigation(Location origin, Location destination, int mode, int openNodeLimit) {
        super(origin, destination, mode, null, openNodeLimit);
    }

    public void getPath(PathCallback<List<Location>> callback) {
        SchedulerUtil.runAsync(() -> {
            List<Location> path = this.getSearchPath();
            if (path.isEmpty()) {
                callback.fail();
                return;
            }

            callback.success(path);
        });
    }
}
