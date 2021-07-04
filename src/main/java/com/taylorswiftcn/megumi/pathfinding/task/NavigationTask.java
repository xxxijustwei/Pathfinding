package com.taylorswiftcn.megumi.pathfinding.task;

import com.taylorswiftcn.megumi.pathfinding.file.sub.ConfigFile;
import com.taylorswiftcn.megumi.pathfinding.file.sub.MessageFile;
import com.taylorswiftcn.megumi.pathfinding.util.message.ActionBarUtil;
import com.taylorswiftcn.megumi.pathfinding.util.special.EntityGlowUtil;
import com.taylorswiftcn.megumi.pathfinding.util.special.RGBParticleUtil;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.math.BigDecimal;
import java.util.List;

public class NavigationTask extends BukkitRunnable {

    private Player player;
    private List<Location> path;
    private Location target;
    private Entity npc;
    private Boolean glow;

    public NavigationTask(Player player, List<Location> path, Location target) {
        this.player = player;
        this.path = path;
        this.target = target;
        this.npc = null;
        this.glow = false;
    }

    public NavigationTask(Player player, List<Location> path, Location target, Entity npc) {
        this.player = player;
        this.path = path;
        this.target = target;
        this.npc = npc;
        this.glow = false;
    }

    @Override
    public void run() {
        if (!player.isOnline()) {
            cancel();
            return;
        }

        if (!glow && npc != null) {
            if (player.getLocation().distance(npc.getLocation()) < 32) {
                EntityGlowUtil.glow(player, npc);
                glow = true;
            }
        }

        if (player.getLocation().distance(target) < 3) {
            if (npc != null) EntityGlowUtil.unGlow(player, npc);
            player.sendMessage(ConfigFile.Prefix + MessageFile.arriveCoord);
            cancel();
            return;
        }

        ActionBarUtil.sendActionBar(player, ConfigFile.navigationBar.replace("%s%", BigDecimal.valueOf(player.getLocation().distance(target)).setScale(0, BigDecimal.ROUND_DOWN).toString()));

        Location front = null;

        for (Location loc : path) {

            RGBParticleUtil.create(player, loc.clone().add(0, 0.6, 0));
            if (front == null) {
                front = loc;
                continue;
            }

            buildLine(front.clone().add(0, 0.6, 0), loc.clone().add(0, 0.6, 0));

            front = loc;
        }
    }

    public void buildLine(Location start, Location end) {
        Vector vector = end.clone().subtract(start).toVector();
        double length = vector.length();
        vector.normalize();
        for (double i = 0; i < length; i += 0.1) {
            Vector child = vector.clone().multiply(i);
            start.add(child);
            RGBParticleUtil.create(player, start);
            start.subtract(child);
        }
    }
}
