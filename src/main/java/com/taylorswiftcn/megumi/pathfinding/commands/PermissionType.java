package com.taylorswiftcn.megumi.pathfinding.commands;

public enum PermissionType {

    Admin("admin"),Use("use"),Nav("nav"),Npc("npc"),Find("find");

    private String id;

    private PermissionType(String id) {
        this.id = id;
    }
}
