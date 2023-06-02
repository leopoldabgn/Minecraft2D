package model;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

public class Map {
    
    private Game game;
    private ArrayList<Player> players;
    private ArrayList<Block> blocks = new ArrayList<>();
    private int width  = 600,
                height = 600;

    private Point origin = new Point(0, 0);

    private Map(Game game, int width, int height) {
        this.game = game;
        this.width = width;
        this.height = height;
        this.players = game.getPlayers();
    }

    // public static Map create(Game game, int width, int height) {
    //     Map map = new Map(game, width, height);
    //     Block startBlock = map.pushBlock(BlockType.GRASS, 0, 0);
    //     startBlock.setPosition(0,
    //                            map.height - startBlock.getSize());

    //     Block iblock = startBlock;
    //     Block jBlock = startBlock;
    //     for(int j=0;j<10;j++) {
    //         for(int i=0;i<100;i++) {
    //             iblock = map.pushRight(iblock, j == 0 ? BlockType.GRASS : BlockType.STONE);
    //         }
    //         jBlock = map.pushDown(jBlock, BlockType.STONE);
    //         iblock = jBlock;
    //     }
        
    //     return map;
    // }
        private static final int MAP_WIDTH = 100;
        private static final int MAP_HEIGHT = 10;
        private static final int NUM_LAYERS = 10;
        private static final double GRASS_PROBABILITY = 0.5;
    
        public static Map create(Game game, int width, int height) {
            Map map = new Map(game, width, height);
            Block startBlock = map.pushBlock(BlockType.GRASS, 0, 0);
            startBlock.setPosition(0, map.height - startBlock.getSize());
    
            Random random = new Random();

            Block currentBlock = startBlock;
            Block currentBlock2 = startBlock;

            long maxMemory = Runtime.getRuntime().maxMemory();

            // Conversion en méga-octets (Mo)
            long maxMemoryInMegabytes = maxMemory / (1024 * 1024);
    
            System.out.println("Mémoire maximale autorisée : " + maxMemoryInMegabytes + " Mo");
       

            for (int layer = 0; layer < 20; layer++) {
                for (int j = 0; j < MAP_HEIGHT; j++) {
                    for (int i = 0; i < MAP_WIDTH; i++) {
                        BlockType blockType = random.nextDouble() < GRASS_PROBABILITY ? BlockType.GRASS : BlockType.STONE;
                        currentBlock = map.pushRight(currentBlock, blockType);
                    }
                    currentBlock2 = map.pushDown(currentBlock2, BlockType.STONE);
                    currentBlock = currentBlock2;
                }
            }
        
            return map;
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
        int x1 = p.getRealX(),
        x2 = x1 + p.getSize(),
        y1 = p.getRealY(),
        y2 = y1 + p.getSize();

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
