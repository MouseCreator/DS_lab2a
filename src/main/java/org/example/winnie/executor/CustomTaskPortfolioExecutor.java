package org.example.winnie.executor;

import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;

public class CustomTaskPortfolioExecutor<V> implements TaskPortfolioExecutor<V>{
    private Thread[] threads;
    private final LinkedBlockingQueue<Callable<V>> tasks = new LinkedBlockingQueue<>();
    private final LinkedBlockingQueue<V> results = new LinkedBlockingQueue<>();
    @Override
    public void execute(Callable<V> task) {
        tasks.add(task);
    }

    private void createThreads(int numThreads) {
        threads = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(()->{
                Callable<V> task;
                while (!Thread.interrupted()) {
                    if ((task = tasks.poll()) != null) {
                        try {
                            V result = task.call();
                            results.add(result);
                            break;
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });
        }
    }

    @Override
    public V getResult() {
        return results.poll();
    }

    @Override
    public void stop() {
        for (Thread thread : threads) {
            thread.interrupt();
        }
    }
}
