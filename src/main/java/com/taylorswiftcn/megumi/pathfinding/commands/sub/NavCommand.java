package com.taylorswiftcn.megumi.pathfinding.commands.sub;

import com.taylorswiftcn.justwei.commands.sub.SubCommand;
import com.taylorswiftcn.megumi.pathfinding.Pathfinding;
import com.taylorswiftcn.megumi.pathfinding.algorithm.SearchPathManager;
import com.taylorswiftcn.megumi.pathfinding.api.NavigationAPI;
import com.taylorswiftcn.megumi.pathfinding.commands.CommandPerms;
import com.taylorswiftcn.megumi.pathfinding.file.sub.ConfigFile;
import com.taylorswiftcn.megumi.pathfinding.file.sub.MessageFile;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NavCommand extends SubCommand {

    private Pathfinding plugin;

    public NavCommand() {
        this.plugin = Pathfinding.getInstance();
    }

    @Override
    public String getIdentifier() {
        return "nav";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (args.length == 1) {
            if (!(sender instanceof Player)) return;

            String id = args[0];
            Player player = getPlayer();

            if (player.isFlying()) {
                player.sendMessage(ConfigFile.Prefix + MessageFile.navFailure);
                return;
            }

            if (!plugin.getCoord().containsKey(id)) {
                player.sendMessage(ConfigFile.Prefix + MessageFile.notExistsCoord);
                return;
            }

            Location location = plugin.getCoord().get(id);

            if (!player.getLocation().getWorld().equals(location.getWorld())) {
                player.sendMessage(ConfigFile.Prefix + MessageFile.notSameWorldCoord);
                return;
            }

            if (SearchPathManager.getFindTask().containsKey(player.getUniqueId())) {
                SearchPathManager.getFindTask().get(player.getUniqueId()).cancel();
                SearchPathManager.getFindTask().remove(player.getUniqueId());
            }

            NavigationAPI.asyncNavPlayer(player, location, null);
            return;
        }

        if (args.length == 2) {
            String s1 = args[0];
            String s2 = args[1];

            Player player = Bukkit.getPlayerExact(s1);

            if (player == null) {
                sender.sendMessage(ConfigFile.Prefix + MessageFile.playerIsOffline);
                return;
            }

            if (!plugin.getCoord().containsKey(s2)) {
                player.sendMessage(ConfigFile.Prefix + MessageFile.notExistsCoord);
                return;
            }

            Location location = plugin.getCoord().get(s2);

            if (!player.getLocation().getWorld().equals(location.getWorld())) {
                player.sendMessage(ConfigFile.Prefix + MessageFile.notSameWorldCoord);
                return;
            }

            if (SearchPathManager.getFindTask().containsKey(player.getUniqueId())) {
                SearchPathManager.getFindTask().get(player.getUniqueId()).cancel();
                SearchPathManager.getFindTask().remove(player.getUniqueId());
            }

            NavigationAPI.asyncNavPlayer(player, location, null);
        }
    }

    @Override
    public boolean playerOnly() {
        return false;
    }

    @Override
    public String getPermission() {
        return CommandPerms.NAV.getNode();
    }
}
