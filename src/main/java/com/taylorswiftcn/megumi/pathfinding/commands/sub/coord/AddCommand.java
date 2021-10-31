package com.taylorswiftcn.megumi.pathfinding.commands.sub.coord;

import com.taylorswiftcn.justwei.commands.sub.SubCommand;
import com.taylorswiftcn.megumi.pathfinding.Pathfinding;
import com.taylorswiftcn.megumi.pathfinding.file.sub.ConfigFile;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddCommand extends SubCommand {

    private Pathfinding plugin;

    public AddCommand() {
        this.plugin = Pathfinding.getInstance();
    }

    @Override
    public String getIdentifier() {
        return "add";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (args.length != 1) return;

        String s = args[0];
        Player player = getPlayer();

        if (plugin.getCoord().containsKey(s)) {
            sender.sendMessage(ConfigFile.Prefix + "§a该坐标已存在!");
            return;
        }

        plugin.getFileManager().add(s, player.getLocation());
        plugin.getCoord().put(s, player.getLocation());

        sender.sendMessage(ConfigFile.Prefix + "§a添加坐标成功!");
    }

    @Override
    public boolean playerOnly() {
        return true;
    }

    @Override
    public String getPermission() {
        return null;
    }
}
