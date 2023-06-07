package gui;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JPanel;

public class ItemsBarView extends JPanel {
    
    ArrayList<ItemView> items;

    public ItemsBarView() {
        this.setPreferredSize(new Dimension(50 * 10, 50));
        items = new ArrayList<>();
        for(int i=0;i<10;i++) {
            ItemView item = new ItemView();
            items.add(item);
            this.add(item);
        }
    }

}
