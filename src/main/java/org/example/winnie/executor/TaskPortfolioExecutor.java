package org.example.winnie.executor;

import java.util.concurrent.Callable;

public interface TaskPortfolioExecutor<V> {
    void execute(Callable<V> taskPortfolioExecutor);

    V getResult();
    void stop();
}
