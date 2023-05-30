package model;

import java.awt.Graphics;
import java.util.ArrayList;

public class Map {
    
    private Game game;
    private ArrayList<Block> blocks = new ArrayList<>();
    private int width  = 600,
                height = 600;

    private Map(Game game) {
        this.game = game;
    }

    public static Map create(Game game) {
        Map map = new Map(game);
        Block startBlock = map.pushBlock(BlockType.GRASS, 0, 0);
        startBlock.setPosition(0,
                               map.height - startBlock.getSize());
        Block jblock = startBlock;
        Block iblock = startBlock;
        for(int i=0;i<12;i++) {
            iblock = jblock;
            for(int j=0;j<11;j++) {
                iblock = map.pushRight(iblock, (i + j )% 2 == 0 ? BlockType.BRICK : BlockType.GRASS);
            }
            if(i != 11)
                jblock = map.pushUp(jblock, i % 2 == 0 ? BlockType.BRICK : BlockType.GRASS);
        }

        return map;
    }

    public void draw(Graphics g) {
        for(Block b : blocks) {
            b.draw(g);
        }
    }

    public Block pushBlock(BlockType type, int x, int y) {
        if(isOut(x, y)) {
            System.err.println("x:"+x+" , y:"+y+" is out of map !");
            return null;
        }
        Block block = Block.create(type);
        block.setPosition(x, y);
        blocks.add(block);
        return block;
    }

    public Block pushFromBlock(BlockType type, Block startBlock, int addx, int addy) {
        int x = startBlock.getX() + addx;
        int y = startBlock.getY() + addy;
        return pushBlock(type, x, y);
    }

    public Block pushLeft(Block startBlock, BlockType type) {
        return pushFromBlock(type, startBlock, -startBlock.getSize(), 0);
    }

    public Block pushUp(Block startBlock, BlockType type) {
        return pushFromBlock(type, startBlock, 0, -startBlock.getSize());
    }

    public Block pushRight(Block startBlock, BlockType type) {
        return pushFromBlock(type, startBlock, startBlock.getSize(), 0);
    }

    public Block pushDown(Block startBlock, BlockType type) {
        return pushFromBlock(type, startBlock, 0, startBlock.getSize());
    }

    public boolean isOut(int x, int y) {
        return !(x >= 0 && x <= width && y >= 0 && y <= height);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public String toString() {
        String s = "width: "+width+", height: "+height+"\nblocks:\n";
        for(Block b : blocks) {
            s += b+"\n";
        }
        return s;
    }

}
