package com.taylorswiftcn.megumi.pathfinding.commands.sub;

import com.taylorswiftcn.megumi.pathfinding.algorithm.SearchPathManager;
import com.taylorswiftcn.megumi.pathfinding.commands.MegumiCommand;
import com.taylorswiftcn.megumi.pathfinding.commands.PermissionType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VisualCommand extends MegumiCommand {
    @Override
    public void perform(CommandSender CommandSender, String[] Strings) {
        Player player = getPlayer();
        SearchPathManager.switchVisual(player);
    }

    @Override
    public boolean playerOnly() {
        return true;
    }

    @Override
    public PermissionType getPT() {
        return PermissionType.Admin;
    }
}
