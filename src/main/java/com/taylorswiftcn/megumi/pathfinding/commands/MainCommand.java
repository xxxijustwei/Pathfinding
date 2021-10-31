package com.taylorswiftcn.megumi.pathfinding.commands;

import com.taylorswiftcn.justwei.commands.JustCommand;
import com.taylorswiftcn.megumi.pathfinding.commands.sub.*;

public class MainCommand extends JustCommand {

    public MainCommand() {
        super(new HelpCommand());
        this.register(new NavCommand());
        this.register(new NPCCommand());
        this.register(new CoordCommand());
        this.register(new VisualCommand());
        this.register(new CancelCommand());
        this.register(new ReloadCommand());
    }

}
