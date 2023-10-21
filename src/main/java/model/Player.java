package model;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;

public class Player extends Mob {
    
    private PlayerType type = PlayerType.STEVE;
    private String pseudo;
    private int score;
    private Inventory inventory = new Inventory();

    private Player(PlayerType type, String pseudo, Point position) {
        super(MobType.PLAYER, position);
        this.type = type;
        this.pseudo = pseudo;
    }

    public static Player createPlayer(PlayerType type, String pseudo) {
        return new Player(type, pseudo, new Point(0, -1));
    }

    public static Player createPlayer(PlayerType type, String pseudo, Point position) {
        return new Player(type, pseudo, position);
    }

    public static class Inventory {
        private ArrayList<Item> items = new ArrayList<>();
        private int maxSlots = 9; // Pour l'instant juste le nombre de slots de la barre d'items
        private ItemsBar itemsBar = new ItemsBar();

        public Inventory() {}

        public Item getItemWithMinNb(Item item) {
            Item minItem = null;
            for(Item it : items) {
                if(item.equals(it)) {
                    if(minItem == null || it.getNb() < minItem.getNb())
                        minItem = it;
                }
            }
            return minItem;
        }

        // On ajoute l'item a l'inventaire
        // Cette fonction se debrouille aussi pour le mettre dans la barre
        // d'items si necessaire !
        public boolean addItem(Item item) {
            Item minItem = getItemWithMinNb(item);
            if(minItem == null) {
                if(items.size() == maxSlots)
                    return false;
                items.add(item);
                itemsBar.addItem(item);
                return true;
            }
            int remainingBFull = minItem.remainingBeforeFull();
            int toAdd = Math.min(item.getNb(), remainingBFull);
            minItem.add(toAdd); // remainingBFull can be 0
            item.add(-toAdd); // On enleve ce qu'on vient d'ajouter

            // Si il reste un nombre > 0 d'items a ajouter, on ajoute un nouvel
            // objet Item a la liste
            if(item.getNb() > 0) {
                minItem.add(item.getNb());
            }
            return true;
        }

        public ItemsBar getItemsBar() {
            return itemsBar;
        }

    }

    // origin represente la coordonnée en haut a gauche de l'écran
    // On calcule les coordonnées de l'objet sur l'écran en partant de ce point
    // On écrase la méthode défini dans Mob
    @Override
    public void draw(Graphics g, Point origin) {
        int x = (int)(getRealX() - origin.getX());
        int y = (int)(getRealY() - origin.getY());

        // Get Image object for texture
        Image img = Textures.loadPlayerTexture(type, texture);
        g.drawImage(img, x, y, getSize(), getSize(), null);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public PlayerType getPlayerType() {
        return type;
    }

}
