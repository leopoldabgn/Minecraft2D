package model;

import java.util.ArrayList;

public class Game {
    
    private ArrayList<Player> players = new ArrayList<>();
    private Player mainPlayer;
    private Map map;

    public Game(Player player) {
        this.mainPlayer = player;
        players.add(player);
        this.map = Map.create(this);
    }

    public Game(ArrayList<Player> players) {
        this.mainPlayer = players.get(0);
        this.map = Map.create(this);
        // Par défaut les joueurs sont tous en (0, 0)
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public Map getMap() {
        return map;
    }

    public Player getMainPlayer() {
        return mainPlayer;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    @Override
    public String toString() {
        return "Map: "+map;
    }

}
