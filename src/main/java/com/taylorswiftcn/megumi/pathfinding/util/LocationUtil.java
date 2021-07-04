package com.taylorswiftcn.megumi.pathfinding.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;


public class LocationUtil {

    public static String convert(Location location) {
        JSONObject obj = new JSONObject();
        obj.put("world", location.getWorld().getName());
        obj.put("x", location.getX());
        obj.put("y", location.getY());
        obj.put("z", location.getZ());

        return obj.toJSONString();
    }

    public static Location convert(String s) {
        JSONObject obj = (JSONObject) JSONValue.parse(s);
        World world = Bukkit.getWorld(obj.get("world").toString());
        double x = Double.parseDouble(obj.get("x").toString());
        double y = Double.parseDouble(obj.get("y").toString());
        double z = Double.parseDouble(obj.get("z").toString());

        return new Location(world, x, y, z);
    }
}
