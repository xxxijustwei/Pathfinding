package com.taylorswiftcn.megumi.pathfinding.api;


import com.taylorswiftcn.megumi.pathfinding.algorithm.astar.AStarNavFinder;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PathNavigation extends AStarNavFinder {


    public PathNavigation(Location origin, Location destination, int openNodeLimit) {
        super(origin, destination, null, openNodeLimit);
    }

    @Override
    public List<Location> start() {
        if (!searchPath()) {
            return new ArrayList<>();
        }

        return Arrays.asList(getPath());
    }
}
