package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import model.Map;

public class MapView extends JPanel {
    
    private Map map;

    public MapView(Map map) {
        this.map = map;
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(map.getWidth(), map.getHeight()));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponents(g);
        // On dessine l'arrière plan
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        if(map == null)
            return;
        // On dessine la map
        map.draw(g);
    }

}
