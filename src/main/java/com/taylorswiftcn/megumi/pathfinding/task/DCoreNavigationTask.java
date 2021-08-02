package com.taylorswiftcn.megumi.pathfinding.task;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.taylorswiftcn.megumi.pathfinding.Main;
import com.taylorswiftcn.megumi.pathfinding.algorithm.SearchPathManager;
import com.taylorswiftcn.megumi.pathfinding.file.sub.ConfigFile;
import com.taylorswiftcn.megumi.pathfinding.file.sub.MessageFile;
import com.taylorswiftcn.megumi.pathfinding.util.special.EntityGlowUtil;
import eos.moe.dragoncore.api.CoreAPI;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class DCoreNavigationTask extends BukkitRunnable {
    private Player player;
    private String key;
    private List<Location> path;
    private Location target;
    private Entity npc;
    private Boolean glow;
    private Integer index;

    public DCoreNavigationTask(Player player, List<Location> path, Location target, Entity npc) {
        this.player = player;
        this.key = UUID.randomUUID().toString();
        this.path = path;
        this.target = target;
        this.npc = npc;
        this.glow = false;
        this.index = 0;
    }

    private void initHologram() {
        SearchPathManager.createHologram(player, target.clone().add(0, 2.5, 0));
    }

    private void delView() {
        for (int i = 0; i < path.size(); i++) CoreAPI.removePlayerWorldTexture(player, key + i);

        SearchPathManager.removeHologram(player);
    }

    @Override
    public synchronized void cancel() throws IllegalStateException {
        delView();
        if (npc != null && glow) EntityGlowUtil.unGlow(player, npc);
        super.cancel();
    }

    @Override
    public void run() {
        if (!player.isOnline()) {
            delView();
            cancel();
            return;
        }

        if (player.getLocation().getWorld() != target.getWorld()) {
            cancel();
            return;
        }

        int distance = BigDecimal.valueOf(player.getLocation().distance(npc.getLocation())).setScale(0, BigDecimal.ROUND_DOWN).intValue();

        if (distance < 2) {
            delView();
            if (npc != null) EntityGlowUtil.unGlow(player, npc);
            player.sendMessage(ConfigFile.Prefix + MessageFile.arriveCoord);
            cancel();
            return;
        }

        if (distance <= 24 && !SearchPathManager.getHolograms().containsKey(player.getUniqueId())) {
            initHologram();
        }

        if (!glow && npc != null && distance < 32) {
            EntityGlowUtil.glow(player, npc);
            glow = true;
        }

        CoreAPI.setPlayerWorldTexture(player, key + getLast(index), getLoc(getLast(index)), 0, 0, 0, "pathfinding/nav_a.png", 0.6f, 0.6f, 1, true, false);
        CoreAPI.setPlayerWorldTexture(player, key + index, getLoc(index), 0, 0, 0, "pathfinding/nav_b.png", 0.8f, 0.8f, 1, true, false);

        SearchPathManager.updateLine(player, distance);
        SearchPathManager.sendNavActionBar(player, distance);

        index++;
        if (index >= path.size()) index = 0;
    }

    private int getLast(int index) {
        if (index == 0) return path.size() - 1;
        return index - 1;
    }

    private Location getLoc(int index) {
        return path.get(index).clone().add(0, 0.5, 0);
    }
}
