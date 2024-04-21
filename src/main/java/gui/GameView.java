package gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.TimerTask;

import javax.swing.JPanel;
import javax.swing.Timer;

import model.Game;
import model.Map;
import model.Mob;
import model.Player;
import model.Mob.MobAction;

public class GameView extends JPanel implements ActionListener {
    
    private Timer timer = new Timer(1, this);

    private Game game;
    private MapView mapView;

    public GameView(Game game) {
        this.game = game;
        this.mapView = new MapView(game.getMap(), game.getPlayerItemsBar());
        this.setBackground(Color.DARK_GRAY);
        
        this.add(mapView);
        
        timer.start();
        
        startMobMovements();
    }

    public MapView getMapView() {
        return mapView;
    }

    private final Random random = new Random();
    private final java.util.Timer mobMoveTimer = new java.util.Timer();

    public void startMobMovements() {
        final Map map = game.getMap();

        mobMoveTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                MobAction mobA;
                for (Mob mob : game.getMobs()) {
                    mobA = mob.getAction();
                    if (random.nextBoolean()) {
                        // Définir un mouvement aléatoire
                        int velX = random.nextInt(3) - 1; // -1, 0, ou 1

                        if(velX != 0) {
                            mob.setWalking(velX);
                            mobA.setWalking(velX < 0);
                        }
                    } else {
                        // Arrêter le mouvement
                        mob.stopWalking();
                    }
                    
                    // 10% de chance de sauter
                    if (random.nextDouble() < 0.1) {
                        if(map.isOnGround(mob) && !mob.isJumping()) {
                            mobA.setJumping(mobA.isWalkingLeft());
                            mob.setJumping(true);
                        }
                    }
                }
            }
        }, 0, 1000); // Déclenche toutes les 1 seconde
    }

    @Override
    public void actionPerformed(ActionEvent action) {
        Map map = game.getMap();

        // Selon la velocité en Y et en X, on effectue
        // les mouvements possibles pour les joueurs/mobs

        boolean mainPlayermoved = false;
        Player mainPlayer = game.getMainPlayer();

        // On met a jour les joueurs
        for(Player p : game.getPlayers()) {
            if(p == mainPlayer)
                mainPlayermoved = p.update(map);// If isOnScreen() ?? TODO
            else
                p.update(map);
        }
        
        // On met a jour les mobs
        for(Mob m : game.getMobs())
            m.update(map); // If isOnScreen() ?? TODO

        // Si le joueur a bougé, on deplace la carte avec lui pour le garder au centre
        if(mainPlayermoved) {
            int midPlayerX = (map.getWidth() / 2)  - (mainPlayer.getSize() / 2);
            int midPlayerY = (map.getHeight() / 2) - (mainPlayer.getSize() / 2);
            int mapX = mainPlayer.getRealX() - midPlayerX;
            int mapY = mainPlayer.getRealY() - midPlayerY;
            map.moveOrigin(mapX, mapY);
            // On rafraichit la texture par exemple si il marche
            mainPlayer.refreshActionTexture();
        }

        // On rafraichit l'affichage
        mapView.repaint();
    }

}
