package model;

public class MapGenerator {
    
    protected static Map allBlocksMap(Game game, int width, int height) {
        Map map = Map.empty(game, width, height);

        int size = BlockType.values().length;
        int j=0;
        for(BlockType bt : BlockType.values()) {
            System.out.println(bt);
            for(int i=0;i<10;i++) {
                map.pushBlock(bt, -size/2 + j, i+2);
            }
            j++;
        }

        j=0;
        for(BlockType bt : BlockType.values()) {
            System.out.println(bt);
            for(int i=-12;i<-2;i++) {
                map.pushBlock(bt, -size/2 + j, i+2);
            }
            j++;
        }

        return map;
    }

}
