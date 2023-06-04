package model;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Map {
    
    private Lock mutex = new ReentrantLock();

    private Game game;
    private ArrayList<Player> players;
    private ArrayList<Block> blocks = new ArrayList<>();
    private int width  = 600,
                height = 600;

    private Point origin = new Point(0, 0);

    private static int[] BOUND_MAP_X = new int[] {-1000, 1000}, // 2000 blocs de large
                         BOUND_MAP_Y = new int[] {-320, 100};

    private Map(Game game, int width, int height) {
        this.game = game;
        this.width = width;
        this.height = height;
        this.players = game.getPlayers();
    }

    public static Map empty(Game game, int width, int height) {
        return new Map(game, width, height);
    }

    public boolean isOnScreen(Entity e) {
        int x1 = e.getRealX(),
            x2 = x1 + e.getSize(),
            y1 = e.getRealY(),
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
                b.draw(g, origin);
        }

        // On dessine les joueurs
        if(players != null) {
            for(Player p : players) {
                if(isOnScreen(p))
                    p.draw(g, origin);
            }
        }
    }

    public void pushLayer(Map map, BlockType bType, int y) {
        for(int i=BOUND_MAP_X[0];i<=BOUND_MAP_X[1];i++) {
            map.pushBlock(bType, i, y);
        }
    }

    // On a les blocks et leur probabilité d'apparition
    // Chaque element de proba est >= 0 et <= 1
    // L'addition de toutes les probas doivent faire 1
    public void pushLayer(Map map, BlockType[] blocks, double[] proba, int y) {

        for(int i=BOUND_MAP_X[0];i<=BOUND_MAP_X[1];i++) {
            map.pushBlock(getRandomBlockType(blocks, proba), i, y);
        }

    }

    public BlockType getRandomBlockType(BlockType[] blocks, double[] proba) {
        double randomNumber = Math.random();
        double cumulativeProbability = 0.0;
    
        for (int i = 0; i < proba.length; i++) {
            cumulativeProbability += proba[i];
    
            if (randomNumber <= cumulativeProbability) {
                return blocks[i];
            }
        }
    
        // Au cas où la somme des probabilités n'atteint pas 1,
        // renvoyer le dernier bloc du tableau
        return blocks[blocks.length - 1];
    }

    // On peut utiliser cette fonction pour retirer un block de la map
    // Le block peut ensuite etre stocké dans l'inventaire du joueur
    // par exemple
    public Block removeBlock(int x, int y) {
        for(Block b : blocks) {
            if(b.getX() == x && b.getY() == y) {
                blocks.remove(b);
                return b;
            }
        }
        return null;
    }

    public Block removeBlock(Block block) {
        if(blocks.remove(block))
            return block;
        return removeBlock(block.getX(), block.getY());
    }

    public Block pushBlockRealPos(BlockType type, int realx, int realy) {
        Block block = Block.create(type);
        block.setRealPosition(realx, realy);
        blocks.add(block);
        return block;
    }

    public Block pushBlock(BlockType type, int x, int y) {
        Block block = Block.create(type);
        block.setPosition(x, y);
        blocks.add(block);
        return block;
    }

    public Block pushFromBlock(BlockType type, Block startBlock, int addx, int addy) {
        int x = startBlock.getRealX() + addx;
        int y = startBlock.getRealY() + addy;
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
        p2.addToRealPosition(0, 1); // On descend de 1px
        // Si le pixel d'en dessous correspond à un block
        // Alors on est bien sur le sol
        if(isOnBlock(p2) != null)
            return true;
        return false;
    }

    public Block getBlockBelow(Player p) {
        Player p2 = p.weakClone();
        p2.addToRealPosition(0, 1); // On descend de 1px
        // Si le pixel d'en dessous correspond à un block
        // Alors on est bien sur le sol
        Block b = isOnBlock(p2);
        if(b != null)
            return b;
        return null;
    }

    public Block isOnBlock(Player player) {
        for(Block b : blocks) {
            if(b.isOnMe(player))
                return b;
        }
        return null;
    }

    public Block isOnBlock(int realX, int realY) {
        for(Block b : blocks) {
            if(b.isOnMe(realX, realY))
                return b;
        }
        return null;
    }

    public boolean isOut(Player p) {
        int x1 = p.getRealX(),
        x2 = x1 + p.getSize() - 1,
        y1 = p.getRealY(),
        y2 = y1 + p.getSize() - 1;

        return isOut(x1, y1) && isOut(x1, y2) && isOut(x2, y1) && isOut(x2, y2);
    }

    public boolean isOut(int x, int y) {
        int orx = (int)origin.getX(),
            ory = (int)origin.getY();
        return !(x >= orx && x <= orx + width && y >= ory && y <= ory + height);
    }

    public boolean canMovePlayer(Player player, int addX, int addY) {
        int x = player.getX() + addX;
        int y = player.getY() + addY;
        // On crée un clone de joueur pour tester si on peut bouger le joueur
        Player p2 = player.weakClone();
        p2.addToRealPosition(addX, addY);
        if(isOut(p2)) {
            // System.err.println("Player at x:"+x+" , y:"+y+" is out of map ! Cannot move player.");
            return false;
        }
        if(isOnBlock(p2) != null) {
            // System.err.println("x:"+x+" , y:"+y+" is on a block ! Cannot move player.");
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
            p.getAction().setWalking(false);
        }).start();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setSize(Dimension dim) {
        this.width  = (int)dim.getWidth();
        this.height = (int)dim.getHeight();
    }

    public void moveOrigin(int addX, int addY) {
        this.origin.setLocation(addX, addY);
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
