package com.taylorswiftcn.megumi.pathfinding.util.special;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.taylorswiftcn.megumi.pathfinding.Main;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class EntityGlowUtil {

    private static Main plugin = Main.getInstance();

    public static void glow(Player player, Entity entity) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
        packet.getIntegers().write(0, entity.getEntityId());
        WrappedDataWatcher watcher = new WrappedDataWatcher();
        WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Byte.class);
        watcher.setEntity(player);
        watcher.setObject(0, serializer, (byte) (0x40));
        packet.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());
        try {
            plugin.getPm().sendServerPacket(player, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void unGlow(Player player, Entity entity) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
        packet.getIntegers().write(0, entity.getEntityId());
        WrappedDataWatcher watcher = new WrappedDataWatcher();
        WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Byte.class);
        watcher.setEntity(player);
        watcher.setObject(0, serializer, (byte) (0x0));
        packet.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());
        try {
            plugin.getPm().sendServerPacket(player, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
