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
        if(gameView == null || game == null)
            return;
        Map map = game.getMap();
        Player p = game.getMainPlayer();
        PlayerAction pAction = p.getAction();
        Block block = null;

        switch(e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                // TODO: Open settings page
                Window.currentWindow.dispose();
                System.exit(0);
            case KeyEvent.VK_E:
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
        

    @Override
    public void keyReleased(KeyEvent e) {
        if(gameView == null || game == null)
            return;
        Player p = game.getMainPlayer();
        PlayerAction pAction = p.getAction();

        switch(e.getKeyCode()) {
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
