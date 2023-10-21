package model;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;

public class Player extends Entity {
    
    private PlayerType type = PlayerType.STEVE;
    private String pseudo;
    private int score;
    private PlayerAction action;
    private Point velocity = new Point(0, 0); // rapidite
    private Inventory inventory = new Inventory();
 
    private boolean isFalling, isJumping;

    private long lastTimeMove, lastTimeAction;
    public static long DELAY_MOVE = 3; // 3ms
    public static long DELAY_ACTION = 50;

    private Player() {}

    private Player(PlayerType type, String pseudo, Point position) {
        super(type.getType(), 1, position);
        this.type = type;
        this.pseudo = pseudo;
        this.action = new PlayerAction(new Action("walk_left", 4),
                                       new Action("walk_right", 4),
                                       new Action("jump_left", 1),
                                       new Action("jump_right", 1),
                                       new Action("sneak", 1)
                                       );
    }

    public static Player createPlayer(PlayerType type, String pseudo) {
        return new Player(type, pseudo, new Point(0, -1));
    }

    public static Player createPlayer(PlayerType type, String pseudo, Point position) {
        return new Player(type, pseudo, position);
    }

    public class PlayerAction {

        private Action WALK_LEFT, WALK_RIGHT, JUMP_LEFT, JUMP_RIGHT, SNEAK;
        private Action currentAction, prevAction;

        public PlayerAction(Action WALK_LEFT, Action WALK_RIGHT, Action JUMP_LEFT, Action JUMP_RIGHT, Action SNEAK) {
            this.WALK_LEFT = WALK_LEFT;
            this.WALK_RIGHT = WALK_RIGHT;
            this.JUMP_LEFT = JUMP_LEFT;
            this.JUMP_RIGHT = JUMP_RIGHT;
            this.currentAction = WALK_RIGHT;
            this.SNEAK = SNEAK;
            setWalking(false); // On met walking right par defaut. Et on met a jour prevAction
        }

        public boolean isWalkingRight() {
            return currentAction == WALK_RIGHT;
        }

        public boolean isWalkingLeft() {
            return currentAction == WALK_LEFT;
        }

        public boolean isJumpingLeft() {
            return currentAction == JUMP_LEFT;
        }

        public boolean isJumpingRight() {
            return currentAction == JUMP_RIGHT;
        }

        public boolean isSneaking() {
            return currentAction == SNEAK;
        }

        public void setPreviousAction() {
            this.currentAction.resetProgress();
            this.currentAction = prevAction;
            Player.this.texture = getCurrentTexture();
        }

        public void setSneak() {
            if(!isSneaking())
                prevAction = currentAction;
            currentAction = SNEAK;
            Player.this.texture = getCurrentTexture();
        }

        public void setJumping(boolean left) {
            if(prevAction != currentAction)
                prevAction = currentAction;
            if(left)
                currentAction = JUMP_LEFT;
            else
                currentAction = JUMP_RIGHT;
            Player.this.texture = getCurrentTexture();
        }

        public void setWalking(boolean left) {
            if(prevAction != currentAction)
                prevAction = currentAction;
            if(left)
                currentAction = WALK_LEFT;
            else
                currentAction = WALK_RIGHT;
            Player.this.texture = getCurrentTexture();
        }

        public void resetTextureProgress() {
            currentAction.resetProgress();
            Player.this.texture = getCurrentTexture();
        }

        public void nextTexture() {
            currentAction.nextTexture();
            Player.this.texture = getCurrentTexture();
        }

        public String getCurrentTexture() {
            return currentAction.getTexture();
        }

    }

    public static class Action {
        private String nom;
        private int progress = 1, maxTextures = 1;

        public Action(String nom, int maxTextures) {
            this.nom = nom;
            this.maxTextures = maxTextures;
        }
    
        public String getTexture() {
            if(maxTextures == 1)
                return nom;
            else
                return nom+"_"+progress;
        }

        public void resetProgress() {
            progress = 1;
        }

        // On prend l'image suivante
        public void nextTexture() {
            if(progress == maxTextures)
                resetProgress();
            else 
                progress++;
        }

        public String getNom() {
            return nom;
        }
    
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
    @Override
    public void draw(Graphics g, Point origin) {
        int x = (int)(getRealX() - origin.getX());
        int y = (int)(getRealY() - origin.getY());

        // Get Image object for texture
        Image img = Textures.loadPlayerTexture(type, texture);
        g.drawImage(img, x, y, getSize(), getSize(), null);
    }

    public boolean move() {
        return move(false, false);
    }

    public boolean move(boolean restrictX, boolean restrictY) {
        if(velocity.getX() == 0 && velocity.getY() == 0)
            return false;
        if(System.currentTimeMillis() - lastTimeMove >= Player.DELAY_MOVE) {
            addToRealPosition(restrictX ? 0 : getVelX(),
                          restrictY ? 0 : getVelY());

            lastTimeMove = System.currentTimeMillis();
            return true;
        }
        return false;
    }

    public boolean refreshActionTexture() {
        if(System.currentTimeMillis() - lastTimeAction >= Player.DELAY_ACTION) {
            action.nextTexture();
            lastTimeAction = System.currentTimeMillis();
            return true;
        }
        return false;
    }

    public void setVelocity(Point vel) {
        this.velocity = new Point(vel);
    }

    public void setVelX(int velX) {
        this.velocity.setLocation(velX, getVelY());
    }

    public void setVelY(int velY) {
        this.velocity.setLocation(getVelX(), velY);
    }

    public int getVelX() {
        return (int)velocity.getX();
    }

    public int getVelY() {
        return (int)velocity.getY();
    }

    public Player weakClone() {
        Player p = new Player();
        p.coeffSize = coeffSize;
        p.position = new Point(position);
        return p;
    }

    public void setFalling(boolean isFalling) {
        this.isFalling = isFalling;
    }

    public boolean isFalling() {
        return isFalling;
    }

    public void setJumping(boolean isJumping) {
        this.isJumping = isJumping;
    }

    public boolean isJumping() {
        return isJumping;
    }

    public void setLastTimeMove(long t) {
        this.lastTimeMove = t;
    }

    public long getLastTimeMove() {
        return lastTimeMove;
    }

    public PlayerAction getAction() {
        return action;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public PlayerType getType() {
        return type;
    }

}
