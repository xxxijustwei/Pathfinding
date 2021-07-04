package com.taylorswiftcn.megumi.pathfinding.commands.sub.waypoint;

import com.taylorswiftcn.megumi.pathfinding.algorithm.waypoint.WayPointHandler;
import com.taylorswiftcn.megumi.pathfinding.algorithm.waypoint.constructor.WorldMap;
import com.taylorswiftcn.megumi.pathfinding.commands.MegumiCommand;
import com.taylorswiftcn.megumi.pathfinding.commands.PermissionType;
import com.taylorswiftcn.megumi.pathfinding.file.sub.ConfigFile;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ListCommand extends MegumiCommand {
    @Override
    public void perform(CommandSender CommandSender, String[] Strings) {
        if (Strings.length == 3) {
            String s = Strings[2];

            sendInfo(CommandSender, s);
            return;
        }

        if (CommandSender instanceof Player) {
            Player player = getPlayer();
            String world = player.getWorld().getName();

            sendInfo(player, world);
            return;
        }

        CommandSender.sendMessage(ConfigFile.Prefix + "导航点列表: ");
        for (WorldMap map : WayPointHandler.getWorlds().values()) {
            CommandSender.sendMessage(String.format("§a> 世界 [%s] : %s", map.getName(), map.getPoints().keySet().toString()));
        }
    }

    private void sendInfo(CommandSender sender, String world) {
        WorldMap map = WayPointHandler.getWorlds().get(world);
        if (map == null) {
            sender.sendMessage(ConfigFile.Prefix + "§a该世界没有导航点信息");
            return;
        }

        sender.sendMessage(ConfigFile.Prefix + String.format("§a世界 %s 的导航点: %s", world, map.getPoints().keySet().toString()));
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
