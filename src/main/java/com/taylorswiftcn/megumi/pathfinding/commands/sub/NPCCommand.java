package com.taylorswiftcn.megumi.pathfinding.commands.sub;

import com.taylorswiftcn.justwei.commands.sub.SubCommand;
import com.taylorswiftcn.justwei.util.MegumiUtil;
import com.taylorswiftcn.megumi.pathfinding.Pathfinding;
import com.taylorswiftcn.megumi.pathfinding.algorithm.SearchPathManager;
import com.taylorswiftcn.megumi.pathfinding.api.NavigationAPI;
import com.taylorswiftcn.megumi.pathfinding.commands.CommandPerms;
import com.taylorswiftcn.megumi.pathfinding.file.sub.ConfigFile;
import com.taylorswiftcn.megumi.pathfinding.file.sub.MessageFile;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NPCCommand extends SubCommand {
    @Override
    public String getIdentifier() {
        return "npc";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (!Pathfinding.citizens) {
            sender.sendMessage(ConfigFile.Prefix + "§a未关联Citizens插件，无法使用该命令");
            return;
        }

        if (args.length == 1) {
            if (!(sender instanceof Player)) return;

            String s = args[0];
            Player player = getPlayer();

            if (player.isFlying()) {
                player.sendMessage(ConfigFile.Prefix + MessageFile.navFailure);
                return;
            }

            if (!MegumiUtil.isInteger(s)) return;

            int id = Integer.parseInt(s);
            NPC npc = CitizensAPI.getNPCRegistry().getById(id);
            if (npc == null) {
                player.sendMessage(ConfigFile.Prefix + MessageFile.notExistsNPC);
                return;
            }

            if (!player.getLocation().getWorld().equals(npc.getStoredLocation().getWorld())) {
                player.sendMessage(ConfigFile.Prefix + MessageFile.notSameWorldNPC);
                return;
            }

            if (SearchPathManager.getFindTask().containsKey(player.getUniqueId())) {
                SearchPathManager.getFindTask().get(player.getUniqueId()).cancel();
                SearchPathManager.getFindTask().remove(player.getUniqueId());
            }

            NavigationAPI.onNav2Player(player, npc.getStoredLocation().clone(), npc.getEntity());
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

            if (!MegumiUtil.isInteger(s2)) return;

            int id = Integer.parseInt(s2);
            NPC npc = CitizensAPI.getNPCRegistry().getById(id);
            if (npc == null) {
                sender.sendMessage(ConfigFile.Prefix + MessageFile.notExistsNPC);
                return;
            }

            if (!player.getLocation().getWorld().equals(npc.getStoredLocation().getWorld())) {
                sender.sendMessage(ConfigFile.Prefix + MessageFile.notSameWorldNPC);
                return;
            }

            if (SearchPathManager.getFindTask().containsKey(player.getUniqueId())) {
                SearchPathManager.getFindTask().get(player.getUniqueId()).cancel();
                SearchPathManager.getFindTask().remove(player.getUniqueId());
            }

            NavigationAPI.onNav2Player(player, npc.getStoredLocation().clone(), npc.getEntity());
        }
    }

    @Override
    public boolean playerOnly() {
        return false;
    }

    @Override
    public String getPermission() {
        return CommandPerms.NPC.getNode();
    }
}
