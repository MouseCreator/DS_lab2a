package org.example.winnie.executor;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class LibTaskPortfolioExecutor<V> implements TaskPortfolioExecutor<V>{
    int numThreads;
    public LibTaskPortfolioExecutor(int numThreads) {
        this.numThreads = numThreads;
    }
    @Override
    public void execute(Callable<V> task) {
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
        Future<V> future = executorService.submit(task);
    }

    @Override
    public V getResult() {
        return null;
    }
}
