package com.taylorswiftcn.megumi.pathfinding.commands.sub;

import com.taylorswiftcn.justwei.commands.sub.SubCommand;
import com.taylorswiftcn.megumi.pathfinding.Pathfinding;
import com.taylorswiftcn.megumi.pathfinding.commands.CommandPerms;
import com.taylorswiftcn.megumi.pathfinding.file.sub.ConfigFile;
import org.bukkit.command.CommandSender;

public class ReloadCommand extends SubCommand {

    private Pathfinding plugin;

    public ReloadCommand() {
        this.plugin = Pathfinding.getInstance();
    }

    @Override
    public String getIdentifier() {
        return "reload";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        plugin.reload();
        sender.sendMessage(ConfigFile.Prefix + "§a重载成功!");
    }

    @Override
    public boolean playerOnly() {
        return false;
    }

    @Override
    public String getPermission() {
        return CommandPerms.ADMIN.getNode();
    }
}
