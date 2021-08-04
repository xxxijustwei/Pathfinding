package com.taylorswiftcn.megumi.pathfinding.commands.sub;

import com.taylorswiftcn.megumi.pathfinding.algorithm.SearchPathManager;
import com.taylorswiftcn.megumi.pathfinding.api.PlayerNavigation;
import com.taylorswiftcn.megumi.pathfinding.commands.MegumiCommand;
import com.taylorswiftcn.megumi.pathfinding.commands.PermissionType;
import com.taylorswiftcn.megumi.pathfinding.file.sub.ConfigFile;
import com.taylorswiftcn.megumi.pathfinding.file.sub.MessageFile;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class NavCommand extends MegumiCommand {
    @Override
    public void perform(CommandSender CommandSender, String[] Strings) {
        if (Strings.length == 2) {
            if (!(CommandSender instanceof Player)) return;

            String id = Strings[1];
            Player player = getPlayer();

            if (player.isFlying()) {
                player.sendMessage(ConfigFile.Prefix + MessageFile.navFailure);
                return;
            }

            if (!getPlugin().getCoord().containsKey(id)) {
                player.sendMessage(ConfigFile.Prefix + MessageFile.notExistsCoord);
                return;
            }

            Location location = getPlugin().getCoord().get(id);

            if (!player.getLocation().getWorld().equals(location.getWorld())) {
                player.sendMessage(ConfigFile.Prefix + MessageFile.notSameWorldCoord);
                return;
            }

            if (SearchPathManager.getFindTask().containsKey(player.getUniqueId())) {
                SearchPathManager.getFindTask().get(player.getUniqueId()).cancel();
                SearchPathManager.getFindTask().remove(player.getUniqueId());
            }

            new BukkitRunnable() {
                @Override
                public void run() {
                    PlayerNavigation nav = new PlayerNavigation(player, location, null, -1);
                    nav.start();
                }
            }.runTaskAsynchronously(getPlugin());
            return;
        }

        if (Strings.length == 3) {
            String s1 = Strings[1];
            String s2 = Strings[2];

            Player player = Bukkit.getPlayerExact(s1);

            if (player == null) {
                CommandSender.sendMessage(ConfigFile.Prefix + MessageFile.playerIsOffline);
                return;
            }

            if (!getPlugin().getCoord().containsKey(s2)) {
                player.sendMessage(ConfigFile.Prefix + MessageFile.notExistsCoord);
                return;
            }

            Location location = getPlugin().getCoord().get(s2);

            if (!player.getLocation().getWorld().equals(location.getWorld())) {
                player.sendMessage(ConfigFile.Prefix + MessageFile.notSameWorldCoord);
                return;
            }

            if (SearchPathManager.getFindTask().containsKey(player.getUniqueId())) {
                SearchPathManager.getFindTask().get(player.getUniqueId()).cancel();
                SearchPathManager.getFindTask().remove(player.getUniqueId());
            }

            new BukkitRunnable() {
                @Override
                public void run() {
                    PlayerNavigation nav = new PlayerNavigation(player, location, null, -1);
                    nav.start();
                }
            }.runTaskAsynchronously(getPlugin());
        }
    }

    @Override
    public boolean playerOnly() {
        return false;
    }

    @Override
    public PermissionType getPT() {
        return PermissionType.Nav;
    }
}
