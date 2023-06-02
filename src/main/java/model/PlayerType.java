package model;

public enum PlayerType {
    STEVE("steve");

    private String texture;

    private PlayerType(String texture) {
        this.texture = texture;
    }

    public String getTexture() {
        return texture;
    }

}
