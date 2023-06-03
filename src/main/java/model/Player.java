package model;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

public class Player extends Entity {
    
    private PlayerType type = PlayerType.STEVE;
    private String pseudo;
    private int score;
    private Point velocity = new Point(0, 0); // rapidite
 
    private boolean isFalling, isJumping;

    private long lastTimeMove;
    public static long DELAY_MOVE = 3; // 5ms

    private Player() {}

    private Player(PlayerType type, String pseudo, Point position) {
        super(type.getTexture(), 1, position);
        this.pseudo = pseudo;
    }

    public static Player createPlayer(PlayerType type, String pseudo) {
        return new Player(type, pseudo, new Point(0, 0));
    }

    public static Player createPlayer(PlayerType type, String pseudo, Point position) {
        return new Player(type, pseudo, position);
    }

    // origin represente la coordonnée en haut a gauche de l'écran
    // On calcule les coordonnées de l'objet sur l'écran en partant de ce point
    @Override
    public void draw(Graphics g, Point origin) {
        int x = (int)(getRealX() - origin.getX());
        int y = (int)(getRealY() - origin.getY());

        // Get Image object for texture
        Image img = Textures.loadPlayerTexture(texture);
        g.drawImage(img, x, y, getSize(), getSize(), null);
    }

    public boolean move() {
        return move(false, false);
    }

    public boolean move(boolean restrictX, boolean restrictY) {
        if(System.currentTimeMillis() - lastTimeMove >= Player.DELAY_MOVE) {
            addToRealPosition(restrictX ? 0 : getVelX(),
                          restrictY ? 0 : getVelY());
            lastTimeMove = System.currentTimeMillis();
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

}
