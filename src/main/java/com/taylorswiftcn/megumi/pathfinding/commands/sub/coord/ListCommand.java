package com.taylorswiftcn.megumi.pathfinding.commands.sub.coord;

import com.taylorswiftcn.justwei.commands.sub.SubCommand;
import com.taylorswiftcn.megumi.pathfinding.Pathfinding;
import com.taylorswiftcn.megumi.pathfinding.file.sub.ConfigFile;
import org.bukkit.command.CommandSender;

public class ListCommand extends SubCommand {

    private Pathfinding plugin;

    public ListCommand() {
        this.plugin = Pathfinding.getInstance();
    }

    @Override
    public String getIdentifier() {
        return "list";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        sender.sendMessage(ConfigFile.Prefix + "§a所有坐标: " + plugin.getCoord().keySet());
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
