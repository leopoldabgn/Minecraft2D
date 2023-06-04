package model;

public enum PlayerType {
    STEVE("steve");

    private String type;

    private PlayerType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
