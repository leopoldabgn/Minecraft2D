package model;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class Map {

    private Game game;
    private ArrayList<Player> players;
    private ArrayList<Mob> mobs;
    private CopyOnWriteArrayList<Block> blocks = new CopyOnWriteArrayList<>();
    private int width  = 600,
                height = 600;

    private Point origin = new Point(0, 0);

    private static int[] BOUND_MAP_X = new int[] {-100, 100}, // 2000 blocs de large
                         BOUND_MAP_Y = new int[] {-320, 100};

    private Map(Game game, int width, int height) {
        this.game = game;
        this.width = width;
        this.height = height;
        this.players = game.getPlayers();
        this.mobs = game.getMobs();
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
        
        // On dessine l'arrière plan
        Image sky = Textures.loadBackgroundTexture("sky");
        g.drawImage(sky, 0, 0, getWidth(), getHeight(), null, null);

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

        // On dessine les mobs
        if(mobs != null) {
            for(Mob m : mobs) {
                if(isOnScreen(m)) {
                    // System.out.println(m.getType());
                    m.draw(g, origin);
                }
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
        Block blockToRemove = null;
        for(Block b : blocks) {
            if(b.getX() == x && b.getY() == y) {
                blockToRemove = b;
                break;
            }
        }
        
        if(blockToRemove != null)
            blocks.remove(blockToRemove);

        return blockToRemove;
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

    public boolean isOnGround(Mob m) {
        Mob m2 = m.weakClone();
        m2.addToRealPosition(0, 1); // On descend de 1px
        // Si le pixel d'en dessous correspond à un block
        // Alors on est bien sur le sol
        if(isOnBlock(m2) != null)
            return true;
        return false;
    }

    public Block getBlockLeft(Mob m, int distance) {
        return getBlockRight(m, -distance);
    }

    public Block getBlockRight(Mob m, int distance) {
        return getBlock(distance, 0, m);
    }

    public Block getBlockAbove(Mob m, int distance) {
        return getBlockBelow(m, -distance);
    }

    public Block getBlockBelow(Mob m, int distance) {
        return getBlock(0, distance, m);
    }

    public Block getBlock(int addX, int addY, Mob m) {
        Mob m2 = m.weakClone();
        m2.addToPosition(addX, addY);

        Block b = isOnBlock(m2);
        if(b != null)
            return b;
        return null;
    }

    public Block isOnBlock(Mob m) {
        for(Block b : blocks) {
            if(b.isOnMe(m))
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

    public boolean isOut(Mob m) {
        int x1 = m.getRealX(),
        x2 = x1 + m.getSize() - 1,
        y1 = m.getRealY(),
        y2 = y1 + m.getSize() - 1;

        return isOut(x1, y1) && isOut(x1, y2) && isOut(x2, y1) && isOut(x2, y2);
    }

    public boolean isOut(int x, int y) {
        int orx = (int)origin.getX(),
            ory = (int)origin.getY();
        return !(x >= orx && x <= orx + width && y >= ory && y <= ory + height);
    }

    public boolean canMoveMob(Mob mob, int addX, int addY) {
        // On crée un clone de joueur pour tester si on peut bouger le joueur
        Mob m2 = mob.weakClone();
        m2.addToRealPosition(addX, addY);
        if(isOut(m2)) {
            // System.err.println("Mob at x:"+x+" , y:"+y+" is out of map ! Cannot move Mob.");
            return false;
        }
        if(isOnBlock(m2) != null) {
            // System.err.println("x:"+x+" , y:"+y+" is on a block ! Cannot move Mob.");
            return false;
        }
        return true;
    }

    public void startFalling(Mob m) {
        if(m.isFalling())
            return;
        m.setFalling(true);
        new Thread(() -> {
            m.setVelY(1);
            while(!isOnGround(m))
                ;
            m.setVelY(0);
            m.setFalling(false);
        }).start();
    }

    public void startJumping(Mob m) {
        if(m.isJumping())
            return;
        m.setJumping(true);
        new Thread(() -> {
            m.setVelY(-1);
            for(int i=0;i<Entity.DEFAULT_BLOCK_SIZE * 2f;i++) {
                try {
                    Thread.sleep(Mob.DELAY_MOVE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            m.setVelY(0);
            m.setJumping(false);
            m.getAction().setPreviousAction();
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

    public void generateTree(int x, int y, int trunkSize) {
        int trunkHeight = Math.max(trunkSize, 1); // Calcul de la hauteur du tronc avec un minimum de 1
    
        // Génération du tronc de l'arbre
        for (int i = 0; i < trunkHeight; i++) {
            pushBlock(BlockType.OAK_LOG, x, y - i);
        }
    
        // Génération des feuilles de l'arbre
        int leafSize = (int) Math.ceil((double) trunkSize / 2.0); // Calcul de la taille des feuilles arrondie supérieurement
        int leafStartY = y - trunkHeight; // Position de départ des feuilles (même niveau que le tronc)
    
        for (int dy = -leafSize; dy <= 0; dy++) {
            int leafWidth = leafSize + dy;
    
            for (int dx = -leafWidth; dx <= leafWidth; dx++) {
                for (int dz = -leafWidth; dz <= leafWidth; dz++) {
                    int blockX = x + dx;
                    int blockY = leafStartY + dy;
    
                    pushBlock(BlockType.OAK_LEAVES, blockX, blockY);
                }
            }
        }
    }
    
    public void generateTrees(int leftBound, int rightBound, int y, double probability) {
        Random random = new Random();

        int minX = leftBound;
        int maxX = rightBound;
        int minY = y;
        int maxY = y;

        int minSpacing = 15;
        int maxSpacing = 50;

        int minTrunkSize = 3;
        int maxTrunkSize = 7;

        for (int x = minX; x <= maxX; x += minSpacing + random.nextInt(maxSpacing - minSpacing + 1)) {
            if (random.nextDouble() <= probability) {
                int trunkSize = minTrunkSize + random.nextInt(maxTrunkSize - minTrunkSize + 1);
                generateTree(x, random.nextInt(maxY - minY + 1) + minY, trunkSize);
            }
        }
    }



    public void generateTrees(int y, double probability) {
        generateTrees(BOUND_MAP_X[0], BOUND_MAP_X[1], y, probability);
    }

}
