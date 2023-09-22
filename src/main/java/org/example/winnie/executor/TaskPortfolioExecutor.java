package org.example.winnie.executor;

import java.util.concurrent.Callable;

public interface TaskPortfolioExecutor<V> {
    Result<V> execute(Callable<V> taskPortfolioExecutor);
    void stop();
}
