package model;

import java.awt.Graphics2D;

public class CraftingTable {

    private Item[][] craftingSlots;
    private Item craftedItem;

    public static int NB_CRAFTING_SLOTS = 9,
                      NB_SLOTS_LINE = (int)Math.sqrt(NB_CRAFTING_SLOTS),
                      MARGIN = 10,
                      WIDTH  = ItemsBar.ITEM_SIZE * (NB_SLOTS_LINE + 1) + 3 * MARGIN,
                      HEIGHT = ItemsBar.ITEM_SIZE * NB_SLOTS_LINE + 2 * MARGIN;

    public CraftingTable() {
        craftingSlots = new Item[NB_SLOTS_LINE][NB_SLOTS_LINE];
        for(int j=0;j<NB_SLOTS_LINE;j++)
            for(int i=0;i<NB_SLOTS_LINE;i++)
                craftingSlots[j][i] = new Item();
        craftedItem = new Item();
    }

    public void draw(Graphics2D g, int x, int y) {
        int size = (int)Math.sqrt(NB_CRAFTING_SLOTS);
        int initX = x;
        
        g.fillRoundRect(x, y, WIDTH, HEIGHT, 10, 10);
        x += MARGIN;
        y += MARGIN;

        for(int j=0;j<size;j++) {
            
            for(int i=0;i<size;i++, x += ItemsBar.ITEM_SIZE) {
                craftingSlots[j][i].drawItem(g, x, y);
            }

            // Si on est au milieu, on trace la case à droite
            if(j == size / 2) {
                x += MARGIN;
                craftedItem.drawItem(g, x, y);
            }

            x = initX + MARGIN;
            y += ItemsBar.ITEM_SIZE; //-= ?
        }

    }

}