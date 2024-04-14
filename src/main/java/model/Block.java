package model;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

public class Block extends Entity {

    private static final long serialVersionUID = 333333L;

    private BlockType type;

    private Block(BlockType type, int x, int y) {
        super(type.getTexture(), type.getMaxStack(), 1, new Point(x, y));
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

        // drawFissures(g, x, y, getSize());
    }


    // private void drawFissures(Graphics g, int x, int y, int size) {
    //     g.setColor(new Color(0, 0, 0)); // Couleur noire avec transparence
    //     int sqrSize = size / 16;

    //     for(int i=0;i<8;i++) {
    //         g.fillRect(x + i * sqrSize, y + i * sqrSize , sqrSize, sqrSize);
    //     }
        

    // }

    public BlockType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "block -> type: "+getClass().getName()+", x: "+getRealX()+", y: "+getRealY();
    }

}
