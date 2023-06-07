package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import model.Textures;

public class ItemView extends JPanel {
    
    private static Color borderColor = new Color(100, 100, 100);

    public ItemView() {
        this.setPreferredSize(new Dimension(50, 50));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int borderSize = 4;

        g.drawImage(Textures.loadBackgroundTexture("sky"), borderSize, borderSize,
        getWidth() - borderSize*2, getHeight() - borderSize*2, null, null);

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(borderColor);
        g2d.setStroke(new BasicStroke(borderSize));
        g2d.drawRect(0, 0, getWidth(), getHeight());

    }

}   
