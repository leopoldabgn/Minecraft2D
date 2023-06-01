package model;

import java.awt.Point;

public class Block extends Entity {

    private Block(int x, int y, String texturePath) {
        super(texturePath, 1, new Point(x, y));
    }

    public static Block create(BlockType type) {
        switch(type) {
            case BRICK:
                return new Brick(0, 0);
            default:
                return new Grass(0, 0);
        }
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

    @Override
    public String toString() {
        return "block -> type: "+getClass().getName()+", x: "+getX()+", y: "+getY();
    }

}
