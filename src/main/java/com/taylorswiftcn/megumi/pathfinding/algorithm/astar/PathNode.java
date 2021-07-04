package com.taylorswiftcn.megumi.pathfinding.algorithm.astar;

import lombok.Data;
import org.bukkit.Location;

@Data
public class PathNode implements Comparable<PathNode> {

    private Location location;
    private PathNode parent;

    public double expense;
    public double distance;

    public PathNode(Location location, PathNode parent, double expense, double distance) {
        this.location = location;
        this.parent = parent;
        this.expense = expense;
        this.distance = distance;
    }

    public double getPriority() {
        return expense + distance;
    }

    public boolean isObstructed(Location location) {
        return false;
    }

    public boolean canPass(Location location) {
        return false;
    }

    public int compareTo(PathNode node) {
        if (node == null) return -1;
        if (getPriority() > node.getPriority()) return 1;
        return -1;
    }
}
