package com.taylorswiftcn.megumi.pathfinding.commands.sub.waypoint;

import com.taylorswiftcn.megumi.pathfinding.algorithm.astar.MultiPointFinder;
import com.taylorswiftcn.megumi.pathfinding.commands.MegumiCommand;
import com.taylorswiftcn.megumi.pathfinding.commands.PermissionType;
import com.taylorswiftcn.megumi.pathfinding.file.sub.ConfigFile;
import com.taylorswiftcn.megumi.pathfinding.file.sub.MessageFile;
import com.taylorswiftcn.megumi.pathfinding.util.MegumiUtil;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NPCCommand extends MegumiCommand {
    @Override
    public void perform(CommandSender CommandSender, String[] Strings) {
        if (Strings.length != 3) return;
        String s = Strings[2];
        Player player = getPlayer();

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

        MultiPointFinder finder = new MultiPointFinder(player, player.getLocation(), npc.getEntity().getLocation(), npc.getEntity());
        finder.navigation();
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
