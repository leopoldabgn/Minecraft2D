package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Game implements Serializable {
    
    private static final long serialVersionUID = 11111L;

    public static String DEFAULT_GAME_NAME = "minecraft2d.save";

    private ArrayList<Player> players = new ArrayList<>();
    private ArrayList<Mob> mobs = new ArrayList<>(); // -----------
    private Player mainPlayer;
    private Map map;
    

    public Game(Player player, int mapWidth, int mapHeight) {
        this.mainPlayer = player;
        players.add(player); // Ajout d'un joueur
        //////
        // mobs.add(Mob.createMob(MobType.PIG, new Point(-5, -10))); // Ajout d'un mob
        //////
        this.map = MapGenerator.underground(this, mapWidth, mapHeight);
    }

    public Game(ArrayList<Player> players, int mapWidth, int mapHeight) {
        this.mainPlayer = players.get(0);
        this.map = MapGenerator.underground(this, mapWidth, mapHeight);
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

    public ArrayList<Mob> getMobs() {
        return mobs;
    }

    public ItemsBar getPlayerItemsBar() {
        return getMainPlayer().getInventory().getItemsBar();
    }

    @Override
    public String toString() {
        return "Map: "+map;
    }

    // Sauvegarder le jeu dans un fichier à l'aide de la sérialisation
	public void save(String name)
	{
		ObjectOutputStream oos = null;
		File file = new File(name);
		try {
			oos =  new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(this);
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
			
		}
	}
	
    // On récupère l'objet game enregistré dans le fichier "name"
	public static Game load(String name)
	{
		Game game = null;
		ObjectInputStream ois = null;
		File file = new File(name);
		if(file.exists())
		{
			try {
				ois =  new ObjectInputStream(new FileInputStream(file)) ;
				game = (Game)ois.readObject();
				ois.close();
			} catch (IOException | ClassNotFoundException e) {
                System.err.println("Cannot load game \""+name+"\" : ");
				e.printStackTrace();
			}	
		}
        
        // Si la partie a ete charge avec succes
        if(game != null) {
            // On vérifie que les mobs n'étaient pas en plein saut lors de la sauvegarde
            // On provoque une chute pour tous les mobs présents sur la map (les joueurs également)
            for(Player p : game.getPlayers()) {
                p.resetMobVelocity();
            }

            for(Mob m : game.getMobs()) {
                m.resetMobVelocity();
            }
        }

		return game;
	}

	public void saveLastGame() {
		save(DEFAULT_GAME_NAME);
	}

	public static Game loadLastGame() {
		return load(DEFAULT_GAME_NAME);
	}

}
