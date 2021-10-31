package com.taylorswiftcn.megumi.pathfinding.commands.sub.coord;

import com.taylorswiftcn.justwei.commands.sub.SubCommand;
import com.taylorswiftcn.megumi.pathfinding.Pathfinding;
import com.taylorswiftcn.megumi.pathfinding.file.sub.ConfigFile;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetCommand extends SubCommand {

    private Pathfinding plugin;

    public SetCommand() {
        this.plugin = Pathfinding.getInstance();
    }

    @Override
    public String getIdentifier() {
        return "set";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (args.length != 1) return;

        Player player = getPlayer();
        String s = args[0];

        plugin.getFileManager().add(s, player.getLocation());
        plugin.getCoord().put(s, player.getLocation());

        sender.sendMessage(ConfigFile.Prefix + "§a设置坐标成功!");
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
