package com.taylorswiftcn.megumi.pathfinding.commands.sub;

import com.taylorswiftcn.megumi.pathfinding.commands.MegumiCommand;
import com.taylorswiftcn.megumi.pathfinding.commands.PermissionType;
import com.taylorswiftcn.megumi.pathfinding.file.sub.ConfigFile;
import org.bukkit.command.CommandSender;

public class ReloadCommand extends MegumiCommand {
    @Override
    public void perform(CommandSender CommandSender, String[] Strings) {
        getPlugin().reload();
        CommandSender.sendMessage(ConfigFile.Prefix + "§a重载成功!");
    }

    @Override
    public boolean playerOnly() {
        return false;
    }

    @Override
    public PermissionType getPT() {
        return PermissionType.Admin;
    }
}
