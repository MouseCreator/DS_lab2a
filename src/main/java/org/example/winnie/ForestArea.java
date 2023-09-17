package org.example.winnie;

import org.example.util.RandomService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class ForestArea implements Iterable<AreaNode> {

    public AreaNode root = null;
    public AreaNode root() {
        return root;
    }
    public void setRoot(AreaNode root) {
        this.root = root;
    }
    public void addNode(AreaNode node, AreaNode parent) {
        parent.getConnections().add(node);
    }
    private int size = 0;
    public void setSize(int size) {
        this.size = size;
    }
    public int getSize() {
        return size;
    }


    @Override
    public Iterator<AreaNode> iterator() {
        return new Iterator<>() {
            final Queue<AreaNode> nodes = new LinkedBlockingQueue<>();
            AreaNode next = root;
            @Override
            public boolean hasNext() {
                return next != null;
            }

            @Override
            public AreaNode next() {
                nodes.addAll(next.getConnections());
                AreaNode toReturn = next;
                next = nodes.poll();
                return toReturn;
            }
        };
    }
}


class AreaNode {
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
