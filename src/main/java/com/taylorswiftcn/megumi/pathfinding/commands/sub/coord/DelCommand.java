package com.taylorswiftcn.megumi.pathfinding.commands.sub.coord;

import com.taylorswiftcn.justwei.commands.sub.SubCommand;
import com.taylorswiftcn.megumi.pathfinding.Pathfinding;
import com.taylorswiftcn.megumi.pathfinding.file.sub.ConfigFile;
import org.bukkit.command.CommandSender;

public class DelCommand extends SubCommand {

    private Pathfinding plugin;

    public DelCommand() {
        this.plugin = Pathfinding.getInstance();
    }

    @Override
    public String getIdentifier() {
        return "del";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (args.length != 1) return;

        String s = args[0];

        if (!plugin.getCoord().containsKey(s)) {
            sender.sendMessage(ConfigFile.Prefix + "§a没有该坐标!");
            return;
        }

        if (plugin.getCoord().containsKey(s)) {
            plugin.getCoord().remove(s);
            plugin.getFileManager().del(s);
        }

        sender.sendMessage(ConfigFile.Prefix + "§a删除坐标成功!");
    }

    @Override
    public boolean playerOnly() {
        return false;
    }

    @Override
    public String getPermission() {
        return null;
    }
}
