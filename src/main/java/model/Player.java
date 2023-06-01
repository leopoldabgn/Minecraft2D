package model;

import java.awt.Point;

public class Player extends Entity {
    
    private String pseudo;
    private int score;
    private Point velocity = new Point(0, 0); // rapidite
 
    private boolean isFalling, isJumping;

    private long lastTimeMove;
    public static long DELAY_MOVE = 3; // 5ms

    private Player() {}

    private Player(String pseudo, String texturePath) {
        super(texturePath, 1, new Point(0, 0));
        this.pseudo = pseudo;
    }

    public static Player createPlayer(String pseudo) {
        return new Steve(pseudo);
    }

    public static class Steve extends Player{

        public Steve(String pseudo) {
            super(pseudo, "players/steve_right.png");
        }

    }

    public void move() {
        move(false, false);
    }

    public void move(boolean restrictX, boolean restrictY) {
        if(System.currentTimeMillis() - lastTimeMove >= Player.DELAY_MOVE) {
            addToPosition(restrictX ? 0 : getVelX(),
                          restrictY ? 0 : getVelY());
            lastTimeMove = System.currentTimeMillis();
        }
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
