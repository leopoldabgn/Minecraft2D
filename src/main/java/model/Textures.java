package model;

import java.awt.Image;
import java.io.IOException;
import java.util.HashMap;

import launcher.App;

public class Textures {
    
    private static String BLOCKS = "blocks/",
                          PLAYERS = "players/",
                          EXT_FILE = ".png";

    private static Image DEFAULT_TEXTURE;

    private static HashMap<String, Image> PLAYER_TEXTURES = new HashMap<>();
    private static HashMap<String, Image> BLOCK_TEXTURES = new HashMap<>();

    // Faire en sorte de Load uniquement les textures dont a besoin ?
    public Textures() {
        try {
            DEFAULT_TEXTURE = App.getImage("default.png");
        } catch (IOException e) {
            System.err.println("Impossible de charger les textures...");
            System.exit(1);
        }
    }

    public static Image getDefaultTexture() {
        return DEFAULT_TEXTURE;
    }

    // Permet de créer un objet Image pour la texture
    // Si elle a deja ete chargé, on renvoie directement
    // son objet Image correspondant dans le dictionnaire
    public static Image loadBlockTexture(String texture) {
        Image img = BLOCK_TEXTURES.get(texture);
        if(img == null) {
            try {
                img = App.getImage(BLOCKS+texture+EXT_FILE);
            } catch (IOException e) {
                System.err.println("Erreur ouverture image: "+BLOCKS+texture+EXT_FILE);
                img = DEFAULT_TEXTURE;
            }
            BLOCK_TEXTURES.put(texture, img);
        }
        return img;
    }

    // Fonctionne pareil que loadBlockTexture
    public static Image loadPlayerTexture(PlayerType playerType, String texture) {
        texture = playerType.getType()+"/"+texture;
        Image img = PLAYER_TEXTURES.get(texture);
        if(img == null) {
            try {
                img = App.getImage(PLAYERS+texture+EXT_FILE);
            } catch (IOException e) {
                System.err.println("Erreur ouverture image: "+PLAYERS+texture+EXT_FILE);
                img = DEFAULT_TEXTURE;
            }
            PLAYER_TEXTURES.put(texture, img);
        }
        return img;
    }

}
