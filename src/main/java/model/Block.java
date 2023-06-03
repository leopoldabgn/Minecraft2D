package model;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

public class Block extends Entity {

    private BlockType type;

    private Block(BlockType type, int x, int y) {
        super(type.getTexture(), 1, new Point(x, y));
        this.type = type;
    }

    public static Block create(BlockType type) {
        return new Block(type, 0, 0);
    }

    @Override
    public void draw(Graphics g, Point origin) {
        int x = (int)(getRealX() - origin.getX());
        int y = (int)(getRealY() - origin.getY());

        // Get Image object for texture
        Image img = Textures.loadBlockTexture(texture);
        g.drawImage(img, x, y, getSize(), getSize(), null);
    }

    public BlockType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "block -> type: "+getClass().getName()+", x: "+getRealX()+", y: "+getRealY();
    }

}
