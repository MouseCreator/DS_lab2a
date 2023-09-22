package org.example.winnie;

import org.example.winnie.executor.Result;
import org.example.winnie.executor.TaskPortfolioExecutor;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;

public class BeesHuntImpl implements BeesHunt {

    private final TaskPortfolioExecutor<Integer> taskPortfolioExecutor;

    public BeesHuntImpl(TaskPortfolioExecutor<Integer> taskPortfolioExecutor) {
        this.taskPortfolioExecutor = taskPortfolioExecutor;
    }

    @Override
    public int find(Forest forest) {
        List<Result<Integer>> resultList = new LinkedList<>();
        for (ForestArea area : forest) {
            Result<Integer> result = taskPortfolioExecutor.execute(new FindWinnieCallable(area));
            resultList.add(result);
        }
        int winnieId = -1;

        taskPortfolioExecutor.start();

        for (Result<Integer> result : resultList) {
            int areaId;
            try {
                areaId = result.get();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (areaId != -1) {
                winnieId = areaId;
                break;
            }
        }
        taskPortfolioExecutor.stop();
        return winnieId;
    }

    private record FindWinnieCallable(ForestArea area) implements Callable<Integer> {

        @Override
            public Integer call() {
                for (AreaNode node : area) {
                    try {
                        Thread.sleep(node.getProcessTime());
                    } catch (InterruptedException e) {
                        return -1;
                    }
                    if (node.hasWinniePooh())
                        return area.getId();
                }
                return -1;
            }
        }


}
