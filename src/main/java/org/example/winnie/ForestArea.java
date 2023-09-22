package org.example.winnie;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class ForestArea implements Iterable<AreaNode> {
    public ForestArea(int id) {
        this.id = id;
    }

    private final int id;
    AreaNode root = null;
    public AreaNode root() {
        return root;
    }
    public void setRoot(AreaNode root) {
        this.root = root;
    }
    public void addNode(AreaNode parent, AreaNode node) {
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

    public int getId() {
        return id;
    }
}


class AreaNode {
    private final int processTime;
    private boolean hasWinniePooh;
    private final List<AreaNode> leedsTo;
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
    public List<AreaNode> getConnections() {
        return leedsTo;
    }

}
