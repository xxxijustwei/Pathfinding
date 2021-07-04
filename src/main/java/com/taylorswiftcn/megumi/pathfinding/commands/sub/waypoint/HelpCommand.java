package com.taylorswiftcn.megumi.pathfinding.commands.sub.waypoint;

import com.taylorswiftcn.megumi.pathfinding.commands.MegumiCommand;
import com.taylorswiftcn.megumi.pathfinding.commands.PermissionType;
import com.taylorswiftcn.megumi.pathfinding.file.sub.MessageFile;
import org.bukkit.command.CommandSender;

public class HelpCommand extends MegumiCommand {
    @Override
    public void perform(CommandSender CommandSender, String[] Strings) {
        MessageFile.Help.forEach(CommandSender::sendMessage);
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
