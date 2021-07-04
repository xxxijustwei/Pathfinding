package com.taylorswiftcn.megumi.pathfinding.commands.sub.coord;

import com.taylorswiftcn.megumi.pathfinding.commands.MegumiCommand;
import com.taylorswiftcn.megumi.pathfinding.commands.PermissionType;
import com.taylorswiftcn.megumi.pathfinding.file.sub.ConfigFile;
import org.bukkit.command.CommandSender;

public class ListCommand extends MegumiCommand {
    @Override
    public void perform(CommandSender CommandSender, String[] Strings) {
        CommandSender.sendMessage(ConfigFile.Prefix + "§a所有坐标: " + getPlugin().getCoord().keySet().toString());
    }

    @Override
    public boolean playerOnly() {
        return false;
    }

    @Override
    public PermissionType getPT() {
        return null;
    }
}
