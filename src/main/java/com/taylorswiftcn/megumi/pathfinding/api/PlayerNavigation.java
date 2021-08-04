package com.taylorswiftcn.megumi.pathfinding.api;

import com.taylorswiftcn.megumi.pathfinding.Main;
import com.taylorswiftcn.megumi.pathfinding.algorithm.SearchPathManager;
import com.taylorswiftcn.megumi.pathfinding.algorithm.astar.AStarNavFinder;
import com.taylorswiftcn.megumi.pathfinding.file.sub.ConfigFile;
import com.taylorswiftcn.megumi.pathfinding.file.sub.MessageFile;
import com.taylorswiftcn.megumi.pathfinding.task.DCoreDemoTask;
import com.taylorswiftcn.megumi.pathfinding.task.DCoreNavigationTask;
import com.taylorswiftcn.megumi.pathfinding.task.NavigationTask;
import com.taylorswiftcn.megumi.pathfinding.task.ParticleDemoTask;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayerNavigation extends AStarNavFinder {

    private Player player;

    public PlayerNavigation(Player player, Location destination, Entity npc, int openNodeLimit) {
        super(player.getLocation(), destination, npc, openNodeLimit);
        this.player = player;
    }

    @Override
    public List<Location> start() {
        SearchPathManager.sendNavTitle(player);

        if (!searchPath()) {
            player.sendMessage(ConfigFile.Prefix + MessageFile.navFailure);
            return new ArrayList<>();
        }

        visual();
        return Arrays.asList(getPath());
    }

    private void visual() {
        if (!SearchPathManager.getVisual().contains(player.getUniqueId())) {
            BukkitRunnable task = Main.dragonCore ?  new DCoreNavigationTask(player, Arrays.asList(getPath()), getDestination(), getNpc()) : new NavigationTask(player, Arrays.asList(getPath()), getDestination(), getNpc());
            task.runTaskTimerAsynchronously(Main.getInstance(), 0, Main.dragonCore ? 2 : 10);
            SearchPathManager.getFindTask().put(player.getUniqueId(), task);
            return;
        }

        TextComponent text = new TextComponent("§a聊天框输入 #cancel 取消粒子效果或点击: ");
        TextComponent button = new TextComponent("§3§l[§a取消§3§l]");
        player.sendMessage(" ");
        player.sendMessage("§6已探索路径节点：" + getCloseNodes().size());
        player.sendMessage("§6列队中路径节点: " + getOpenNodes().size());
        player.sendMessage("§6最优路径长度: " + getPath().length);
        button.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/pf cancel"));
        button.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§a点击取消").create()));
        text.addExtra(button);
        player.spigot().sendMessage(text);
        player.sendMessage(" ");

        BukkitRunnable task = Main.dragonCore ? new DCoreDemoTask(player, getOpenNodes(), getCloseNodes(), Arrays.asList(getPath())) : new ParticleDemoTask(player, getOpenNodes(), getCloseNodes(), Arrays.asList(getPath()));
        task.runTaskTimerAsynchronously(Main.getInstance(), 0, 5);
        SearchPathManager.addDemoTaskID(player, task);

        if (getNpc() != null) SearchPathManager.getGlow().put(player.getUniqueId(), getNpc());
    }
}
