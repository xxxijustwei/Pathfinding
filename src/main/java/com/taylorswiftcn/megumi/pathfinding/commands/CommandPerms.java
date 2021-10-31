package com.taylorswiftcn.megumi.pathfinding.commands;

import lombok.Getter;

public enum CommandPerms {

    ADMIN("pathfinding.admin"),
    USER("pathfinding.user"),
    NAV("pathfinding.nav"),
    NPC("pathfinding.npc"),
    FIND("pathfinding.find");

    @Getter private String node;

    private CommandPerms(String node) {
        this.node = node;
    }
}
