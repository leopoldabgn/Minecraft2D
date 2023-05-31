package gui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import model.Game;
import model.Map;
import model.Player;

public class GameKeyListener extends KeyAdapter {
    
    private GameView gameView;
    private Game game;

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
        Player player = game.getMainPlayer();

        switch(e.getKeyCode()) {
            case KeyEvent.VK_SPACE:
            case KeyEvent.VK_UP:
                // TODO: jump thread
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                if(!map.movePlayer(player, 5, 0))
                    return;
                mapView.repaint();
                break;
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                if(!map.movePlayer(player, -5, 0))
                    return;
                mapView.repaint();
                break;
        }

    }
        
}
