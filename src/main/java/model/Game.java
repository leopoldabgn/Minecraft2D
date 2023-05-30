package model;

import java.util.ArrayList;

public class Game {
    
    private ArrayList<Player> players = new ArrayList<>();
    private Map map;

    public Game(Player player) {
        players.add(player);
        this.map = Map.create(this);
    }

    public Game(ArrayList<Player> players) {
        this.map = Map.create(this);
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public Map getMap() {
        return map;
    }

    @Override
    public String toString() {
        return "Map: "+map;
    }

}
