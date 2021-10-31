package com.taylorswiftcn.megumi.pathfinding.commands.sub;

import com.taylorswiftcn.justwei.commands.sub.SubCommand;
import com.taylorswiftcn.megumi.pathfinding.algorithm.SearchPathManager;
import com.taylorswiftcn.megumi.pathfinding.commands.CommandPerms;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VisualCommand extends SubCommand {

    @Override
    public String getIdentifier() {
        return "visual";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        Player player = getPlayer();
        SearchPathManager.switchVisual(player);
    }

    @Override
    public boolean playerOnly() {
        return true;
    }

    @Override
    public String getPermission() {
        return CommandPerms.ADMIN.getNode();
    }
}
