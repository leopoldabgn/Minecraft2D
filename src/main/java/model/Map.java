package model;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

public class Map {
    
    private Game game;
    private ArrayList<Player> players;
    private ArrayList<Block> blocks = new ArrayList<>();
    private int width  = 600,
                height = 600;

    private Point origin = new Point(0, 0);

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

    public boolean isOnScreen(Entity e) {
        int x1 = e.getX(),
            x2 = x1 + e.getSize(),
            y1 = e.getY(),
            y2 = y1 + e.getSize();
    
        return isOnScreen(x1, y1) || isOnScreen(x2, y1) || isOnScreen(x1, y2) || isOnScreen(x2, y2);
    }
    
    public boolean isOnScreen(int x, int y) {
        int orx = (int)origin.getX(),
            ory = (int)origin.getY();

        return x >= orx && x <= orx + width && y >= ory && y <= ory + height;
    }

    public void draw(Graphics g) {
        // On commence par dessiner le terrain en arrière plan
        for(Block b : blocks) {
            if(isOnScreen(b))
                b.draw(g);
        }

        // On dessine les joueurs
        if(players != null) {
            for(Player p : players) {
                if(isOnScreen(p))
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
        y2 = y1 + p.getSize();

        return isOut(x1, y1) && isOut(x1, y2) && isOut(x2, y1) && isOut(x2, y2);
    }

    public boolean isOut(int x, int y) {
        return !(x >= 0 && x <= width && y >= 0 && y <= height);
    }

    public boolean canMovePlayer(Player player, int addX, int addY) {
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
        return true;
    }

    public void startFalling(Player p) {
        if(p.isFalling())
            return;
        p.setFalling(true);
        new Thread(() -> {
            p.setVelY(1);
            while(!isOnGround(p))
                ;
            p.setVelY(0);
            p.setFalling(false);
        }).start();
    }

    public void startJumping(Player p) {
        if(p.isJumping())
            return;
        p.setJumping(true);
        new Thread(() -> {
            p.setVelY(-1);
            for(int i=0;i<Entity.DEFAULT_BLOCK_SIZE * 1.5f;i++) {
                try {
                    Thread.sleep(Player.DELAY_MOVE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            p.setVelY(0);
            p.setJumping(false);
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
