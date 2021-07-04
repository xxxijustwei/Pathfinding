package com.taylorswiftcn.megumi.pathfinding.commands.sub.coord;

import com.taylorswiftcn.megumi.pathfinding.commands.MegumiCommand;
import com.taylorswiftcn.megumi.pathfinding.commands.PermissionType;
import com.taylorswiftcn.megumi.pathfinding.file.sub.ConfigFile;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddCommand extends MegumiCommand {
    @Override
    public void perform(CommandSender CommandSender, String[] Strings) {
        if (Strings.length != 3) return;

        String s = Strings[2];
        Player player = getPlayer();

        if (getPlugin().getCoord().containsKey(s)) {
            CommandSender.sendMessage(ConfigFile.Prefix + "§a该坐标已存在!");
            return;
        }

        getPlugin().getFileManager().add(s, player.getLocation());
        getPlugin().getCoord().put(s, player.getLocation());

        CommandSender.sendMessage(ConfigFile.Prefix + "§a添加坐标成功!");
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
