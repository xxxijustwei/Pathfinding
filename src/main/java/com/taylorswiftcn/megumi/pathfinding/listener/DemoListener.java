package com.taylorswiftcn.megumi.pathfinding.listener;

import com.taylorswiftcn.megumi.pathfinding.Pathfinding;
import com.taylorswiftcn.megumi.pathfinding.algorithm.SearchPathManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;

public class DemoListener implements Listener {

    private Pathfinding plugin = Pathfinding.getInstance();

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        String msg = e.getMessage();

        if (!msg.equalsIgnoreCase("#cancel")) return;
        if (!SearchPathManager.getDemo().containsKey(uuid)) return;

        SearchPathManager.cancelDemo(player);
        e.setCancelled(true);
    }
}
