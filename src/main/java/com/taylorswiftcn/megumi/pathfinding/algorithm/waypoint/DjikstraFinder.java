package com.taylorswiftcn.megumi.pathfinding.algorithm.waypoint;

import com.taylorswiftcn.megumi.pathfinding.algorithm.waypoint.constructor.NavPoint;
import com.taylorswiftcn.megumi.pathfinding.algorithm.waypoint.constructor.WayNode;
import org.bukkit.Location;

import java.util.*;

public class DjikstraFinder {

    private String world;
    private Location origin;
    private Location destination;
    private String start;
    private String end;
    private Queue<WayNode> open;
    private List<String> close;
    private List<Location> points;
    private HashMap<String, List<String>> conn;

    public DjikstraFinder(Location origin, Location destination) {
        this.world = origin.getWorld().getName();
        this.origin = origin;
        this.destination = destination;
        this.start = "$origin$";
        this.end = "$destination$";
        this.open = new PriorityQueue<>();
        this.close = new ArrayList<>();
        this.points = new ArrayList<>();
        this.conn = new HashMap<>(WayPointHandler.getWorlds().get(world).getConn());
        this.relation();
    }

    private void relation() {
        List<String> just = getNearPoints(origin);
        List<String> wei = getNearPoints(destination);

        conn.put(start, just);
        conn.put(end, wei);

        just.forEach(s -> conn.get(s).add(start));
        wei.forEach(s -> conn.get(s).add(end));
    }

    private List<String> getNearPoints(Location location) {
        List<String> near = new ArrayList<>();
        List<NavPoint> points = new ArrayList<>(WayPointHandler.getWorlds().get(world).getPoints().values());

        for (NavPoint point : points) {
            Location loc = point.getLocation();
            if (loc.distance(location) > 50) continue;
            near.add(point.getId());
        }

        if (near.size() == 0) {
            String shortest = null;
            double dis = 0;
            for (NavPoint point : points) {
                if (shortest == null) {
                    shortest = point.getId();
                    dis = point.getLocation().distance(location);
                    continue;
                }

                if (point.getLocation().distance(location) > dis) continue;

                shortest = point.getId();
                dis = point.getLocation().distance(location);
            }

            near.add(shortest);
        }

        return near;
    }

    public List<Location> route() {
        long p = System.currentTimeMillis();
        getWay();
        long f = System.currentTimeMillis();

        System.out.println("WP用时: " + (f - p) + "ms");

        return points;
    }

    private void getWay() {
        open.add(new WayNode(start, null, 0d));

        while (!open.isEmpty()) {
            WayNode current = open.poll();

            if (current.getId().equals(end)) {
                int length = 1;
                WayNode last = current;
                while (last.getParent() != null) {
                    length++;
                    last = last.getParent();
                }

                last = current;
                String[] tay = new String[length];
                for (int i = length - 1; i > 0; i--) {
                    tay[i] = last.getId();
                    last = last.getParent();
                }
                tay[0] = start;

                System.out.println("已规划导航点路径: " + Arrays.toString(tay));

                for (String s : tay) {
                    points.add(getLoc(s));
                }
            }

            close.add(current.getId());
            List<String> list = conn.get(current.getId());
            for (String s : list) {
                if (close.contains(s)) continue;

                double expense = current.getExpense() + getDis(current.getId(), s);

                WayNode child = getNodeInOpen(s);

                if (child == null) {
                    child = new WayNode(s, current, expense);
                    open.add(child);
                }
                else {
                    if (child.getExpense() > expense) {
                        child.setExpense(expense);
                        child.setParent(current);
                    }
                }
            }
        }

        System.out.println("未找到路径");
    }

    private Location getLoc(String id) {
        if (id.equals(start)) return origin;
        if (id.equals(end)) return destination;

        return WayPointHandler.getWorlds().get(world).getPoints().get(id).getLocation();
    }

    private double getDis(String a, String b) {
        Location locA = getLoc(a);
        Location locB = getLoc(b);

        return locA.distance(locB);
    }

    private WayNode getNodeInOpen(String id) {
        for (WayNode n : open) {
            if (!n.getId().equals(id)) continue;
            return n;
        }
        return null;
    }
}
