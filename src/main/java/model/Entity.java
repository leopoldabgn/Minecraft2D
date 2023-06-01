package model;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.io.IOException;

import launcher.App;

public abstract class Entity {
    
    protected Point position = new Point(0, 0);
    private String texturePath;
    private transient Image texture;

    public static int DEFAULT_BLOCK_SIZE = 50;
    protected double coeffSize = 1;

    protected Entity() {}

    public Entity(String texturePath, int coeffSize, Point position) {
        setTexture(texturePath);
        this.coeffSize = coeffSize;
    }

    public void draw(Graphics g) {
        if(texture == null)
            setTexture(texturePath); // Refresh Image Object
        g.drawImage(texture, (int)position.getX(), (int)position.getY(), getSize(), getSize(), null);
    }

    public boolean isOnMe(Entity e) {
        int x1 = e.getX(),
            x2 = x1 + e.getSize(),
            y1 = e.getY(),
            y2 = e.getY() + e.getSize();

        return isOnMe(x1, y1) || isOnMe(x2, y1) || isOnMe(x1, y2) || isOnMe(x2, y2);
    }

    public boolean isOnMe(int x2, int y2) {
        return x2 >= getX() && x2 <= getX() + getSize() &&
               y2 >= getY() && y2 <= getY() + getSize(); 
    }

    public int getSize() {
        return (int)(coeffSize * DEFAULT_BLOCK_SIZE);
    }

    public int getX() {
        return (int)position.getX();
    }

    public int getRealX() {
        return getX() * DEFAULT_BLOCK_SIZE;
    }

    public int getY() {
        return (int)position.getY();
    }

    public int getRealY() {
        return getY() * DEFAULT_BLOCK_SIZE;
    }

    public Point getPosition() {
        return position;
    }

    public void addToPosition(int addX, int addY) {
        this.setPosition(getX() + addX, getY() + addY);
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

    public void setCoeffSize(double coeffSize) {
        this.coeffSize = coeffSize;
    }

    @Override
    public String toString() {
        return "entity -> position: x: "+getX()+", y: "+getY();
    }

}
