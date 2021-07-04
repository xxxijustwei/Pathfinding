package com.taylorswiftcn.megumi.pathfinding.commands.sub;

import com.taylorswiftcn.megumi.pathfinding.commands.MegumiCommand;
import com.taylorswiftcn.megumi.pathfinding.commands.PermissionType;
import com.taylorswiftcn.megumi.pathfinding.commands.sub.waypoint.*;
import com.taylorswiftcn.megumi.pathfinding.commands.sub.waypoint.HelpCommand;
import com.taylorswiftcn.megumi.pathfinding.commands.sub.waypoint.NPCCommand;
import com.taylorswiftcn.megumi.pathfinding.commands.sub.waypoint.NavCommand;
import org.bukkit.command.CommandSender;

import java.util.HashMap;

public class WayPointCommand extends MegumiCommand {

    private MegumiCommand help;
    private HashMap<String, MegumiCommand> child;

    public WayPointCommand() {
        help = new HelpCommand();
        child = new HashMap<>();
        child.put("add", new AddCommand());
        child.put("del", new DelCommand());
        child.put("route", new RouteCommand());
        child.put("tp", new TeleportCommand());
        child.put("nav", new NavCommand());
        child.put("npc", new NPCCommand());
        child.put("show", new ShowCommand());
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
