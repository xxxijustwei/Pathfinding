package com.taylorswiftcn.megumi.pathfinding.algorithm.astar;

import com.taylorswiftcn.megumi.pathfinding.algorithm.PathCallback;
import com.taylorswiftcn.megumi.pathfinding.file.sub.ConfigFile;
import com.taylorswiftcn.megumi.pathfinding.util.MaterialUtil;
import lombok.Data;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.material.Door;
import org.bukkit.material.Gate;
import org.bukkit.material.MaterialData;

import java.util.*;

@Data
public abstract class AStarFinder {

    /**
     * 起点
     */
    private Location origin;
    /**
     * 终点
     */
    private Location destination;

    /**
     * 模式
     */
    private Integer mode;

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
     * AstarNavFinder
     *
     * @param origin      起点
     * @param destination 终点
     * @param mode        模式
     */
    public AStarFinder(Location origin, Location destination, Integer mode) {
        this(origin, destination, mode, null);
    }

    /**
     * AstarNavFinder
     *
     * @param origin      起点
     * @param destination 终点
     * @param npc         NPC
     * @param mode        模式
     */
    public AStarFinder(Location origin, Location destination, Integer mode, Entity npc) {
        this(origin, destination, mode, npc, -1);
    }

    /**
     * AstarNavFinder
     *
     * @param origin        起点
     * @param destination   终点
     * @param npc           NPC
     * @param openNodeLimit 开放节点数量限制
     * @param mode          模式
     */
    public AStarFinder(Location origin, Location destination, Integer mode, Entity npc, int openNodeLimit) {
        this.origin = origin;
        this.destination = destination;
        this.mode = mode;
        this.start = new PathNode(origin, null, 0, origin.distance(destination));
        this.end = new PathNode(origin, null, 0, 0);
        this.openNodes = new PriorityQueue<>();
        this.closeNodes = new ArrayList<>();
        this.npc = npc;
        this.openNodeLimit = openNodeLimit;
        this.time = -1;
        this.openNodes.add(start);
    }

    /**
     * 测试导航路径计算时间
     *
     * @return long
     */
    public long testCalculationTime() {
        openNodes.add(start);
        startSearch();
        return time;
    }

    /**
     * 获取导航路径
     *
     * @return {@link List<Location>}
     */
    public List<Location> getSearchPath() {
        openNodes.add(start);
        if (!startSearch()) return new ArrayList<>();
        return Arrays.asList(path);
    }

    /**
     * 导航路径探索
     *
     * @return boolean
     */
    public boolean startSearch() {
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

                return true;
            }

            closeNodes.add(current);
            explore(current);
        }

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
            child = new PathNode(loc, parent, expense, getDistance(loc, destination));
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
        if (mode == 1) return getEuclideanDistance(locA, locB);
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
