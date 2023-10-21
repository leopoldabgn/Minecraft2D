package model;

public enum MobType {

    PLAYER("player"),

    // Mobs
    PIG("pig");

    private String type;

    private MobType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
