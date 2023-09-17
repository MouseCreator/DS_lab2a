package org.example.winnie;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Forest implements Iterable<ForestArea> {
    private final List<ForestArea> areasList = new ArrayList<>();
    public void addArea(ForestArea area) {
        this.areasList.add(area);
    }

    @Override
    public Iterator<ForestArea> iterator() {
        return areasList.iterator();
    }
}
