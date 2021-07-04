package com.taylorswiftcn.megumi.pathfinding.algorithm.waypoint.constructor;

import lombok.Data;

@Data
public class Connection {

    private String id;
    private Double distance;

    public Connection(String id, Double distance) {
        this.id = id;
        this.distance = distance;
    }
}
