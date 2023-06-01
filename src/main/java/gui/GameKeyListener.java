package gui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import model.Game;
import model.Map;
import model.Player;

public class GameKeyListener extends KeyAdapter {
    
    private GameView gameView;
    private Game game;

    private boolean leftPressed, rightPressed;

    public GameKeyListener(GameView gameView, Game game) {
        this.gameView = gameView;
        this.game = game;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(gameView == null || game == null)
            return;
        MapView mapView = gameView.getMapView();
        Map map = game.getMap();
        Player p = game.getMainPlayer();

        switch(e.getKeyCode()) {
            case KeyEvent.VK_SPACE:
            case KeyEvent.VK_UP:
                if(map.isOnGround(p) && !p.isJumping()) {
                    map.startJumping(p);
                }
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                rightPressed = true;
                p.setVelX(1);
                break;
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                leftPressed = true;
                p.setVelX(-1);
                break;
        }
    }
        

    @Override
    public void keyReleased(KeyEvent e) {
        if(gameView == null || game == null)
            return;
        MapView mapView = gameView.getMapView();
        Map map = game.getMap();
        Player p = game.getMainPlayer();

        switch(e.getKeyCode()) {
            case KeyEvent.VK_SPACE:
            case KeyEvent.VK_UP:
                // TODO ?
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                rightPressed = false;
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                leftPressed = false;
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

}
