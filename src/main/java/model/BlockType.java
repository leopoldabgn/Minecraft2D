package model;

public enum BlockType {
    BEDROCK("bedrock"),
    BRICK("brick"),
    CHEST("chest"),
    COAL_ORE("coal_ore"),
    CRAFTING_TABLE("crafting_table"),
    DIAMOND_ORE("diamond_ore"),
    DIRT("dirt"),
    EMERALD_ORE("emerald_ore"),
    FURNACE_OFF("furnace_off"),
    FURNACE_ON("furnace_on", 0),
    GOLD_ORE("gold_ore"),
    GRASS("grass"),
    IRON_ORE("iron_ore"),
    LAPIZ_LAZULI_ORE("lapis_lazuli_ore"),
    OAK_DOOR_BOTTOM("oak_door_bottom", 0),
    OAK_DOOR_TOP("oak_door_top"),
    OAK_LEAVES("oak_leaves"),
    OAK_LOG("oak_log"),
    OAK_PLANKS("oak_planks"),
    OAK_SAPLING("oak_sapling"),
    REDSTONE_ORE("redstone_ore"),
    STONE("stone");

    private String texture;
    private int maxStack;

    private BlockType(String texture) {
        this.texture = texture;
        this.maxStack = 64;
    }

    private BlockType(String texture, int maxStack) {
        this.texture = texture;
        this.maxStack = maxStack;
    }

    public String getTexture() {
        return texture;
    }

    public int getMaxStack() {
        return maxStack;
    }
}
