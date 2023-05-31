package model;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.io.IOException;

import launcher.App;

public class Block {

    private Point position = new Point(-1, -1);
    private String texturePath;
    private transient Image texture;

    private int DEFAULT_SIZE = 50;
    private int size;

    private Block(int x, int y, String texturePath) {
        this.position = new Point(x, y);
        setTexture(texturePath);
        this.size = DEFAULT_SIZE;
    }

    public static Block create(BlockType type) {
        switch(type) {
            case BRICK:
                return new Brick(0, 0);
            default:
                return new Grass(0, 0);
        }
    }

    public void draw(Graphics g) {
        if(texture == null)
            setTexture(texturePath); // Refresh Image Object
        g.drawImage(texture, (int)position.getX(), (int)position.getY(), size, size, null);
    }

    public static class Grass extends Block {

        public Grass(int x, int y) {
            super(x, y, "blocks/grass.png");
        }

    }

    public static class Brick extends Block {

        public Brick(int x, int y) {
            super(x, y, "blocks/brick.png");
        }

    }

    public boolean isOnMe(Player p) {
        int x1 = p.getX(),
            x2 = x1 + p.getSize(),
            y1 = p.getY(),
            y2 = p.getY() + p.getSize();

        return ((x1 <= getX() && x2 >= getX()) || (x1 <= getX() + getSize() && x2 >= getX() + getSize())) &&
        ((y1 <= getY() && y2 >= getY()) || (y1 <= getY() + getSize() && y2 >= getY() + getSize()));
    }

    public boolean isOnMe(int x2, int y2) {
        return x2 >= getX() && x2 <= getX() + getSize() &&
               y2 >= getY() && y2 <= getY() + getSize(); 
    }

    public int getSize() {
        return size;
    }

    public int getX() {
        return (int)position.getX();
    }

    public int getY() {
        return (int)position.getY();
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(int x, int y) {
        this.position = new Point(x, y);
    }

    public void setTexture(String path) {
        this.texturePath = path;
        try {
            this.texture = App.getImage(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "block -> type: "+getClass().getName()+", x: "+getX()+", y: "+getY();
    }

    public void setSize(int size) {
        this.size = size;
    }

}
