package com.taylorswiftcn.megumi.pathfinding.commands.sub.waypoint;

import com.taylorswiftcn.megumi.pathfinding.algorithm.waypoint.WayPointHandler;
import com.taylorswiftcn.megumi.pathfinding.algorithm.waypoint.constructor.WorldMap;
import com.taylorswiftcn.megumi.pathfinding.commands.MegumiCommand;
import com.taylorswiftcn.megumi.pathfinding.commands.PermissionType;
import com.taylorswiftcn.megumi.pathfinding.file.sub.ConfigFile;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DelCommand extends MegumiCommand {
    @Override
    public void perform(CommandSender CommandSender, String[] Strings) {
        if (Strings.length != 3) return;

        Player player = getPlayer();
        String world = player.getWorld().getName();
        String s = Strings[2];

        if (!WayPointHandler.getWorlds().containsKey(world)) {
            CommandSender.sendMessage(ConfigFile.Prefix + "§a你所在世界没有任何导航点");
            return;
        }

        WorldMap map = WayPointHandler.getWorlds().get(world);

        if (!map.getPoints().containsKey(s)) {
            CommandSender.sendMessage(ConfigFile.Prefix + "§a没有该导航点");
            return;
        }

        map.delPoint(s);
        CommandSender.sendMessage(ConfigFile.Prefix + "§a删除成功!");
    }

    @Override
    public boolean playerOnly() {
        return false;
    }

    @Override
    public PermissionType getPT() {
        return null;
    }
}
