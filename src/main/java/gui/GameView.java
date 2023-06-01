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
    
    private Timer timer = new Timer(1, this);

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
        Player p = game.getMainPlayer();

        if(map.canMovePlayer(p, p.getVelX(), p.getVelY())) {
            p.move();
        }
        else if(p.getVelX() != 0 && p.getVelY() != 0) {
            if(map.canMovePlayer(p, 0, p.getVelY()))
                p.move(true, false); // On interdit d'avancer sur x
            else if(map.canMovePlayer(p, p.getVelX(), 0))
                p.move(false, true);
        }

        if(!p.isJumping() && !p.isFalling() && !map.isOnGround(p)) {
            // Launch Thread
            map.startFalling(p);
        }

        mapView.repaint();
    }

}
