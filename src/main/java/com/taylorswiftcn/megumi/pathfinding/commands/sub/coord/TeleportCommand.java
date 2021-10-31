package com.taylorswiftcn.megumi.pathfinding.commands.sub.coord;

import com.taylorswiftcn.justwei.commands.sub.SubCommand;
import com.taylorswiftcn.megumi.pathfinding.Pathfinding;
import com.taylorswiftcn.megumi.pathfinding.file.sub.ConfigFile;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportCommand extends SubCommand {

    private Pathfinding plugin;

    public TeleportCommand() {
        this.plugin = Pathfinding.getInstance();
    }

    @Override
    public String getIdentifier() {
        return "tp";
    }

    @Override
    public void perform(CommandSender CommandSender, String[] Strings) {
        if (Strings.length != 3) return;

        Player player = getPlayer();
        String s = Strings[2];

        if (!plugin.getCoord().containsKey(s)) {
            CommandSender.sendMessage(ConfigFile.Prefix + "§a没有该坐标!");
            return;
        }

        Location location = plugin.getCoord().get(s);
        player.teleport(location);

        CommandSender.sendMessage(ConfigFile.Prefix + "§a已传送到坐标!");
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
