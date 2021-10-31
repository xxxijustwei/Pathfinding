package com.taylorswiftcn.megumi.pathfinding.util;

import com.taylorswiftcn.megumi.pathfinding.file.sub.ConfigFile;
import org.bukkit.Location;

public class FinderUtil {

    public static int getNavMode(Location locA, Location locB) {
        int mode = ConfigFile.Base.mode;
        if (ConfigFile.Enable.autoMode) {
            double distance = locA.distance(locB);
            if (distance >= ConfigFile.Base.autoDistance) {
                mode = 0;
            }
        }

        return mode;
    }
}
