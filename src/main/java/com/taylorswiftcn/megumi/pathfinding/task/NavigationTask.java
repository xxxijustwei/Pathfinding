package com.taylorswiftcn.megumi.pathfinding.task;

import com.taylorswiftcn.justwei.util.special.EntityGlow;
import com.taylorswiftcn.justwei.util.special.RGBParticle;
import com.taylorswiftcn.megumi.pathfinding.algorithm.SearchPathManager;
import com.taylorswiftcn.megumi.pathfinding.file.sub.ConfigFile;
import com.taylorswiftcn.megumi.pathfinding.file.sub.MessageFile;
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
        this(player, path, target, null);
    }

    public NavigationTask(Player player, List<Location> path, Location target, Entity npc) {
        this.player = player;
        this.path = path;
        this.target = target;
        this.npc = npc;
        this.glow = false;
    }

    private void initHologram() {
        SearchPathManager.createHologram(player, target.clone().add(0, 2.5, 0));
    }

    @Override
    public synchronized void cancel() throws IllegalStateException {
        SearchPathManager.removeHologram(player);
        if (npc != null && glow) EntityGlow.undoGlow(player, npc);
        super.cancel();
    }

    @Override
    public void run() {
        if (!player.isOnline()) {
            cancel();
            return;
        }

        if (player.getLocation().getWorld() != target.getWorld()) {
            cancel();
            return;
        }

        int distance = BigDecimal.valueOf(player.getLocation().distance(target)).setScale(0, BigDecimal.ROUND_DOWN).intValue();

        if (distance < 2) {
            if (npc != null) EntityGlow.undoGlow(player, npc);
            player.sendMessage(ConfigFile.Prefix + MessageFile.arriveCoord);
            cancel();
            return;
        }

        if (distance <= 24 && !SearchPathManager.getHolograms().containsKey(player.getUniqueId())) {
            initHologram();
        }

        if (!glow && npc != null && distance < 32) {
            EntityGlow.doGlow(player, npc);
            glow = true;
        }

        BigDecimal dis = BigDecimal.valueOf(player.getLocation().distance(target)).setScale(0, BigDecimal.ROUND_DOWN);

        SearchPathManager.updateLine(player, dis.intValue());
        SearchPathManager.sendNavActionBar(player, dis.intValue());

        Location front = null;

        for (Location loc : path) {

            RGBParticle.send(player, loc.clone().add(0, 0.6, 0));
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
            RGBParticle.send(player, start);
            start.subtract(child);
        }
    }
}
