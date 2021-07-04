package com.taylorswiftcn.megumi.pathfinding.algorithm.waypoint.constructor;

import lombok.Data;
import org.bukkit.Location;

@Data
public class NavPoint {

    private String id;
    private Location location;

    public NavPoint(String id, Location location) {
        this.id = id;
        this.location = location;
    }
}
