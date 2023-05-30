package gui;

import java.awt.Color;

import javax.swing.JPanel;

import model.Game;

public class GameView extends JPanel {
    
    private Game game;
    private MapView mapView;

    public GameView(Game game) {
        this.game = game;
        this.mapView = new MapView(game.getMap());

        this.setBackground(Color.DARK_GRAY);
        
        this.add(mapView);
    }

}
