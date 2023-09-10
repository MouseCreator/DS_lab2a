package org.example.winnie;

import org.example.util.RandomService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ForestArea {

    public AreaNode root = null;
    public Optional<AreaNode> root() {
        return Optional.ofNullable(root);
    }

    public void setRoot(AreaNode root) {
        this.root = root;
    }
    public void addNode(AreaNode node, AreaNode parent) {
        parent.leedsTo.add(node);
    }

    static class AreaNode {
        private final int processTime;
        private boolean hasWinniePooh;
        private final List<AreaNode> leedsTo;
        public AreaNode(int processTime, boolean hasWinniePooh) {
            this.processTime = processTime;
            this.hasWinniePooh = hasWinniePooh;
            leedsTo = new ArrayList<>();
        }
        public AreaNode(int processTime) {
            this.processTime = processTime;
            this.hasWinniePooh = false;
            leedsTo = new ArrayList<>();
        }
        public int getProcessTime() {
            return processTime;
        }
        public void hideWinniePooh() {
            this.hasWinniePooh = true;
        }
        public boolean hasWinniePooh() {
            return hasWinniePooh;
        }

        public void addConnection(AreaNode node) {
            leedsTo.add(node);
        }
        public List<AreaNode> getConnections() {
            return leedsTo;
        }

        public AreaNode randomChild() {
            RandomService r = new RandomService();
            return leedsTo.get(r.randomInt(leedsTo.size()));
        }
    }
}
