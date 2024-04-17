package model;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;

public class Mob extends Entity {
    
    private static final long serialVersionUID = 666666L;

    private MobType type = MobType.PIG;
    private MobAction action;
    private Point velocity = new Point(0, 0); // rapidite
 
    private boolean isFalling, isJumping;

    private int moveStartY;
    private long lastTimeMove, lastTimeAction, moveStartTime;

    public static long DELAY_MOVE = 0; // 3ms - TODO: A supprimer ?
    public static long DELAY_ACTION = 50; // 50ms

    // FALL speed est aussi basé sur JUMP_DELAY et JUMP_HEIGHT
    public static long JUMP_DELAY = 200; // 300ms
    public static long JUMP_HEIGHT = (int)(Entity.DEFAULT_BLOCK_SIZE * 1.7f); // Hauteur d'un saut
    public static float JUMP_FRICTION = 0.9f;

    private Mob() {}

    protected Mob(MobType type, Point position) {
        super(type.getType(), 1, position);
        this.type = type;
        this.action = new MobAction(new Action("walk_left", 4),
                                       new Action("walk_right", 4),
                                       new Action("jump_left", 1),
                                       new Action("jump_right", 1),
                                       new Action("sneak", 1)
                                       );
    }

    public static Mob createMob(MobType type) {
        return new Mob(type, new Point(0, -1));
    }

    public static Mob createMob(MobType type, Point position) {
        return new Mob(type, position);
    }

    public class MobAction implements Serializable {

        private static final long serialVersionUID = 888888L;

        private Action WALK_LEFT, WALK_RIGHT, JUMP_LEFT, JUMP_RIGHT, SNEAK;
        private Action currentAction, prevAction;

        public MobAction(Action WALK_LEFT, Action WALK_RIGHT, Action JUMP_LEFT, Action JUMP_RIGHT, Action SNEAK) {
            this.WALK_LEFT = WALK_LEFT;
            this.WALK_RIGHT = WALK_RIGHT;
            this.JUMP_LEFT = JUMP_LEFT;
            this.JUMP_RIGHT = JUMP_RIGHT;
            this.currentAction = WALK_RIGHT;
            this.SNEAK = SNEAK;
            setWalking(false); // On met walking right par defaut. Et on met a jour prevAction
        }

        public boolean isWalkingRight() {
            return currentAction == WALK_RIGHT;
        }

        public boolean isWalkingLeft() {
            return currentAction == WALK_LEFT;
        }

        public boolean isJumpingLeft() {
            return currentAction == JUMP_LEFT;
        }

        public boolean isJumpingRight() {
            return currentAction == JUMP_RIGHT;
        }

        public boolean isSneaking() {
            return currentAction == SNEAK;
        }

        public void setPreviousAction() {
            this.currentAction.resetProgress();
            this.currentAction = prevAction;
            Mob.this.texture = getCurrentTexture();
        }

        public void setSneak() {
            if(!isSneaking())
                prevAction = currentAction;
            currentAction = SNEAK;
            Mob.this.texture = getCurrentTexture();
        }

        public void setJumping(boolean left) {
            if(prevAction != currentAction)
                prevAction = currentAction;
            if(left)
                currentAction = JUMP_LEFT;
            else
                currentAction = JUMP_RIGHT;
            Mob.this.texture = getCurrentTexture();
        }

        public void setWalking(boolean left) {
            if(prevAction != currentAction)
                prevAction = currentAction;
            if(left)
                currentAction = WALK_LEFT;
            else
                currentAction = WALK_RIGHT;
            Mob.this.texture = getCurrentTexture();
        }

        public void resetTextureProgress() {
            currentAction.resetProgress();
            Mob.this.texture = getCurrentTexture();
        }

        public void nextTexture() {
            currentAction.nextTexture();
            Mob.this.texture = getCurrentTexture();
        }

        public String getCurrentTexture() {
            return currentAction.getTexture();
        }

    }

    public static class Action implements Serializable {

        private static final long serialVersionUID = 999999L;

        private String nom;
        private int progress = 1, maxTextures = 1;

        public Action(String nom, int maxTextures) {
            this.nom = nom;
            this.maxTextures = maxTextures;
        }
    
        public String getTexture() {
            if(maxTextures == 1)
                return nom;
            else
                return nom+"_"+progress;
        }

        public void resetProgress() {
            progress = 1;
        }

        // On prend l'image suivante
        public void nextTexture() {
            if(progress == maxTextures)
                resetProgress();
            else 
                progress++;
        }

        public String getNom() {
            return nom;
        }
    
    }

    // TODO: A supprimer ? Ou pas ? A voir...
    public static class Inventory {
        private ArrayList<Item> items = new ArrayList<>();
        private int maxSlots = 9; // Pour l'instant juste le nombre de slots de la barre d'items
        private ItemsBar itemsBar = new ItemsBar();

        public Inventory() {}

        public Item getItemWithMinNb(Item item) {
            Item minItem = null;
            for(Item it : items) {
                if(item.equals(it)) {
                    if(minItem == null || it.getNb() < minItem.getNb())
                        minItem = it;
                }
            }
            return minItem;
        }

        // On ajoute l'item a l'inventaire
        // Cette fonction se debrouille aussi pour le mettre dans la barre
        // d'items si necessaire !
        public boolean addItem(Item item) {
            Item minItem = getItemWithMinNb(item);
            if(minItem == null) {
                if(items.size() == maxSlots)
                    return false;
                items.add(item);
                itemsBar.addItem(item);
                return true;
            }
            int remainingBFull = minItem.remainingBeforeFull();
            int toAdd = Math.min(item.getNb(), remainingBFull);
            minItem.add(toAdd); // remainingBFull can be 0
            item.add(-toAdd); // On enleve ce qu'on vient d'ajouter

            // Si il reste un nombre > 0 d'items a ajouter, on ajoute un nouvel
            // objet Item a la liste
            if(item.getNb() > 0) {
                minItem.add(item.getNb());
            }
            return true;
        }

        public ItemsBar getItemsBar() {
            return itemsBar;
        }

    }

    // origin represente la coordonnée en haut a gauche de l'écran
    // On calcule les coordonnées de l'objet sur l'écran en partant de ce point
    @Override
    public void draw(Graphics g, Point origin) {
        int x = (int)(getRealX() - origin.getX());
        int y = (int)(getRealY() - origin.getY());

        // Get Image object for texture
        Image img = Textures.loadMobTexture(type, texture);
        g.drawImage(img, x, y, getSize(), getSize(), null);
    }

    public boolean update(Map map) {
        int x = getRealX() + getVelX(),
            y = getRealY();

        if(isFalling()) {
            long elapsedTime = System.currentTimeMillis() - moveStartTime;
            int deltaY = getDistanceProgress(elapsedTime, JUMP_HEIGHT, JUMP_DELAY);
            y = moveStartY + deltaY;
        }
        else if(isJumping()) {
            long elapsedTime = System.currentTimeMillis() - moveStartTime;
            int deltaY = getDistanceProgress(elapsedTime, JUMP_HEIGHT, JUMP_DELAY);

            // System.out.println(y+" "+deltaY+" "+jumpStartY+" "+(jumpStartY-deltaY)+" "+elapsedTime);

            y = moveStartY - deltaY;
            
            // Appliquer la friction à la vélocité de saut
            // setVelY((int)(getVelY() * JUMP_FRICTION));

            // Ajuster la vélocité de Y pour simuler le saut
            // setVelY(getVelY() - deltaY);

            // Vérifier si le mob doit cesser de sauter
            if(elapsedTime >= JUMP_DELAY) {
                setJumping(false); // Le saut est terminé après JUMP_DELAY ms
            }

            // System.out.println(elapsedTime+" "+deltaY+" "+getVelY());
        }

        boolean movedMobX = false, movedMobY = false;

        if(map.canMoveMob(this, x, y)) {
            movedMobX = move(x, y);
            movedMobY = movedMobX;
        }
        else if(x != getRealX() && y != getRealY()) {
            if (map.canMoveMob(this, getRealX(), y))
                movedMobY = move(getRealX(), y); // On interdit d'avancer sur x
            else if (map.canMoveMob(this, x, getRealY()))
                movedMobX = move(x, getRealY());
        }

        // Un bloc semble bloquer le mouvement du joueur
        if(isFalling()) {
            if(!movedMobY && y != getRealY())
                setFalling(false);
        }
        else if(isJumping()) {
            if(!movedMobY && y != getRealY()) {
                setJumping(false);
            }
        }

        if(!isJumping() && !isFalling() && !map.isOnGround(this)) {
            map.startFalling(this);
        }

        return movedMobX || movedMobY;
    }

    // Calculer la distance de saut en fonction du temps écoulé
    private int getDistanceProgress(long elapsedTime, long distance, long delay) {
        double normalizedTime = (double) elapsedTime / delay; // Coefficient temps écoulé / temps total
        double distProgress = normalizedTime * distance;
        return (int)distProgress;
    }

    // public boolean move() {
    //     return move(false, false);
    // }

    public boolean move(int realX, int realY) {
        if(realX == getRealX() && realY == getRealY())
            return false;
        // TODO: Attention, ça peut poser des problèmes pour la friction d'un jump ?
        // Peut etre qu'il vaut mieux mettre ça dans la fonction update !!!
        if(System.currentTimeMillis() - lastTimeMove >= Mob.DELAY_MOVE) {
            setRealPosition(realX, realY);
            lastTimeMove = System.currentTimeMillis();
            return true;
        }
        return false;
    }

    // Old Method
    public boolean moveWithVelocity(boolean restrictX, boolean restrictY) {
        if(velocity.getX() == 0 && velocity.getY() == 0)
            return false;
        // TODO: Attention, ça peut poser des problèmes pour la friction d'un jump ?
        // Peut etre qu'il vaut mieux mettre ça dans la fonction update !!!
        if(System.currentTimeMillis() - lastTimeMove >= Mob.DELAY_MOVE) {
            addToRealPosition(restrictX ? 0 : getVelX(),
                          restrictY ? 0 : getVelY());

            lastTimeMove = System.currentTimeMillis();
            return true;
        }
        return false;
    }

    public boolean refreshActionTexture() {
        if(System.currentTimeMillis() - lastTimeAction >= Mob.DELAY_ACTION) {
            action.nextTexture();
            lastTimeAction = System.currentTimeMillis();
            return true;
        }
        return false;
    }

    public void setVelocity(Point vel) {
        this.velocity = new Point(vel);
    }

    public void setVelX(int velX) {
        this.velocity.setLocation(velX, getVelY());
    }

    public void setVelY(int velY) {
        this.velocity.setLocation(getVelX(), velY);
    }

    public int getVelX() {
        return (int)velocity.getX();
    }

    public int getVelY() {
        return (int)velocity.getY();
    }

    public Mob weakClone() {
        Mob p = new Mob();
        p.coeffSize = coeffSize;
        p.position = new Point(position);
        return p;
    }

    public void setFalling(boolean isFalling) {
        if(isFalling && isJumping())
            return;
        this.isFalling = isFalling;
        if(isFalling) {
            moveStartTime = System.currentTimeMillis();
            moveStartY = getRealY();
        }
    }

    public boolean isFalling() {
        return isFalling;
    }

    public void setJumping(boolean isJumping) {
        if(isJumping && isFalling())
            return;
        this.isJumping = isJumping;
        if(isJumping) {
            moveStartTime = System.currentTimeMillis();
            moveStartY = getRealY();
        }
        else
            action.setPreviousAction();
    }

    public boolean isJumping() {
        return isJumping;
    }

    public void setLastTimeMove(long t) {
        this.lastTimeMove = t;
    }

    public long getLastTimeMove() {
        return lastTimeMove;
    }

    public MobAction getAction() {
        return action;
    }

    public MobType getMobType() {
        return type;
    }

    public void resetMobVelocity() {
        setVelX(0);
        setVelY(0);
        setJumping(false);
        setFalling(false);
    }

}
