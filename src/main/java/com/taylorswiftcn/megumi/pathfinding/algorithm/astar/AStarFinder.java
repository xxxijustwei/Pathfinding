package com.taylorswiftcn.megumi.pathfinding.algorithm.astar;

import com.taylorswiftcn.megumi.pathfinding.Main;
import com.taylorswiftcn.megumi.pathfinding.algorithm.SearchPathManager;
import com.taylorswiftcn.megumi.pathfinding.file.sub.ConfigFile;
import com.taylorswiftcn.megumi.pathfinding.file.sub.MessageFile;
import com.taylorswiftcn.megumi.pathfinding.task.DCoreDemoTask;
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
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class AStarFinder {

    /**
     * 玩家
     */
    private Player player;

    /**
     * 起点
     */
    private Location origin;
    /**
     * 终点
     */
    private Location target;

    /**
     * NPC
     */
    private Entity npc;

    /**
     * 开放节点数量限制
     */
    private Integer openNodeLimit;

    /**
     * 起始路径点
     */
    private PathNode start;
    /**
     * 终止路径点
     */
    private PathNode end;

    /**
     * 开放节点
     * 队列中需要探索的路径点
     */
    private Queue<PathNode> openNodes;
    /**
     * 关闭节点
     * 已经探索过的路径点
     */
    private List<PathNode> closeNodes;

    /**
     * 路径
     */
    private Location[] path;

    /**
     * 用时
     */
    private long time;

    /**
     * AstarFinder
     *
     * @param player 玩家
     * @param origin 起点
     * @param target 终点
     */
    public AStarFinder(Player player, Location origin, Location target) {
        this(player, origin, target, null);
    }

    /**
     * AstarFinder
     *
     * @param player 玩家
     * @param origin 起点
     * @param target 终点
     * @param npc    NPC
     */
    public AStarFinder(Player player, Location origin, Location target, Entity npc) {
        this(player, origin, target, npc, -1);
    }

    /**
     * 斯达仪
     *
     * @param player 玩家
     * @param origin 起点
     * @param target 终点
     * @param npc    NPC
     * @param openNodeLimit 开放节点数量限制
     */
    public AStarFinder(Player player, Location origin, Location target, Entity npc, int openNodeLimit) {
        this.player = player;
        this.origin = origin;
        this.target = target;
        this.start = new PathNode(origin, null, 0, origin.distance(target));
        this.end = new PathNode(origin, null, 0, 0);
        this.openNodes = new PriorityQueue<>();
        this.closeNodes = new ArrayList<>();
        this.npc = npc;
        this.openNodeLimit = openNodeLimit;
        this.time = -1;
    }

    /**
     * 开始导航
     */
    public void navigation() {
        SearchPathManager.sendNavTitle(player);

        openNodes.add(start);
        if (!getWay()) {
            player.sendMessage(ConfigFile.Prefix + MessageFile.navFailure);
            return;
        }
        visual();
    }

    /**
     * 测试导航路径计算时间
     *
     * @return long
     */
    public long testCalculationTime() {
        openNodes.add(start);
        getWay();
        return time;
    }

    /**
     * 获取导航路径
     *
     * @return {@link List<Location>}
     */
    public List<Location> getPath() {
        openNodes.add(start);
        if (!getWay()) return new ArrayList<>();
        return Arrays.asList(path);
    }

    /**
     * 导航视觉指引提示
     */
    private void visual() {
        if (!SearchPathManager.getVisual().contains(player.getUniqueId())) {
            BukkitRunnable task = Main.dragonCore ?  new DCoreNavigationTask(player, Arrays.asList(path), target, npc) : new NavigationTask(player, Arrays.asList(path), target, npc);
            task.runTaskTimerAsynchronously(Main.getInstance(), 0, Main.dragonCore ? 2 : 10);
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

        BukkitRunnable task = Main.dragonCore ? new DCoreDemoTask(player, openNodes, closeNodes, Arrays.asList(path)) : new ParticleDemoTask(player, openNodes, closeNodes, Arrays.asList(path));
        task.runTaskTimerAsynchronously(Main.getInstance(), 0, 5);
        SearchPathManager.addDemoTaskID(player, task);

        if (npc != null) SearchPathManager.getGlow().put(player.getUniqueId(), npc);
    }

    /**
     * 导航路径探索
     *
     * @return boolean
     */
    private boolean getWay() {
        long a = System.currentTimeMillis();
        while (!openNodes.isEmpty()) {

            if (openNodes.size() > getNodeLimit()) return false;

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
                path = new Location[length];
                for (int i = length - 1; i > 0; i--) {
                    Location loc = last.getLocation();
                    if (MaterialUtil.isStep(loc.clone().add(0, -1, 0).getBlock().getType())) {
                        loc.add(0, -0.5, 0);
                    }
                    path[i] = loc;
                    last = last.getParent();
                }
                path[0] = origin;

                long b = System.currentTimeMillis();
                time = b - a;

                MegumiUtil.debug(player, "§a已找到路径用时: " + time + " ms");
                MegumiUtil.debug(player, "§6路径长度: " + path.length);
                MegumiUtil.debug(player, "§6已探索路径节点：" + closeNodes.size());
                MegumiUtil.debug(player, "§6列队中路径节点: " + openNodes.size());

                return true;
            }

            closeNodes.add(current);
            explore(current);
        }

        MegumiUtil.debug(player, "§c未找到路径");

        return false;
    }

    /**
     * 获取开放节点数量限制
     *
     * @return {@link Integer}
     */
    private Integer getNodeLimit() {
        if (openNodeLimit == -1) return ConfigFile.Base.openNodeCount;

        return openNodeLimit;
    }

    /**
     * 路径点周围探索
     *
     * @param node 路径点
     */
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

    /**
     * 添加邻居路径点
     *
     * @param loc     位置
     * @param parent  父路径点
     * @param expense 代价
     */
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


    /**
     * 通过位置在开放节点内获取路径点
     *
     * @param loc 位置
     * @return {@link PathNode}
     */
    private PathNode getNodeInOpen(Location loc) {
        if (openNodes.isEmpty()) return null;
        for (PathNode node : openNodes) {
            if (node.getLocation().equals(loc)) return node;
        }

        return null;
    }

    /**
     * 判断开放节点内是否有该位置的路径点
     *
     * @param loc 位置
     * @return boolean
     */
    public boolean canAddNodeToOpen(Location loc) {
        if (closeNodes.isEmpty()) return true;
        for (PathNode node : closeNodes) {
            if (node.getLocation().equals(loc)) return false;
        }

        return true;
    }

    /**
     * 判断该位置是否可以让玩家站立
     *
     * @param loc 位置
     * @return boolean
     */
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
    }

    /**
     * 判断该位置方块是否是门或者围栏
     *
     * @param loc 位置
     * @return boolean
     */
    private boolean isDoorOrGate(Location loc) {
        Material material = loc.getBlock().getType();

        return MaterialUtil.isDoorOrGate(material);
    }

    /**
     * 判断该位置方块是否是实心的
     *
     * @param loc 疯狂的
     * @return boolean
     */
    private boolean isObstructed(Location loc) {
        Material material = loc.getBlock().getType();

        return material.isSolid();
    }

    /**
     * 获取两个位置之间的距离
     *
     * @param locA 位置A
     * @param locB 位置B
     * @return double
     */
    private double getDistance(Location locA, Location locB) {
        if (ConfigFile.Base.mode == 1) return getEuclideanDistance(locA, locB);
        return getManhattanDistance(locA, locB);
    }

    /**
     * 获取两个位置间的曼哈顿距离
     *
     * @param locA 位置A
     * @param locB 位置B
     * @return double
     */
    private double getManhattanDistance(Location locA, Location locB) {
        return Math.abs(locA.getX() - locB.getX()) + Math.abs(locA.getY() - locB.getY()) + Math.abs(locA.getZ() - locB.getZ());
    }

    /**
     * 获取两个位置间的欧几里得距离
     *
     * @param locA 位置A
     * @param locB 位置B
     * @return double
     */
    private double getEuclideanDistance(Location locA, Location locB) {
        double dx = Math.pow(locA.getBlockX() - locB.getBlockX(), 2);
        double dy = Math.pow(locA.getBlockY() - locB.getBlockY(), 2);
        double dz = Math.pow(locA.getBlockZ() - locB.getBlockZ(), 2);

        return Math.sqrt(dx + dy + dz);
    }
}
