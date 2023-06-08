package gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import model.Game;
import model.ItemsBar;
import model.Map;

public class GameMouseListener extends MouseAdapter {
    
    private GameView gameView;
    private Game game;

    public GameMouseListener(GameView gameView, Game game) {
        this.gameView = gameView;
        this.game = game;
    }

    static int y = 0;

    @Override
    public void mousePressed(MouseEvent e) {
        Map map = game.getMap();
        // map.pushBlock(BlockType.BRICK, e.getX() - 50, e.getY() - 50);
        map.moveOrigin(0, y);
        y += e.getY();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int rotation = e.getWheelRotation();
        ItemsBar itemsBar = game.getItemsBar();
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
