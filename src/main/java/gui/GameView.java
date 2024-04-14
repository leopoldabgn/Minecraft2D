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

    public void refreshMob(Map map, Mob m) {
        boolean movedMob = false;

        if (map.canMoveMob(m, m.getVelX(), m.getVelY())) {
            movedMob = m.move();
        } else if (m.getVelX() != 0 && m.getVelY() != 0) {
            if (map.canMoveMob(m, 0, m.getVelY()))
                movedMob = m.move(true, false); // On interdit d'avancer sur x
            else if (map.canMoveMob(m, m.getVelX(), 0))
                movedMob = m.move(false, true);
        }

        if (!m.isJumping() && !m.isFalling() && !map.isOnGround(m)) {
            map.startFalling(m);
        }

        // Si le joueur a bougé, on deplace la carte avec lui pour le garder au centre
        if(movedMob && m == game.getMainPlayer()) {
            int midPlayerX = (map.getWidth() / 2)  - (m.getSize() / 2);
            int midPlayerY = (map.getHeight() / 2) - (m.getSize() / 2);
            int mapX = m.getRealX() - midPlayerX;
            int mapY = m.getRealY() - midPlayerY;
            map.moveOrigin(mapX, mapY);
            // On rafraichit la texture par exemple si il marche
            m.refreshActionTexture();
        }

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
                        
                        mob.setVelX(velX);
                        if(velX != 0)
                            mobA.setWalking(velX < 0);
                    } else {
                        // Arrêter le mouvement
                        mob.setVelX(0);
                        // mob.setVelY(0);
                    }
                    
                    // 10% de chance de sauter
                    if (random.nextDouble() < 0.1) {
                        if(map.isOnGround(mob) && !mob.isJumping()) {
                            mobA.setJumping(mobA.isWalkingLeft());
                            map.startJumping(mob);
                        }
                    }
                }
            }
        }, 0, 1000); // Déclenche toutes les 1 seconde
    }

    @Override
    public void actionPerformed(ActionEvent action) {
        Map map = game.getMap();

        // On met a jour les joueurs
        for(Player p : game.getPlayers())
            refreshMob(map, p);
        
        // On met a jour les mobs
        for(Mob m : game.getMobs())
            refreshMob(map, m); // If isOnScreen() ??

        // On rafraichit l'affichage
        mapView.repaint();
    }

}
