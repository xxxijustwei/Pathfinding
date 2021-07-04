package com.taylorswiftcn.megumi.pathfinding.commands.sub.waypoint;

import com.taylorswiftcn.megumi.pathfinding.algorithm.waypoint.WayPointHandler;
import com.taylorswiftcn.megumi.pathfinding.commands.MegumiCommand;
import com.taylorswiftcn.megumi.pathfinding.commands.PermissionType;
import com.taylorswiftcn.megumi.pathfinding.file.sub.ConfigFile;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportCommand extends MegumiCommand {
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

        if (!WayPointHandler.getWorlds().get(world).getPoints().containsKey(s)) {
            CommandSender.sendMessage(ConfigFile.Prefix + "§a你所在世界没有该导航点");
            return;
        }

        Location location = WayPointHandler.getWorlds().get(world).getPoints().get(s).getLocation();
        player.teleport(location);

        CommandSender.sendMessage(ConfigFile.Prefix + "§a已传送到导航点");
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
