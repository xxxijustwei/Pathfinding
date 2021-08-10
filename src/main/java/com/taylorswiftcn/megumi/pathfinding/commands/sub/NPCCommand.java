package com.taylorswiftcn.megumi.pathfinding.commands.sub;

import com.taylorswiftcn.megumi.pathfinding.Main;
import com.taylorswiftcn.megumi.pathfinding.algorithm.SearchPathManager;
import com.taylorswiftcn.megumi.pathfinding.api.PlayerNavigation;
import com.taylorswiftcn.megumi.pathfinding.commands.MegumiCommand;
import com.taylorswiftcn.megumi.pathfinding.commands.PermissionType;
import com.taylorswiftcn.megumi.pathfinding.file.sub.ConfigFile;
import com.taylorswiftcn.megumi.pathfinding.file.sub.MessageFile;
import com.taylorswiftcn.megumi.pathfinding.util.MegumiUtil;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class NPCCommand extends MegumiCommand {
    @Override
    public void perform(CommandSender CommandSender, String[] Strings) {
        if (!Main.citizens) {
            CommandSender.sendMessage(ConfigFile.Prefix + "§a未关联Citizens插件，无法使用该命令");
            return;
        }

        if (Strings.length == 2) {
            if (!(CommandSender instanceof Player)) return;

            String s = Strings[1];
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

            new BukkitRunnable() {
                @Override
                public void run() {
                    PlayerNavigation nav = new PlayerNavigation(player, npc.getStoredLocation().clone(), npc.getEntity());
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

            if (!MegumiUtil.isInteger(s2)) return;

            int id = Integer.parseInt(s2);
            NPC npc = CitizensAPI.getNPCRegistry().getById(id);
            if (npc == null) {
                CommandSender.sendMessage(ConfigFile.Prefix + MessageFile.notExistsNPC);
                return;
            }

            if (!player.getLocation().getWorld().equals(npc.getStoredLocation().getWorld())) {
                CommandSender.sendMessage(ConfigFile.Prefix + MessageFile.notSameWorldNPC);
                return;
            }

            if (SearchPathManager.getFindTask().containsKey(player.getUniqueId())) {
                SearchPathManager.getFindTask().get(player.getUniqueId()).cancel();
                SearchPathManager.getFindTask().remove(player.getUniqueId());
            }

            new BukkitRunnable() {
                @Override
                public void run() {
                    PlayerNavigation nav = new PlayerNavigation(player, npc.getStoredLocation().clone(), npc.getEntity());
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
        return PermissionType.Npc;
    }
}
