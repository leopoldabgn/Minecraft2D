package model;

public enum PlayerType {
    STEVE("steve"),
    PIG("pig");

    private String type;

    private PlayerType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
