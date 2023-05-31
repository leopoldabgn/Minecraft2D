package model;

import java.awt.Graphics;
import java.util.ArrayList;

import model.Block.Grass;

public class Map {
    
    private Game game;
    private ArrayList<Player> players;
    private ArrayList<Block> blocks = new ArrayList<>();
    private int width  = 600,
                height = 600;

    private Map(Game game) {
        this.game = game;
        this.players = game.getPlayers();
    }

    public static Map create(Game game) {
        Map map = new Map(game);
        Block startBlock = map.pushBlock(BlockType.GRASS, 0, 0);
        startBlock.setPosition(0,
                               map.height - startBlock.getSize());

        Block iblock = startBlock;
        for(int j=0;j<11;j++) {
            iblock = map.pushRight(iblock, BlockType.GRASS);
        }

        return map;
    }

    public void draw(Graphics g) {

        // On commence par dessiner le terrain en arrière plan
        for(Block b : blocks) {
            b.draw(g);
        }

        // On dessine les joueurs
        if(players != null) {
            for(Player p : players) {
                p.draw(g);
            }
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

    public boolean isOnGround(Player p) {
        Player p2 = p.weakClone();
        p2.addToPosition(0, 1); // On descend de 1px
        // Si le pixel d'en dessous correspond à un block
        // Alors on est bien sur le sol
        if(isOnBlock(p2))
            return true;
        return false;
    }

    public boolean isOnBlock(Player player) {
        for(Block b : blocks) {
            if(b.isOnMe(player))
                return true;
        }
        return false;
    }

    public boolean isOnBlock(int x, int y) {
        for(Block b : blocks) {
            if(b.isOnMe(x, y))
                return true;
        }
        return false;
    }

    public boolean isOut(Player p) {
        int x1 = p.getX(),
        x2 = x1 + p.getSize(),
        y1 = p.getY(),
        y2 = p.getY() + p.getSize();

        return ((x1 <= 0 && x2 >= 0) || (x1 <= width && x2 >= width)) ||
        ((y1 <= 0 && y2 >= 0) || (y1 <= height && y2 >= height));
    }

    public boolean isOut(int x, int y) {
        return !(x >= 0 && x <= width && y >= 0 && y <= height);
    }

    public boolean movePlayer(Player player, int addX, int addY) {
        int x = player.getX() + addX;
        int y = player.getY() + addY;
        // On crée un clone de joueur pour tester si on peut bouger le joueur
        Player p2 = player.weakClone();
        p2.addToPosition(addX, addY);
        if(isOut(p2)) {
            System.err.println("Player at x:"+x+" , y:"+y+" is out of map ! Cannot move player.");
            return false;
        }
        if(isOnBlock(p2)) {
            System.err.println("x:"+x+" , y:"+y+" is on a block ! Cannot move player.");
            return false;
        }
        // On change les coordonnées du vrai joueur
        player.addToPosition(addX, addY);
        return true;
    }

    public void startFalling(Player p) {
        if(p.isFalling())
            return;
        p.setFalling(true);
        new Thread(() -> {
            while(!isOnGround(p)) { // timeout ?
                p.addToPosition(0, 1);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();
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