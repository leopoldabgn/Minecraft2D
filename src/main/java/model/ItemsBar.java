package model;

import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.ArrayList;

public class ItemsBar implements Serializable {
    
    private static final long serialVersionUID = 123123123L;

    private ArrayList<Item> items = new ArrayList<>();
    public static int ITEM_SIZE = 65,
                      NB_ITEMS = 9,
                      ITEMS_BAR_WIDTH = ITEM_SIZE * NB_ITEMS,
                      ITEMS_BAR_HEIGHT = ITEM_SIZE;

    private int currentItem;

    public ItemsBar() {
        for(int i=0;i<NB_ITEMS;i++)
            items.add(new Item());
        currentItem = 0;
        items.get(currentItem).setSelected(true);
    }

    public boolean addItem(Item item) {
        for (int index = 0; index < items.size(); index++) {
            Item i = items.get(index);
            if (i.isEmpty()) {
                items.set(index, item);
                return true;
            }
        }
        return false;
    }

    public void draw(Graphics2D g, int x, int y) {
        for(int i=0;i<NB_ITEMS;i++, x += ITEM_SIZE) {
            items.get(i).drawItem(g, x, y);
            if(i > 0 && items.get(i-1).isSelected())
                items.get(i-1).drawItem(g, x - ITEM_SIZE, y);
        }
    }

    public void prevItem() {
        if(currentItem == 0)
            return;
        items.get(currentItem--).setSelected(false);
        items.get(currentItem).setSelected(true);
    }

    public void nextItem() {
        if(currentItem == items.size()-1)
            return;
        items.get(currentItem++).setSelected(false);
        items.get(currentItem).setSelected(true);
    }

}
