package com.taylorswiftcn.megumi.pathfinding.commands.sub;

import com.taylorswiftcn.justwei.commands.sub.SubTabCompleter;
import com.taylorswiftcn.megumi.pathfinding.commands.CommandPerms;
import com.taylorswiftcn.megumi.pathfinding.commands.sub.coord.*;
import com.taylorswiftcn.megumi.pathfinding.commands.sub.coord.HelpCommand;

public class CoordCommand extends SubTabCompleter {

    public CoordCommand() {
        super(new HelpCommand());
        this.register(new AddCommand());
        this.register(new DelCommand());
        this.register(new SetCommand());
        this.register(new TeleportCommand());
        this.register(new ListCommand());
    }

    @Override
    public String getIdentifier() {
        return "coord";
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
