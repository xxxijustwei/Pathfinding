package com.taylorswiftcn.megumi.pathfinding.api;


import com.taylorswiftcn.megumi.pathfinding.algorithm.astar.AStarNavFinder;
import com.taylorswiftcn.megumi.pathfinding.file.sub.ConfigFile;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PathNavigation extends AStarNavFinder {

    public PathNavigation(Location origin, Location destination, int mode) {
        this(origin, destination, mode, -1);
    }

    public PathNavigation(Location origin, Location destination, int mode, int openNodeLimit) {
        super(origin, destination, mode, null, openNodeLimit);
    }

    @Override
    public List<Location> start() {
        if (!searchPath()) {
            return new ArrayList<>();
        }

        return Arrays.asList(getPath());
    }
}
