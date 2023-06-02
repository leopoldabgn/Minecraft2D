package model;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

public abstract class Entity {
    
    protected Point position = new Point(0, 0);
    protected String texture;
    // private transient Image texture;

    public static int DEFAULT_BLOCK_SIZE = 50;
    protected double coeffSize = 1;

    protected Entity() {}

    public Entity(String texture, int coeffSize, Point position) {
        this.texture = texture;
        this.coeffSize = coeffSize;
    }

    // origin represente la coordonnée en haut a gauche de l'écran
    // On calcule les coordonnées de l'objet sur l'écran en partant de ce point
    public void draw(Graphics g, Point origin) {
        int x = (int)(getRealX() - origin.getX());
        int y = (int)(getRealY() - origin.getY());

        // Get Image object for texture
        Image img = Textures.getDefaultTexture();
        g.drawImage(img, x, y, getSize(), getSize(), null);
    }

    public boolean isOnMe(Entity e) {
        int x1 = e.getRealX(),
            x2 = x1 + e.getSize(),
            y1 = e.getRealY(),
            y2 = y1 + e.getSize();

        return isOnMe(x1, y1) || isOnMe(x2, y1) || isOnMe(x1, y2) || isOnMe(x2, y2);
    }

    public boolean isOnMe(int x2, int y2) {
        return x2 >= getRealX() && x2 <= getRealX() + getSize() &&
               y2 >= getRealY() && y2 <= getRealY() + getSize(); 
    }

    public int getSize() {
        return (int)(coeffSize * DEFAULT_BLOCK_SIZE);
    }

    public int getX() {
        return (int)(position.getX() / DEFAULT_BLOCK_SIZE);
    }

    public int getRealX() {
        return (int)position.getX();
    }

    public int getY() {
        return (int)(position.getY() / DEFAULT_BLOCK_SIZE);
    }

    public int getRealY() {
        return (int)position.getY();
    }

    public Point getPosition() {
        return position;
    }

    public void addToPosition(int addX, int addY) {
        this.setPosition(getRealX() + addX, getRealY() + addY);
    }

    public void setPosition(int x, int y) {
        this.position = new Point(x, y);
    }

    public void setCoeffSize(double coeffSize) {
        this.coeffSize = coeffSize;
    }

    @Override
    public String toString() {
        return "entity -> position: x: "+getRealX()+", y: "+getRealY();
    }

}