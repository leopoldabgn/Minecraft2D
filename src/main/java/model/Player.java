package model;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.io.IOException;

import launcher.App;

public class Player {
    
    private String pseudo, texturePath;
    private transient Image texture;
    private int score;
    private Point position = new Point(0, 0);
    private int size = 50;
    private boolean isFalling;

    private Player() {}

    private Player(String pseudo, String texturePath) {
        this.pseudo = pseudo;
        setTexture(texturePath);
    }

    public static Player createPlayer(String pseudo) {
        return new Steve(pseudo);
    }

    public void draw(Graphics g) {
        if(texture == null)
            setTexture(texturePath); // Refresh Image Object
        g.drawImage(texture, (int)position.getX(), (int)position.getY(), size, size, null);
    }

    public static class Steve extends Player{

        public Steve(String pseudo) {
            super(pseudo, "players/steve_right.png");
        }

    }

    public void addToPosition(int addX, int addY) {
        this.setPosition(getX() + addX, getY() + addY);
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

    public void setTexture(String path) {
        this.texturePath = path;
        try {
            System.out.println(path);
            this.texture = App.getImage(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getSize() {
        return size;
    }

    public Player weakClone() {
        Player p = new Player();
        p.size = size;
        p.position = new Point(position);
        return p;
    }

    public void setFalling(boolean isFalling) {
        this.isFalling = isFalling;
    }

    public boolean isFalling() {
        return isFalling;
    }

}
