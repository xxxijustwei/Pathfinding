package com.taylorswiftcn.megumi.pathfinding.algorithm.waypoint.constructor;

import lombok.Data;

@Data
public class WayNode implements Comparable<WayNode> {

    private String id;
    private WayNode parent;
    private Double expense;

    public WayNode(String id, WayNode parent, Double expense) {
        this.id = id;
        this.parent = parent;
        this.expense = expense;
    }

    @Override
    public int compareTo(WayNode o) {
        if (o == null) return -1;
        if (getExpense() > o.getExpense()) return 1;
        return -1;
    }
}
