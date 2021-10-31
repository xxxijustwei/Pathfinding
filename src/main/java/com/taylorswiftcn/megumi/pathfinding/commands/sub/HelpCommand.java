package com.taylorswiftcn.megumi.pathfinding.commands.sub;

import com.taylorswiftcn.justwei.commands.sub.SubCommand;
import com.taylorswiftcn.megumi.pathfinding.commands.CommandPerms;
import com.taylorswiftcn.megumi.pathfinding.file.sub.MessageFile;
import org.bukkit.command.CommandSender;

public class HelpCommand extends SubCommand {
    @Override
    public String getIdentifier() {
        return "help";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        MessageFile.Help.forEach(sender::sendMessage);
        if (sender.hasPermission(CommandPerms.ADMIN.getNode()))
            MessageFile.AdminHelp.forEach(sender::sendMessage);
    }

    @Override
    public boolean playerOnly() {
        return false;
    }

    @Override
    public String getPermission() {
        return CommandPerms.USER.getNode();
    }
}
