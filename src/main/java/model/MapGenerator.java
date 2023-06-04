package model;

import java.util.ArrayList;
import java.util.List;

public class MapGenerator {

    protected static Map allBlocksMap(Game game, int width, int height) {
        Map map = Map.empty(game, width, height);

        int size = BlockType.values().length;
        int j=0;
        for(BlockType bt : BlockType.values()) {
            for(int i=0;i<10;i++) {
                map.pushBlock(bt, -size/2 + j, i+2);
            }
            j++;
        }

        j=0;
        for(BlockType bt : BlockType.values()) {
            for(int i=-12;i<-2;i++) {
                map.pushBlock(bt, -size/2 + j, i+2);
            }
            j++;
        }

        return map;
    }

    protected static Map underground(Game game, int width, int height) {
        Map map = Map.empty(game, width, height);
        
        int y = 1;
        map.pushLayer(map, BlockType.GRASS, y++);
        
        for(int i=0;i<2;i++) {
            map.pushLayer(map, BlockType.DIRT, y++);
        }

        List<BlockType> blocks = new ArrayList<>();
        List<Double> probabilities = new ArrayList<>();


        for (int i = 4; i <= 63; i++) {
            double probaStone = 1.0;

            // Ajout des minerais avec intervalle de couches correspondant
            for (Ore ore : Ore.values()) {
                int[] layers = ore.getLayers();
                if (i >= -layers[0] && i <= -layers[1]) {
                    blocks.add(ore.getBlockType());
                    probabilities.add(ore.getProba());
                    probaStone -= ore.getProba();
                }
            }

            blocks.add(BlockType.STONE);
            probabilities.add(probaStone);
    
            sortBlocksByProbability(blocks, probabilities);

            // Conversion des listes en tableaux pour les passer à la fonction pushLayer
            BlockType[] blockArray = blocks.toArray(new BlockType[0]);
            double[] probaArray = probabilities.stream().mapToDouble(Double::doubleValue).toArray();

            map.pushLayer(map, blockArray, probaArray, y++);
            blocks.clear();
            probabilities.clear();
        }

        map.pushLayer(map, BlockType.BEDROCK, y);

        return map;
    }

    public static void sortBlocksByProbability(List<BlockType> blocks, List<Double> probabilities) {
        int size = blocks.size();
        if (size != probabilities.size()) {
            throw new IllegalArgumentException("The size of the input lists must be the same.");
        }
    
        // Création d'une liste d'indices pour conserver la correspondance entre les blocs et les probabilités
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            indices.add(i);
        }
    
        // Tri des indices en fonction des probabilités dans l'ordre décroissant
        indices.sort((i, j) -> Double.compare(probabilities.get(j), probabilities.get(i)));
    
        // Création de listes temporaires pour les blocs et les probabilités
        List<BlockType> tempBlocks = new ArrayList<>(size);
        List<Double> tempProbabilities = new ArrayList<>(size);
    
        // Remplissage des listes temporaires en utilisant les indices triés
        for (int index : indices) {
            tempBlocks.add(blocks.get(index));
            tempProbabilities.add(probabilities.get(index));
        }
    
        // Mise à jour des listes d'origine avec les listes temporaires triées
        blocks.clear();
        blocks.addAll(tempBlocks);
        probabilities.clear();
        probabilities.addAll(tempProbabilities);
    }


}
