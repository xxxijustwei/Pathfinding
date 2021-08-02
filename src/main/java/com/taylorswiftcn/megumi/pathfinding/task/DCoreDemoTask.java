package com.taylorswiftcn.megumi.pathfinding.task;

import com.taylorswiftcn.megumi.pathfinding.algorithm.astar.PathNode;
import eos.moe.dragoncore.api.CoreAPI;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.UUID;

public class DCoreDemoTask extends BukkitRunnable {

    private Player player;
    private String world;
    private String key;
    private List<PathNode> open;
    private List<PathNode> close;
    private List<Location> path;

    public DCoreDemoTask(Player player, Queue<PathNode> open, List<PathNode> close, List<Location> path) {
        this.player = player;
        this.world = player.getWorld().getName();
        this.key = UUID.randomUUID().toString();
        this.open = new ArrayList<>(open);
        this.close = close;
        this.path = path;
        this.initView();
    }

    private void initView() {
        for (int i = 0; i < path.size(); i++) {
            Location clone = path.get(i).clone().add(0, 0.5, 0);
            CoreAPI.setPlayerWorldTexture(player, key + ".path." + i, clone, 0, 0, 0, "pathfinding/target.png", 0.8f, 0.8f, 1, true, false);
        }

        for (int i = 0; i < open.size(); i++) {
            Location clone = open.get(i).getLocation().clone().add(0, 0.5, 0);
            CoreAPI.setPlayerWorldTexture(player, key + ".open." + i, clone, 0, 0, 0, "pathfinding/nav_b.png", 0.8f, 0.8f, 1, true, false);
        }

        for (int i = 0; i < close.size(); i++) {
            Location clone = close.get(i).getLocation().clone().add(0, 0.5, 0);
            CoreAPI.setPlayerWorldTexture(player, key + ".close." + i, clone, 0, 0, 0, "pathfinding/nav_a.png", 0.8f, 0.8f, 1, true, false);
        }
    }

    private void delView() {
        for (int i = 0; i < path.size(); i++) CoreAPI.removePlayerWorldTexture(player, key + ".path." + i);
        for (int i = 0; i < open.size(); i++) CoreAPI.removePlayerWorldTexture(player, key + ".open." + i);
        for (int i = 0; i < close.size(); i++) CoreAPI.removePlayerWorldTexture(player, key + ".close." + i);
    }

    @Override
    public synchronized void cancel() throws IllegalStateException {
        delView();
        super.cancel();
    }

    @Override
    public void run() {
        if (!player.isOnline()) {
            cancel();
            return;
        }

        if (!player.getLocation().getWorld().getName().equals(world)) {
            cancel();
        }
    }
}
