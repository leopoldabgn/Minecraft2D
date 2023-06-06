package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import model.Map;
import model.Textures;

public class MapView extends JPanel {
    
    private Map map;

    public MapView(Map map) {
        this.map = map;
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(map.getWidth(), map.getHeight()));
    }

    @Override
    public void setPreferredSize(Dimension prefSize) {
        super.setPreferredSize(prefSize);
        map.setSize(prefSize);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponents(g);
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
       
        // On dessine l'arrière plan
        Image sky = Textures.loadBackgroundTexture("sky");
        g.drawImage(sky, 0, 0, getWidth(), getHeight(), null, null);

        if(map == null)
            return;
        // On dessine la map
        map.draw(g);
    }

}
