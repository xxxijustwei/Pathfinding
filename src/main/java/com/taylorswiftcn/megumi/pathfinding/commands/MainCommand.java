package com.taylorswiftcn.megumi.pathfinding.commands;

import com.taylorswiftcn.megumi.pathfinding.commands.sub.*;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class MainCommand implements TabExecutor {
    private HelpCommand help;
    private HashMap<String, MegumiCommand> commands;

    public MainCommand() {
        this.help = new HelpCommand();
        this.commands = new HashMap<>();
        this.commands.put("coord", new CoordCommand());
        /*this.commands.put("wp", new WayPointCommand());*/
        this.commands.put("nav", new NavCommand());
        this.commands.put("npc", new NPCCommand());
        this.commands.put("visual", new VisualCommand());
        this.commands.put("cancel", new CancelCommand());
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

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length > 1) return null;

        List<String> keys = new ArrayList<>(commands.keySet());
        if (args.length == 0) return keys;

        return keys.stream().filter(s -> StringUtils.startsWithIgnoreCase(s, args[0])).collect(Collectors.toList());
    }
}
