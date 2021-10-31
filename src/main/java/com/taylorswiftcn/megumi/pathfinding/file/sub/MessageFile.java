package com.taylorswiftcn.megumi.pathfinding.file.sub;

import com.taylorswiftcn.justwei.util.MegumiUtil;
import com.taylorswiftcn.megumi.pathfinding.Pathfinding;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;

public class MessageFile {
    private static YamlConfiguration message;

    public static List<String> Help;
    public static List<String> AdminHelp;
    public static List<String> coord;

    public static String NoPermission;
    public static String notExistsCoord;
    public static String notExistsNPC;
    public static String arriveCoord;
    public static String notSameWorldCoord;
    public static String notSameWorldNPC;
    public static String navFailure;
    public static String playerIsOffline;

    public static void init() {
        message = Pathfinding.getInstance().getFileManager().getMessage();

        Help = getStringList("Help");
        AdminHelp = getStringList("AdminHelp");
        coord = getStringList("Coord");

        NoPermission = getString("Message.NoPermission");
        notExistsCoord = getString("Message.NotExistsCoord");
        notExistsNPC = getString("Message.NotExistsNPC");
        arriveCoord = getString("Message.ArriveCoord");
        notSameWorldCoord = getString("Message.NotSameWorldCoord");
        notSameWorldNPC = getString("Message.NotSameWorldNPC");
        navFailure = getString("Message.NavFailure");
        playerIsOffline = getString("Message.PlayerIsOffline");
    }

    private static String getString(String path) {
        return MegumiUtil.onReplace(message.getString(path));
    }

    private static List<String> getStringList(String path) {
        return MegumiUtil.onReplace(message.getStringList(path));
    }


}
