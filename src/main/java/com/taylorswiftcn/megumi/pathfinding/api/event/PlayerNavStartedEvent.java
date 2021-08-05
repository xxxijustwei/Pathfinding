package com.taylorswiftcn.megumi.pathfinding.api.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class PlayerNavStartedEvent extends Event {

    private Player player;
    private List<Location> path;
    private Entity npc;
    private boolean cancel;
    private static HandlerList handlerList = new HandlerList();

    public PlayerNavStartedEvent(Player player, List<Location> path) {
        this(player, path, null);
    }

    public PlayerNavStartedEvent(Player player, List<Location> path, Entity npc) {
        this.player = player;
        this.path = path;
        this.npc = npc;
        this.cancel = false;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}
