package gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.Timer;

import model.Game;
import model.Map;
import model.Player;

public class GameView extends JPanel implements ActionListener {
    
    private Timer timer = new Timer(5, this);

    private Game game;
    private MapView mapView;

    public GameView(Game game) {
        this.game = game;
        this.mapView = new MapView(game.getMap());

        this.setBackground(Color.DARK_GRAY);
        
        this.add(mapView);
        
        timer.start();
    }

    public MapView getMapView() {
        return mapView;
    }

    @Override
    public void actionPerformed(ActionEvent action) {
        Map map = game.getMap();
        Player player = game.getMainPlayer();
        
        if(!player.isFalling() && !map.isOnGround(player)) {
            // Launch Thread
            map.startFalling(player);
        }

        mapView.repaint();
    }

}
