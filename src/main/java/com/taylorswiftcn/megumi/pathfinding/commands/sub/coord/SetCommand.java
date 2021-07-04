package com.taylorswiftcn.megumi.pathfinding.commands.sub.coord;

import com.taylorswiftcn.megumi.pathfinding.commands.MegumiCommand;
import com.taylorswiftcn.megumi.pathfinding.commands.PermissionType;
import com.taylorswiftcn.megumi.pathfinding.file.sub.ConfigFile;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetCommand extends MegumiCommand {
    @Override
    public void perform(CommandSender CommandSender, String[] Strings) {
        if (Strings.length != 3) return;

        Player player = getPlayer();
        String s = Strings[2];

        getPlugin().getFileManager().add(s, player.getLocation());
        getPlugin().getCoord().put(s, player.getLocation());

        CommandSender.sendMessage(ConfigFile.Prefix + "§a设置坐标成功!");
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
