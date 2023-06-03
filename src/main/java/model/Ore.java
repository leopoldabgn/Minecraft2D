package model;

// This enum is used to generate layers of ore with their probability
public enum Ore {
    COAL_ORE(BlockType.COAL_ORE, 0.3),
    DIAMOND_ORE(BlockType.DIAMOND_ORE, 0.03),
    EMERALD_ORE(BlockType.EMERALD_ORE, 0.02),
    GOLD_ORE(BlockType.GOLD_ORE, 0.05),
    IRON_ORE(BlockType.IRON_ORE, 0.2),
    LAPIZ_LAZULI_ORE(BlockType.LAPIZ_LAZULI_ORE, 0.1),
    REDSTONE_ORE(BlockType.REDSTONE_ORE, 0.15);

    private BlockType blockType;
    private double proba;

    private Ore(BlockType blockType, double proba) {
        this.blockType = blockType;
        this.proba = proba;
    }
}
