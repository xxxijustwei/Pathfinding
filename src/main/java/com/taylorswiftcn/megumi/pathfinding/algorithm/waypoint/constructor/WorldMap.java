package com.taylorswiftcn.megumi.pathfinding.algorithm.waypoint.constructor;

import com.taylorswiftcn.megumi.pathfinding.Main;
import com.taylorswiftcn.megumi.pathfinding.algorithm.astar.AStarFinder;
import com.taylorswiftcn.megumi.pathfinding.file.sub.ConfigFile;
import com.taylorswiftcn.megumi.pathfinding.util.LocationUtil;
import lombok.Data;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
public class WorldMap {

    private String name;
    private YamlConfiguration yaml;
    private HashMap<String, NavPoint> points;
    private HashMap<String, List<String>> conn;

    public WorldMap(File file) {
        name = file.getName();
        points = new HashMap<>();
        conn = new HashMap<>();
        yaml = YamlConfiguration.loadConfiguration(file);

        ConfigurationSection sectionA = yaml.getConfigurationSection("Point");
        if (sectionA != null) {
            for (String s : sectionA.getKeys(false)) {
                Location loc = LocationUtil.convert(sectionA.getString(s));

                points.put(s, new NavPoint(s, loc));
            }
        }

        ConfigurationSection sectionB = yaml.getConfigurationSection("Conn");
        if (sectionB != null) {
            for (String s : sectionB.getKeys(false)) {

                List<String> list = new ArrayList<>();
                JSONArray array = (JSONArray) JSONValue.parse(sectionB.getString(s));
                for (Object obj : array) {
                    list.add(obj.toString());
                }

                conn.put(s, list);
            }
        }
    }

    public void routing(Player player, String id) {
        Location location = points.get(id).getLocation();

        for (NavPoint point : points.values()) {
            String s = point.getId();
            if (s.equals(id)) continue;

            Location site = point.getLocation();
            double distance = site.distance(location);
            if (distance > 75) continue;

            AStarFinder astar = new AStarFinder(null, location, site);
            long time = astar.testCalculationTime();

            if (time > 20L) continue;

            addConn(id, s);

            player.sendMessage(ConfigFile.Prefix + String.format("%s 点到 %s 点距离[%s]导航用时[%sms],以关联两点为临近点", id, s, (int) distance, time));
        }

        save();
    }

    public void setPoint(String id, Location location) {
        yaml.set("Point." + id, LocationUtil.convert(location));
        save();

        points.put(id, new NavPoint(id, location));
        conn.put(id, new ArrayList<>());
    }

    public void delPoint(String id) {
        yaml.set("Point." + id, null);
        delConn(id);
        save();

        points.remove(id);
        conn.remove(id);
    }

    private void addConn(String a, String b) {
        conn.get(a).add(b);
        conn.get(b).add(a);

        yaml.set("Conn." + a, JSONValue.toJSONString(conn.get(a)));
        yaml.set("Conn." + b, JSONValue.toJSONString(conn.get(b)));
    }

    private void delConn(String a) {
        for (String s : conn.keySet()) {
            if (!conn.get(s).contains(a) || s.equals(a)) continue;

            conn.get(s).remove(a);
            yaml.set("Conn." + s, JSONValue.toJSONString(conn.get(s)));
        }

        yaml.set("Conn." + a, null);
    }

    private void save() {
        File file = new File(Main.getInstance().getDataFolder(), "point/" + name);
        try {
            yaml.save(file);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
