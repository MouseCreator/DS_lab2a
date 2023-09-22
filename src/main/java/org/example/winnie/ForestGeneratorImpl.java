package org.example.winnie;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ForestGeneratorImpl implements ForestGenerator {
    private final Random random = new Random();
    @Override
    public Forest generateForest(int targetAreaSizeNodes, int targetAreaNum, int averageProcessTimeMillis) {
        Forest forest = new Forest();
        int winnieThePoohArea = generateAreaNum(targetAreaNum);
        for (int i = 0; i < targetAreaNum; i++) {
            double areaSizeDistribution = 0.25;
            int size = (int) (targetAreaSizeNodes * (1 + random.nextDouble(-areaSizeDistribution, areaSizeDistribution)));
            ForestArea area = generateAreaNum(i, size, averageProcessTimeMillis);
            if (i == winnieThePoohArea) {
                hideWinnie(area);
            }
            forest.addArea(area);
        }
        return forest;
    }

    private void hideWinnie(ForestArea area) {
        int nodeId = generateAreaNum(area.getSize());
        hide(area.root(), 0, nodeId);
    }
    private int hide(AreaNode node, int id, int target) {
        if (id == target) {
            node.hideWinniePooh();
            return -1;
        }
        int nodesVisited = id + 1;
        for (AreaNode child : node.getConnections()) {
            int visited = hide(child, nodesVisited, target);
            if (visited == -1)
                return -1;
            else
                nodesVisited = visited;
        }
        return nodesVisited;
    }

    private int generateAreaNum(int targetAreaNum) {
        if (targetAreaNum < 1)
            throw new RuntimeException("Cannot hide Winnie the Pooh in negative area!");
        if (targetAreaNum == 1)
            return 0;
        return random.nextInt(targetAreaNum);
    }

    private ForestArea generateAreaNum(int id, int size, int averageProcessTime) {
        ForestArea forestArea = new ForestArea(id);
        List<AreaNode> areaNodes = new ArrayList<>();
        AreaNode root = createNode(averageProcessTime);
        forestArea.setSize(size);
        size--;
        areaNodes.add(root);
        forestArea.setRoot(root);
        for (int i = 0; i < size; i++) {
            AreaNode parent = randomNode(areaNodes);
            forestArea.addNode(parent, createNode(averageProcessTime));
        }
        return forestArea;
    }

    private AreaNode randomNode(List<AreaNode> areaNodes) {
        if (areaNodes.isEmpty())
            throw new RuntimeException("Empty area nodes list in Forest Generator");
        if (areaNodes.size() == 1)
            return areaNodes.get(0);
        return areaNodes.get(random.nextInt(areaNodes.size()));
    }

    private static AreaNode createNode(int averageProcessTime) {
        return new AreaNode(averageProcessTime);
    }


}
