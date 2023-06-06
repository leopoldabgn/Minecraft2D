package gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import model.Game;
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

}
