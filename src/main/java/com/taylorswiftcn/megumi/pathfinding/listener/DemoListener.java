package com.taylorswiftcn.megumi.pathfinding.listener;

import com.taylorswiftcn.megumi.pathfinding.Main;
import com.taylorswiftcn.megumi.pathfinding.algorithm.SearchPathManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;

public class DemoListener implements Listener {

    private Main plugin = Main.getInstance();

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        String msg = e.getMessage();

        if (!msg.equalsIgnoreCase("#cancel")) return;
        if (!SearchPathManager.getParticle().containsKey(uuid)) return;

        SearchPathManager.cancelDemo(player);
        e.setCancelled(true);
    }
}
