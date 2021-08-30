package com.taylorswiftcn.megumi.pathfinding.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;


public class LocationUtil {

    private static Gson gson = new Gson();

    public static String convert(Location location) {
        JsonObject obj = new JsonObject();
        obj.addProperty("world", location.getWorld().getName());
        obj.addProperty("x", location.getX());
        obj.addProperty("y", location.getY());
        obj.addProperty("z", location.getZ());

        return gson.toJson(obj);
    }

    public static Location convert(String s) {
        JsonObject obj = gson.fromJson(s, JsonObject.class);
        World world = Bukkit.getWorld(obj.get("world").toString());
        double x = Double.parseDouble(obj.get("x").toString());
        double y = Double.parseDouble(obj.get("y").toString());
        double z = Double.parseDouble(obj.get("z").toString());

        return new Location(world, x, y, z);
    }
}
