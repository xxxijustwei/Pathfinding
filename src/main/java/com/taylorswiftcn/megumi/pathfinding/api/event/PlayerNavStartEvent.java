package com.taylorswiftcn.megumi.pathfinding.api.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@EqualsAndHashCode(callSuper = true)
@Data
public class PlayerNavStartEvent extends Event {

    private Player player;
    private Location origin;
    private Location destination;
    private Entity npc;
    private int openNodeLimit;
    private boolean cancel;
    private static HandlerList handlerList = new HandlerList();

    public PlayerNavStartEvent(Player player, Location origin, Location destination) {
        this(player, origin, destination, null);
    }

    public PlayerNavStartEvent(Player player, Location origin, Location destination, Entity npc) {
        this(player, origin, destination, npc, -1);
    }

    public PlayerNavStartEvent(Player player, Location origin, Location destination, Entity npc, int openNodeLimit) {
        this.player = player;
        this.origin = origin;
        this.destination = destination;
        this.npc = npc;
        this.openNodeLimit = openNodeLimit;
        this.cancel = false;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}
