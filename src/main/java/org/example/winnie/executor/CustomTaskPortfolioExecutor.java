package org.example.winnie.executor;

import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;

public class CustomTaskPortfolioExecutor<V> implements TaskPortfolioExecutor<V>{
    private Thread[] threads;

    private record TaskResultPair<V>(Callable<V> callableTask, Result<V> result) { }
    private final LinkedBlockingQueue<TaskResultPair<V>> tasks = new LinkedBlockingQueue<>();

    @Override
    public Result<V> execute(Callable<V> task) {
         Result<V> result = new Result<>();
         TaskResultPair<V> pair = new TaskResultPair<>(task, result);
         tasks.add(pair);
         return result;
    }

    private void createThreads(int numThreads) {
        threads = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(()->{
                TaskResultPair<V> taskPair;
                while (!Thread.interrupted()) {
                    if ((taskPair = tasks.poll()) != null) {
                        try {
                            Callable<V> task = taskPair.callableTask();
                            V result = task.call();
                            taskPair.result().set(result);
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
    public void stop() {
        for (Thread thread : threads) {
            thread.interrupt();
        }
    }
}
