package org.example.winnie;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ForestGeneratorImpl implements ForestGenerator {
    private Random random = new Random();

    @Override
    public Forest generateForest(int targetAreaSizeNodes, int targetAreaNum, int averageProcessTimeMillis) {
        Forest forest = new Forest();
        for (int i = 0; i < targetAreaNum; i++) {
            double areaSizeDistribution = 0.25;
            int size = (int) (targetAreaSizeNodes * (1 + random.nextDouble(-areaSizeDistribution, areaSizeDistribution)));
            ForestArea area = generateArea(size, averageProcessTimeMillis);
            forest.areasList.add(area);
        }
        return forest;
    }

    private ForestArea generateArea(int size, int averageProcessTime) {
        ForestArea forestArea = new ForestArea();
        List<ForestArea.AreaNode> areaNodes = new ArrayList<>();
        ForestArea.AreaNode root = createNode(averageProcessTime);
        size--;
        areaNodes.add(root);
        forestArea.setRoot(root);
        for (int i = 0; i < size; i++) {
            ForestArea.AreaNode parent = randomNode(areaNodes);
            forestArea.addNode(parent, createNode(averageProcessTime));
        }
        return forestArea;
    }

    private ForestArea.AreaNode randomNode(List<ForestArea.AreaNode> areaNodes) {
        if (areaNodes.isEmpty())
            throw new RuntimeException("Empty area nodes list in Forest Generator");
        if (areaNodes.size() == 1)
            return areaNodes.get(0);
        return areaNodes.get(random.nextInt(areaNodes.size()));
    }

    private static ForestArea.AreaNode createNode(int averageProcessTime) {
        return new ForestArea.AreaNode(averageProcessTime);
    }


}
