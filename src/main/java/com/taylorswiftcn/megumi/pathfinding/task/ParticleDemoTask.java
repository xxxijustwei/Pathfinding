package com.taylorswiftcn.megumi.pathfinding.task;

import com.taylorswiftcn.megumi.pathfinding.algorithm.astar.PathNode;
import com.taylorswiftcn.megumi.pathfinding.util.special.RGBParticleUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Queue;

public class ParticleDemoTask extends BukkitRunnable {

    private Player player;
    private Queue<PathNode> open;
    private List<PathNode> close;
    private List<Location> path;

    public ParticleDemoTask(Player player, Queue<PathNode> open, List<PathNode> close, List<Location> path) {
        this.player = player;
        this.open = open;
        this.close = close;
        this.path = path;
    }

    @Override
    public void run() {
        for (PathNode node : open) {
            RGBParticleUtil.create(player, node.getLocation().clone().add(0, 0.2, 0), 132f, 112f, 255f);
        }

        for (PathNode node : close) {
            RGBParticleUtil.create(player, node.getLocation().clone().add(0, 0.2, 0), 255f, 255f, 0f);
        }

        for (Location loc : path) {
            player.spawnParticle(Particle.BARRIER, loc.clone().add(0, 1, 0), 1);
        }
    }
}
