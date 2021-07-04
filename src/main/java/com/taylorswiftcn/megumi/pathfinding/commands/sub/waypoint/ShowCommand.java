package com.taylorswiftcn.megumi.pathfinding.commands.sub.waypoint;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.taylorswiftcn.megumi.pathfinding.algorithm.waypoint.WayPointHandler;
import com.taylorswiftcn.megumi.pathfinding.algorithm.waypoint.constructor.NavPoint;
import com.taylorswiftcn.megumi.pathfinding.commands.MegumiCommand;
import com.taylorswiftcn.megumi.pathfinding.commands.PermissionType;
import com.taylorswiftcn.megumi.pathfinding.file.sub.ConfigFile;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShowCommand extends MegumiCommand {

    @Override
    public void perform(CommandSender CommandSender, String[] Strings) {
        Player player = getPlayer();
        String world = player.getLocation().getWorld().getName();

        if (!WayPointHandler.getWorlds().containsKey(world)) {
            player.sendMessage(ConfigFile.Prefix + "§a当前世界没有任何导航点");
            return;
        }

        if (WayPointHandler.getWorlds().get(world).getPoints().size() == 0) {
            player.sendMessage(ConfigFile.Prefix + "§a当前世界没有任何导航点");
            return;
        }

        if (WayPointHandler.getTasks().containsKey(player.getUniqueId()) || WayPointHandler.getGrams().containsKey(player.getUniqueId())) {
            WayPointHandler.close(player);
            player.sendMessage(ConfigFile.Prefix + "§a导航点视图关闭");
            return;
        }

        addHologram(player, world);

        player.sendMessage(ConfigFile.Prefix + "§a导航点视图开启");
    }

    private void addHologram(Player player, String world) {
        List<NavPoint> points = new ArrayList<>(WayPointHandler.getWorlds().get(world).getPoints().values());

        for (NavPoint point : points) {
            String id = point.getId();
            Location loc = point.getLocation().clone().add(0, 2, 0);

            Hologram hologram = HologramsAPI.createHologram(getPlugin(), loc);
            hologram.getVisibilityManager().showTo(player);
            hologram.insertTextLine(0, "§7§l[§c§l导航点§7§l]");
            hologram.insertTextLine(1, id);

            WayPointHandler.addHologram(player, hologram);
        }
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
