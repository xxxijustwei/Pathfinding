package com.taylorswiftcn.megumi.pathfinding.commands.sub.waypoint;

import com.taylorswiftcn.megumi.pathfinding.Main;
import com.taylorswiftcn.megumi.pathfinding.algorithm.astar.MultiPointFinder;
import com.taylorswiftcn.megumi.pathfinding.commands.MegumiCommand;
import com.taylorswiftcn.megumi.pathfinding.commands.PermissionType;
import com.taylorswiftcn.megumi.pathfinding.file.sub.ConfigFile;
import com.taylorswiftcn.megumi.pathfinding.file.sub.MessageFile;
import com.taylorswiftcn.megumi.pathfinding.task.NavigationTask;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class NavCommand extends MegumiCommand {
    @Override
    public void perform(CommandSender CommandSender, String[] Strings) {
        if (Strings.length != 3) return;

        String s = Strings[2];
        Player player = getPlayer();

        if (!getPlugin().getCoord().containsKey(s)) {
            player.sendMessage(ConfigFile.Prefix + MessageFile.notExistsCoord);
            return;
        }

        Location location = getPlugin().getCoord().get(s);

        if (!player.getLocation().getWorld().equals(location.getWorld())) {
            player.sendMessage(ConfigFile.Prefix + MessageFile.notSameWorldCoord);
            return;
        }

        MultiPointFinder finder = new MultiPointFinder(player, player.getLocation(), location);
        finder.navigation();
    }

    @Override
    public boolean playerOnly() {
        return true;
    }

    @Override
    public PermissionType getPT() {
        return PermissionType.Find;
    }
}
