package gui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import model.Block;
import model.Game;
import model.Map;
import model.Player;
import model.Player.PlayerAction;

public class GameKeyListener extends KeyAdapter {
    
    private GameView gameView;
    private Game game;

    private boolean leftPressed, rightPressed;

    private long timeBetweenActions = 350,
                startActionTime;

    public GameKeyListener(GameView gameView, Game game) {
        this.gameView = gameView;
        this.game = game;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        pressKey(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        releaseKey(e.getKeyCode());
    }

    public void pressKey(int keyCode) {
        if(gameView == null || game == null)
        return;
        Map map = game.getMap();
        Player p = game.getMainPlayer();
        PlayerAction pAction = p.getAction();
        Block block = null;

        switch(keyCode) {
            case KeyEvent.VK_ESCAPE:
                // TODO: Open settings page
                Window.currentWindow.dispose();
                System.exit(0);
            case KeyEvent.VK_S: // Detruire un block
                if(System.currentTimeMillis() - startActionTime <= timeBetweenActions)
                    return;
                startActionTime = System.currentTimeMillis();

                if(pAction.isSneaking()) {
                    block = map.getBlockBelow(p, 1);
                }
                else {
                    int maxDist = 2;
                    for(int i=0;block == null && i < maxDist;i++) {
                        if(pAction.isWalkingRight()) {
                            block = map.getBlockRight(p, i);
                        }
                        else if(pAction.isWalkingLeft()) {
                            block = map.getBlockLeft(p, i);
                        }
                    }
                }
                if(block != null) // && have pickaxe ?
                        map.removeBlock(block); // TODO: add block to player inventory
                break;
            case KeyEvent.VK_W: // Poser un block
                if(System.currentTimeMillis() - startActionTime <= timeBetweenActions)
                    return;
                startActionTime = System.currentTimeMillis();

                // Verifier les blocks devant le joueur
                // Si c est vide, on l'autorise a poser un block
                // Cependant, on doit aussi verifier dans la barre d'items
                // Que le joueur tient bien un bloc dans sa main !
                // Il faut vérifier ça en premier d'ailleurs ! Avant meme
                // de voir si il y a un creux devant le joueur

                // int maxDist = 2;
                // for(int i=0;block == null && i < maxDist;i++) {
                //     if(pAction.isWalkingRight()) {
                //         block = map.getBlockRight(p, i);
                //     }
                //     else if(pAction.isWalkingLeft()) {
                //         block = map.getBlockLeft(p, i);
                //     }
                // }
                
                // if(block != null) // && have pickaxe ?
                //         map.addBlock(Block.GRASS, x, y....); // TODO: remove block from player inventory
                break;
            case KeyEvent.VK_SPACE:
            case KeyEvent.VK_UP:
                if(map.isOnGround(p) && !p.isJumping()) {
                    pAction.setJumping(pAction.isWalkingLeft());
                    map.startJumping(p);
                }
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                rightPressed = true;
                p.setVelX(1);
                if(!pAction.isWalkingRight())
                    pAction.setWalking(false);
                // else
                //     pAction.nextTexture();
                break;
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                leftPressed = true;
                p.setVelX(-1);
                if(!pAction.isWalkingLeft())
                    pAction.setWalking(true);
                // else
                //     pAction.nextTexture();
                break;
            case KeyEvent.VK_SHIFT:
            case KeyEvent.VK_DOWN:
                if(p.getVelX() == 0 && p.getVelY() == 0 && !pAction.isSneaking())
                    pAction.setSneak();
                break;
        }
    }

    public void releaseKey(int keyCode) {
        if(gameView == null || game == null)
            return;
        Player p = game.getMainPlayer();
        PlayerAction pAction = p.getAction();

        switch(keyCode) {
            case KeyEvent.VK_SPACE:
            case KeyEvent.VK_UP:
                // TODO ?
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                rightPressed = false;
                pAction.resetTextureProgress();
                break;
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                leftPressed = false;
                pAction.resetTextureProgress();
                break;
            case KeyEvent.VK_SHIFT:
            case KeyEvent.VK_DOWN:
                if(pAction.isSneaking())
                    pAction.setPreviousAction();
                break;
        }

        if(rightPressed && !leftPressed) {
            p.setVelX(1);
        }
        else if(!rightPressed && leftPressed) {
            p.setVelX(-1);
        }
        else if(!rightPressed && !leftPressed) {
            p.setVelX(0);
        }
    }
    
    public boolean isLeftPressed() {
        return leftPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }

}
