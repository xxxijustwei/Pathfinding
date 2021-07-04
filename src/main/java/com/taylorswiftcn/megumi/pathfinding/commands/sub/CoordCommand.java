package com.taylorswiftcn.megumi.pathfinding.commands.sub;

import com.taylorswiftcn.megumi.pathfinding.commands.MegumiCommand;
import com.taylorswiftcn.megumi.pathfinding.commands.PermissionType;
import com.taylorswiftcn.megumi.pathfinding.commands.sub.coord.*;
import com.taylorswiftcn.megumi.pathfinding.commands.sub.coord.HelpCommand;
import org.bukkit.command.CommandSender;

import java.util.HashMap;

public class CoordCommand extends MegumiCommand {

    private MegumiCommand help;
    private HashMap<String, MegumiCommand> child;

    public CoordCommand() {
        help = new HelpCommand();
        child = new HashMap<>();
        child.put("add", new AddCommand());
        child.put("del", new DelCommand());
        child.put("set", new SetCommand());
        child.put("tp", new TeleportCommand());
        child.put("list", new ListCommand());
    }

    @Override
    public void perform(CommandSender CommandSender, String[] Strings) {
        MegumiCommand command = help;
        if (Strings.length >= 2 && child.containsKey(Strings[1])) {
            command = child.get(Strings[1]);
        }

        command.execute(CommandSender, Strings);
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
