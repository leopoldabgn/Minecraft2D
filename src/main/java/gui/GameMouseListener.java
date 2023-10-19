package gui;

import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import model.Game;
import model.ItemsBar;

public class GameMouseListener extends MouseAdapter {
    
    private GameView gameView;
    private Game game;
    private GameKeyListener gameKeyListener;

    public GameMouseListener(GameView gameView, Game game, GameKeyListener gameKeyListener) {
        this.gameView = gameView;
        this.game = game;
        this.gameKeyListener = gameKeyListener;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        
        if(e.getButton() == MouseEvent.BUTTON1) {
            gameKeyListener.pressKey(KeyEvent.VK_S);
        }
        else if(e.getButton() == MouseEvent.BUTTON2) {
            
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int rotation = e.getWheelRotation();
        ItemsBar itemsBar = game.getPlayerItemsBar();
        // On utilise la valeur de rotation pour détecter si le défilement
        // est vers l'avant ou vers l'arrière
        if (rotation < 0) {
            itemsBar.prevItem();
        }
        else {
            itemsBar.nextItem();
        }
    }

}
