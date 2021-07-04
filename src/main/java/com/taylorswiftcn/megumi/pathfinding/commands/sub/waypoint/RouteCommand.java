package com.taylorswiftcn.megumi.pathfinding.commands.sub.waypoint;

import com.taylorswiftcn.megumi.pathfinding.algorithm.waypoint.WayPointHandler;
import com.taylorswiftcn.megumi.pathfinding.algorithm.waypoint.constructor.WorldMap;
import com.taylorswiftcn.megumi.pathfinding.commands.MegumiCommand;
import com.taylorswiftcn.megumi.pathfinding.commands.PermissionType;
import com.taylorswiftcn.megumi.pathfinding.file.sub.ConfigFile;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RouteCommand extends MegumiCommand {
    @Override
    public void perform(CommandSender CommandSender, String[] Strings) {
        if (Strings.length != 3) return;

        Player player = getPlayer();
        String world = player.getWorld().getName();
        String s = Strings[2];

        if (!WayPointHandler.getWorlds().containsKey(world)) return;

        WorldMap map = WayPointHandler.getWorlds().get(world);

        player.sendMessage(ConfigFile.Prefix + "§a开始关联...");
        map.routing(player, s);
    }

    @Override
    public boolean playerOnly() {
        return true;
    }

    @Override
    public PermissionType getPT() {
        return null;
    }
}
