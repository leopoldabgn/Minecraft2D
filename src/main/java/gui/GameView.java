package gui;

import java.awt.BorderLayout;
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
    private ItemsBarView itemsBarView;

    public GameView(Game game) {
        this.game = game;
        this.mapView = new MapView(game.getMap());
        this.itemsBarView = new ItemsBarView();
        this.setBackground(Color.DARK_GRAY);
        // this.setLayout(null);

        // mapView.setBounds(0, 0, getWidth(), getHeight());
        // itemsBarView.setLocation((getWidth() - itemsBarView.getWidth()) / 2,
        //                         getHeight() - itemsBarView.getHeight() - 20);

        this.add(mapView);
        // this.add(itemsBarView);
        
        timer.start();
    }

    public MapView getMapView() {
        return mapView;
    }

    @Override
    public void actionPerformed(ActionEvent action) {
        Map map = game.getMap();
        Player p = game.getMainPlayer();
        boolean movedPlayer = false;

        if(map.canMovePlayer(p, p.getVelX(), p.getVelY())) {
            movedPlayer = p.move();
        }
        else if(p.getVelX() != 0 && p.getVelY() != 0) {
            if(map.canMovePlayer(p, 0, p.getVelY()))
                movedPlayer = p.move(true, false); // On interdit d'avancer sur x
            else if(map.canMovePlayer(p, p.getVelX(), 0))
                movedPlayer = p.move(false, true);
        }

        // Si le joueur a bougé, on deplace la carte avec lui pour le garder au center
        if(movedPlayer) {
            int midPlayerX = (map.getWidth() / 2)  - (p.getSize() / 2);
            int midPlayerY = (map.getHeight() / 2) - (p.getSize() / 2);
            int mapX = p.getRealX() - midPlayerX;
            int mapY = p.getRealY() - midPlayerY;
            map.moveOrigin(mapX, mapY);
            // On rafraichit la texture par exemple si il marche
            p.refreshActionTexture();
        }

        if(!p.isJumping() && !p.isFalling() && !map.isOnGround(p)) {
            // Launch Thread
            map.startFalling(p);
        }


        mapView.repaint();
    }

}
