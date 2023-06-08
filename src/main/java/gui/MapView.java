package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import model.ItemsBar;
import model.Map;

public class MapView extends JPanel {
    
    private Map map;
    private ItemsBar itemsBar;

    public MapView(Map map, ItemsBar itemsBar) {
        this.map = map;
        this.itemsBar = itemsBar;
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

        if(map == null)
            return;

        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // On dessine la map
        map.draw(g2d);

        // On dessine la barre d'items
        itemsBar.draw(g2d, (getWidth() - ItemsBar.ITEMS_BAR_WIDTH) / 2,
                           getHeight() - ItemsBar.ITEMS_BAR_HEIGHT - 15);
    }

}
