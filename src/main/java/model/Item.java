package model;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;

public class Item {

    public static Color ITEM_BORDER = new Color(120, 130, 140);
    public static Color ITEM_BORDER_SELECTED = new Color(180, 180, 180);
    public static Color EMPTY = new Color(0, 0, 0, 127);

    protected String texture;
    private int nb = 0, maxStack = 64; // nombre de fois qu'il possède l'item
    private boolean isSelected;

    public Item() {}

    public Item(String texture, int maxStack) {
        this(texture, 1, maxStack);
    }

    public Item(String texture, int nb, int maxStack) {
        this.texture = texture;
        this.maxStack = maxStack;
        if(texture != null) {
            this.nb = nb;
        }
    }

    protected void drawItem(Graphics2D g, int x, int y) {
        int size = ItemsBar.ITEM_SIZE, borderSize = 4;
        
        // Un item vide a un attribut nb = 0
        if(!isEmpty()) {
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

    public int getNb() {
        return nb;
    }

    public void setNb(int nb) {
        if(texture == null) {
            System.err.println("texture null, you cannot change the nb");
            return;
        }
        this.nb = nb < 0 ? 0 : nb;
    }

    public int getMaxStack() {
        return maxStack;
    }

    public boolean add(int nb) {
        if(this.nb + nb > maxStack || this.nb - nb < 0)
            return false;
        this.nb += nb;
        return true;
    }

    public void addOne() {
        setNb(nb+1);
    }

    public void removeOne() {
        setNb(nb-1);
    }

    public int remainingBeforeFull() {
        return maxStack - nb;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        this.isSelected = selected;
    }

    public boolean isEmpty() {
        return nb == 0;
    }

    public void reset(Item item) {
        this.texture = item.texture;
        this.nb = item.nb;
        this.maxStack = item.maxStack;
        this.isSelected = item.isSelected;
    }

    // TODO: Corriger la methode equals, elle fonctionne mal
    // La destruction de block ne fonctionne pas et remove le mauvais de la liste
    // @Override
    // public boolean equals(Object obj) {
    //     return ((Item)obj).texture.equals(texture);
    // }

}
