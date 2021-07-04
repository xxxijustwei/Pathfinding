package com.taylorswiftcn.megumi.pathfinding.algorithm.astar;

import com.taylorswiftcn.megumi.pathfinding.Main;
import com.taylorswiftcn.megumi.pathfinding.algorithm.SearchPathManager;
import com.taylorswiftcn.megumi.pathfinding.file.sub.ConfigFile;
import com.taylorswiftcn.megumi.pathfinding.file.sub.MessageFile;
import com.taylorswiftcn.megumi.pathfinding.task.DCoreNavigationTask;
import com.taylorswiftcn.megumi.pathfinding.task.NavigationTask;
import com.taylorswiftcn.megumi.pathfinding.task.ParticleDemoTask;
import com.taylorswiftcn.megumi.pathfinding.util.MegumiUtil;
import com.taylorswiftcn.megumi.pathfinding.util.special.MaterialUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.material.Door;
import org.bukkit.material.Gate;
import org.bukkit.material.MaterialData;

import java.util.*;

public class AStarFinder {

    private Player player;

    private Location origin;
    private Location target;

    private Entity npc;

    private PathNode start;
    private PathNode end;

    private Queue<PathNode> openNodes;
    private List<PathNode> closeNodes;

    private Location[] path;

    private long time;

    public AStarFinder(Player player, Location origin, Location target) {
        this(player, origin, target, null);
    }

    public AStarFinder(Player player, Location origin, Location target, Entity npc) {
        this.player = player;
        this.origin = origin;
        this.target = target;
        this.start = new PathNode(origin, null, 0, origin.distance(target));
        this.end = new PathNode(origin, null, 0, 0);
        this.openNodes = new PriorityQueue<>();
        this.closeNodes = new ArrayList<>();
        this.npc = npc;
        this.time = -1;
    }

    public void navigation() {
        player.sendTitle(ConfigFile.title_theme, ConfigFile.title_sub, 10, 20, 10);

        openNodes.add(start);
        if (!getWay()) {
            player.sendMessage(ConfigFile.Prefix + MessageFile.navFailure);
            return;
        }
        visual();
    }

    public long test() {
        openNodes.add(start);
        getWay();
        return time;
    }

    public List<Location> getPath() {
        openNodes.add(start);
        if (!getWay()) return new ArrayList<>();
        return Arrays.asList(path);
    }

    private void visual() {
        if (!SearchPathManager.getVisual().contains(player.getUniqueId())) {
            if (Main.dragonCore) {
                DCoreNavigationTask task = new DCoreNavigationTask(player, Arrays.asList(path), target, npc);
                task.runTaskTimerAsynchronously(Main.getInstance(), 0, 2);
                SearchPathManager.getFindTask().put(player.getUniqueId(), task);
                return;
            }
            NavigationTask task = new NavigationTask(player, Arrays.asList(path), target, npc);
            task.runTaskTimerAsynchronously(Main.getInstance(), 0, 10);
            SearchPathManager.getFindTask().put(player.getUniqueId(), task);
            return;
        }

        TextComponent text = new TextComponent("§a聊天框输入 #cancel 取消粒子效果或点击: ");
        TextComponent button = new TextComponent("§3§l[§a取消§3§l]");
        player.sendMessage(" ");
        player.sendMessage("§6已探索路径节点：" + closeNodes.size());
        player.sendMessage("§6列队中路径节点: " + openNodes.size());
        player.sendMessage("§6最优路径长度: " + path.length);
        button.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/pf cancel"));
        button.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§a点击取消").create()));
        text.addExtra(button);
        player.spigot().sendMessage(text);
        player.sendMessage(" ");

        ParticleDemoTask task = new ParticleDemoTask(player, openNodes, closeNodes, Arrays.asList(path));
        task.runTaskTimerAsynchronously(Main.getInstance(), 0, 5);
        SearchPathManager.addDemoTaskID(player, task.getTaskId());

        if (npc != null) SearchPathManager.getGlow().put(player.getUniqueId(), npc);
    }

    private boolean getWay() {
        long a = System.currentTimeMillis();
        while (!openNodes.isEmpty()) {

            if (openNodes.size() > 600) return false;

            PathNode current = openNodes.poll();

            if (current.getDistance() < 1) {
                int length = 1;
                end = current;
                PathNode last = current;
                while (last.getParent() != null) {
                    length++;
                    last = last.getParent();
                }

                last = end;
                /*Location[] locations = new Location[length];*/
                path = new Location[length];
                for (int i = length - 1; i > 0; i--) {
                    Location loc = last.getLocation();
                    if (MaterialUtil.isStep(loc.clone().add(0, -1, 0).getBlock().getType())) {
                        loc.add(0, -0.5, 0);
                    }
                    path[i] = loc;
                    /*path[i] = last.getLocation();*/
                    last = last.getParent();
                }
                path[0] = origin;

                long b = System.currentTimeMillis();
                time = b - a;

                MegumiUtil.debug(player, "§a已找到路径用时: " + time + " ms");
                MegumiUtil.debug(player, "§6路径长度: " + path.length);
                MegumiUtil.debug(player, "§6已探索路径节点：" + closeNodes.size());
                MegumiUtil.debug(player, "§6列队中路径节点: " + openNodes.size());

                /*path = locations;*/
                return true;
            }

            closeNodes.add(current);
            explore(current);
        }

        MegumiUtil.debug(player, "§c未找到路径");

        return false;
    }

    private void explore(PathNode node) {
        /*boolean w = false;
        boolean s = false;
        boolean a = false;
        boolean d = false;

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if ((x == 0 && (y == 0 || y == -1) && z == 0) || x * z != 0) continue;
                    Location loc = node.getLocation().clone().add(x, y, z);

                    if (!canStandAt(loc)) continue;

                    if (x == 1 && z == 0) d = true;
                    if (x == -1 && z == 0) a = true;
                    if (x == 0 && z == 1) w = true;
                    if (x == 0 && z == -1) s = true;

                    addNeighborNode(loc, node, y != 0 ? 1.4142 : 1);
                }
            }
        }

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if ((x == 0 && (y == 0 || y == -1) && z == 0) || x * z == 0) continue;
                    Location loc = node.getLocation().clone().add(x, y, z);

                    if (!canStandAt(loc)) continue;

                    if (x == 1 && z == 1 && (w || d)) {
                        addNeighborNode(loc, node, y == 0 ? 1.4142 : 1.732);
                        continue;
                    }

                    if (x == -1 && z == 1 && (w || a)) {
                        addNeighborNode(loc, node, y == 0 ? 1.4142 : 1.732);
                        continue;
                    }

                    if (x == -1 && z == -1 && (s || a)) {
                        addNeighborNode(loc, node, y == 0 ? 1.4142 : 1.732);
                        continue;
                    }

                    if (x == 1 && z == -1 && (s || d)) {
                        addNeighborNode(loc, node, y == 0 ? 1.4142 : 1.732);
                    }
                }
            }
        }*/

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if ((x == 0 && (y == 0 || y == -1) && z == 0)) continue;
                    Location loc = node.getLocation().clone().add(x, y, z);

                    if (y > 0) {
                        Material material = loc.clone().add(0, -1, 0).getBlock().getType();
                        if (MaterialUtil.isFence(material) || MaterialUtil.isWall(material)) continue;
                    }

                    if (!canStandAt(loc)) continue;

                    if (x * z == 0) {
                        addNeighborNode(loc, node, y != 0 ? 1.4142 : 1);
                        continue;
                    }

                    Location neighborA = loc.clone().add(-x, 0, 0);
                    Location neighborB = loc.clone().add(0, 0, -z);
                    if (!(canStandAt(neighborA) || canStandAt(neighborB))) continue;

                    addNeighborNode(loc, node, y == 0 ? 1.4142 : 1.732);
                }
            }
        }
    }

    private void addNeighborNode(Location loc, PathNode parent, double expense) {
        if (!canAddNodeToOpen(loc)) return;
        PathNode child = getNodeInOpen(loc);
        expense = parent.getExpense() + expense;

        if (child == null) {
            /*child = new PathNode(loc, parent, expense, loc.distance(target));*/
            child = new PathNode(loc, parent, expense, getDistance(loc, target));
            openNodes.add(child);
            return;
        }

        if (child.getExpense() > expense) {
            child.setExpense(expense);
            child.setParent(parent);
        }
    }


    private PathNode getNodeInOpen(Location loc) {
        if (openNodes.isEmpty()) return null;
        for (PathNode node : openNodes) {
            if (node.getLocation().equals(loc)) return node;
        }

        return null;
    }

    public boolean canAddNodeToOpen(Location loc) {
        if (closeNodes.isEmpty()) return true;
        for (PathNode node : closeNodes) {
            if (node.getLocation().equals(loc)) return false;
        }

        return true;
    }

    private boolean canStandAt(Location loc) {
        if (isDoorOrGate(loc)) {
            MaterialData data = loc.getBlock().getState().getData();
            if (data instanceof Door) {
                Door door = (Door) data;
                return door.isOpen() || !isObstructed(loc.clone().add(0, -1, 0));
            }

            if (data instanceof Gate) {
                Gate gate = (Gate) data;

                return gate.isOpen() || isObstructed(loc.clone().add(0, 1, 0)) || !isObstructed(loc.clone().add(0, -1, 0));
            }
            return false;
        }

        if (isObstructed(loc)) return false;

        Material material = loc.clone().add(0, -1, 0).getBlock().getType();
        if (MaterialUtil.isFence(material) || MaterialUtil.isWall(material)) {
            return !(isObstructed(loc.clone().add(0, 1, 0)) || isObstructed(loc.clone().add(0, 2, 0)));
        }

        return !(isObstructed(loc.clone().add(0, 1, 0)) || !isObstructed(loc.clone().add(0, -1, 0)));

        /*return !(isObstructed(loc) || isObstructed(loc.clone().add(0, 1, 0)) || !isObstructed(loc.clone().add(0, -1, 0)));*/
    }

    private boolean isDoorOrGate(Location loc) {
        Material material = loc.getBlock().getType();

        return MaterialUtil.isDoorOrGate(material);
    }

    private boolean isObstructed(Location loc) {
        Material material = loc.getBlock().getType();

        return material.isSolid();
    }

    private double getDistance(Location locA, Location locB) {
        if (ConfigFile.model == 1) return locA.distance(locB);
        return getManhattanDistance(locA, locB);
    }

    private double getManhattanDistance(Location locA, Location locB) {
        return Math.abs(locA.getX() - locB.getX()) + Math.abs(locA.getY() - locB.getY()) + Math.abs(locA.getZ() - locB.getZ());
    }

    private double getEuclideanDistance(Location locA, Location locB) {
        double dx = Math.pow(locA.getBlockX() - locB.getBlockX(), 2);
        double dy = Math.pow(locA.getBlockY() - locB.getBlockY(), 2);
        double dz = Math.pow(locA.getBlockZ() - locB.getBlockZ(), 2);

        return Math.sqrt(dx + dy + dz);
    }
}
