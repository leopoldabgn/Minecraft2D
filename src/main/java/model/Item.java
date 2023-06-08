package model;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.plaf.TextUI;

public class Item {

    public static Color ITEM_BORDER = new Color(120, 130, 140);
    public static Color ITEM_BORDER_SELECTED = new Color(180, 180, 180);
    public static Color EMPTY = new Color(0, 0, 0, 127);

    protected String texture;
    private int nb = 0; // nombre de fois qu'il possède l'item
    private boolean isSelected;

    public Item() {}

    public Item(String texture) {
        this.texture = texture;
        if(texture != null)
            nb = 1;
    }

    protected void drawItem(Graphics2D g, int x, int y) {
        int size = ItemsBar.ITEM_SIZE, borderSize = 4;

        // Un item vide a un attribut nb = 0
        if(nb != 0) {
            Image img = Textures.loadItemTexture(texture);
            g.drawImage(img, x + borderSize, y + borderSize,
            size - borderSize*2, size - borderSize*2, null, null);
        }
        else {
            // Si pas d'image
            g.setColor(EMPTY);
            g.fillRect(x + borderSize, y + borderSize, size - borderSize*2, size - borderSize*2);
        }
        if(isSelected) {
            g.setColor(new Color(0, 0, 0, 20));
            g.fillRect(x, y, size+borderSize*2, size+borderSize*2);
        }

        if(isSelected) {
            borderSize *= 2;
            g.setColor(ITEM_BORDER_SELECTED);
        }
        else {
            g.setColor(ITEM_BORDER);
        }
        g.setStroke(new BasicStroke(borderSize));
        g.drawRect(x, y, size, size);
    }

    public void setTexture(String texture) {
        this.texture = texture;
        nb = texture == null ? 0 : 1;
    }

    public void setNb(int nb) {
        if(texture == null) {
            System.err.println("texture null, you cannot change the nb");
            return;
        }
        this.nb = nb < 0 ? 0 : nb;
    }

    public void addOne() {
        setNb(nb+1);
    }

    public void removeOne() {
        setNb(nb-1);
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        this.isSelected = selected;
    }

}
