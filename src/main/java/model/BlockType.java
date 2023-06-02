package model;

public enum BlockType {
    GRASS("grass"),
    STONE("stone"),
    BRICK("brick");

    private String texture;

    private BlockType(String texture) {
        this.texture = texture;
    }

    public String getTexture() {
        return texture;
    }

}
