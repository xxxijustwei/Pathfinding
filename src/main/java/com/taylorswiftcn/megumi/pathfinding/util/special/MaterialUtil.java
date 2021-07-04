package com.taylorswiftcn.megumi.pathfinding.util.special;

import org.bukkit.Material;

public class MaterialUtil {

    public static boolean isStep(Material material) {
        switch (material) {
            case STEP:
            case WOOD_STEP:
            case STONE_SLAB2:
            case PURPUR_SLAB:
                return true;
        }

        return false;
    }

    public static boolean isDoorOrGate(Material material) {
        switch (material) {
            case DARK_OAK_DOOR:
            case IRON_DOOR:
            case WOOD_DOOR:
            case BIRCH_DOOR:
            case ACACIA_DOOR:
            case JUNGLE_DOOR:
            case SPRUCE_DOOR:
            case WOODEN_DOOR:
            case FENCE_GATE:
            case BIRCH_FENCE_GATE:
            case ACACIA_FENCE_GATE:
            case JUNGLE_FENCE_GATE:
            case SPRUCE_FENCE_GATE:
            case DARK_OAK_FENCE_GATE:
                return true;
        }

        return false;
    }

    public static boolean isFence(Material material) {
        switch (material) {
            case FENCE:
            case ACACIA_FENCE:
            case BIRCH_FENCE:
            case IRON_FENCE:
            case DARK_OAK_FENCE:
            case JUNGLE_FENCE:
            case NETHER_FENCE:
            case SPRUCE_FENCE:
            case FENCE_GATE:
            case BIRCH_FENCE_GATE:
            case ACACIA_FENCE_GATE:
            case JUNGLE_FENCE_GATE:
            case SPRUCE_FENCE_GATE:
            case DARK_OAK_FENCE_GATE:
                return true;
        }

        return false;
    }

    public static boolean isWall(Material material) {
        return material == Material.COBBLE_WALL;
    }
}
