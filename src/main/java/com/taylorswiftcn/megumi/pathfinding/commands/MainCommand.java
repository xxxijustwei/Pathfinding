package com.taylorswiftcn.megumi.pathfinding.commands;

import com.taylorswiftcn.megumi.pathfinding.commands.sub.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;

public class MainCommand implements CommandExecutor {
    private HelpCommand help;
    private HashMap<String, MegumiCommand> commands;

    public MainCommand() {
        this.help = new HelpCommand();
        this.commands = new HashMap<>();
        this.commands.put("coord", new CoordCommand());
        /*this.commands.put("wp", new WayPointCommand());*/
        this.commands.put("nav", new NavCommand());
        this.commands.put("npc", new NPCCommand());
        /*this.commands.put("visual", new VisualCommand());
        this.commands.put("cancel", new CancelCommand());*/
        this.commands.put("reload", new ReloadCommand());
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        MegumiCommand cmd = help;
        if (strings.length >= 1 && commands.containsKey(strings[0])) {
            cmd = commands.get(strings[0]);
        }
        cmd.execute(commandSender, strings);
        return false;
    }
}
