package org.example.winnie.executor;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;

public class CustomTaskPortfolioExecutor<V> implements TaskPortfolioExecutor<V>{

    private List<Callable<V>> tasks = new LinkedList<>();
    @Override
    public void execute(Callable<V> taskPortfolioExecutor) {

    }

    @Override
    public V getResult() {
        return null;
    }
}
