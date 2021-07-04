package com.taylorswiftcn.megumi.pathfinding.commands.sub.waypoint;

import com.taylorswiftcn.megumi.pathfinding.algorithm.waypoint.WayPointHandler;
import com.taylorswiftcn.megumi.pathfinding.algorithm.waypoint.constructor.WorldMap;
import com.taylorswiftcn.megumi.pathfinding.commands.MegumiCommand;
import com.taylorswiftcn.megumi.pathfinding.commands.PermissionType;
import com.taylorswiftcn.megumi.pathfinding.file.sub.ConfigFile;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddCommand extends MegumiCommand {
    @Override
    public void perform(CommandSender CommandSender, String[] Strings) {
        if (Strings.length != 3) return;

        Player player = getPlayer();
        String world = player.getWorld().getName();
        String s = Strings[2];

        if (!WayPointHandler.getWorlds().containsKey(world)) {
            WayPointHandler.addWorld(world);
        }

        WorldMap map = WayPointHandler.getWorlds().get(world);

        if (map.getPoints().containsKey(s)) {
            CommandSender.sendMessage(ConfigFile.Prefix + "§a该导航点ID已存在");
            return;
        }

        map.setPoint(s, player.getLocation());

        CommandSender.sendMessage(ConfigFile.Prefix + "§a添加成功!");

        TextComponent msg = new TextComponent(ConfigFile.Prefix + "§a是否关联临近导航点？ ");
        TextComponent button = new TextComponent("§7§l[§3关联§7§l]");
        button.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("/pf wp route %s", s)));
        button.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§a点击关联").create()));
        msg.addExtra(button);
        player.spigot().sendMessage(msg);
    }

    @Override
    public boolean playerOnly() {
        return true;
    }

    @Override
    public PermissionType getPT() {
        return null;
    }
}
