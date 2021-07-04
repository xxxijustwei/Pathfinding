package com.taylorswiftcn.megumi.pathfinding.commands.sub.coord;

import com.taylorswiftcn.megumi.pathfinding.commands.MegumiCommand;
import com.taylorswiftcn.megumi.pathfinding.commands.PermissionType;
import com.taylorswiftcn.megumi.pathfinding.file.sub.ConfigFile;
import org.bukkit.command.CommandSender;

public class DelCommand extends MegumiCommand {
    @Override
    public void perform(CommandSender CommandSender, String[] Strings) {
        if (Strings.length != 3) return;

        String s = Strings[2];

        if (!getPlugin().getCoord().containsKey(s)) {
            CommandSender.sendMessage(ConfigFile.Prefix + "§a没有该坐标!");
            return;
        }

        if (getPlugin().getCoord().containsKey(s)) {
            getPlugin().getCoord().remove(s);
            getPlugin().getFileManager().del(s);
        }

        CommandSender.sendMessage(ConfigFile.Prefix + "§a删除坐标成功!");
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
