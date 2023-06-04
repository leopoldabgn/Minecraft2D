package model;

import java.util.Arrays;

// This enum is used to generate layers of ore with their probability
public enum Ore {
    COAL_ORE(BlockType.COAL_ORE, 0.02, new int[] {-4, -34}),
    DIAMOND_ORE(BlockType.DIAMOND_ORE, 0.001, new int[] {-45, -63}),
    EMERALD_ORE(BlockType.EMERALD_ORE, 0.0005, new int[] {-38, -63}),
    GOLD_ORE(BlockType.GOLD_ORE, 0.005, new int[] {-20, -63}),
    IRON_ORE(BlockType.IRON_ORE, 0.01, new int[] {-10, -35}),
    LAPIZ_LAZULI_ORE(BlockType.LAPIZ_LAZULI_ORE, 0.0075, new int[] {-20, -63}),
    REDSTONE_ORE(BlockType.REDSTONE_ORE, 0.0075, new int[] {-20, -63});

    private BlockType blockType;
    private double proba;
    private int[] layers;

    private Ore(BlockType blockType, double proba, int[] layers) {
        this.blockType = blockType;
        this.proba = proba;
        this.layers = layers;
    }

    public BlockType getBlockType() {
        return blockType;
    }

    public double getProba() {
        return proba;
    }

    public int[] getLayers() {
        return Arrays.copyOf(layers, layers.length);
    }

}
