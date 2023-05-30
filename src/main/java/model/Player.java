package model;

import java.awt.Point;

public class Player {
    
    private String pseudo, texturePath;
    private int score;
    private Point position;

    private Player(String pseudo, String texturePath) {
        this.pseudo = pseudo;
        this.texturePath = texturePath;
    }

    public static Player createPlayer(String pseudo) {
        return new Steve(pseudo);
    }

    public static class Steve extends Player{

        public Steve(String pseudo) {
            super(pseudo, "resources/player/steve.png");
        }

    }

    public void setPosition(int x, int y) {
        this.setPosition(new Point(x, y));
    }

    public void setPosition(Point position) {
        this.position = new Point(position);
    }

    public Point getPosition() {
        return position;
    }

    public int getX() {
        return (int)position.getX();
    }

    public int getY() {
        return (int)position.getY();
    }

}
