package org.example.winnie;

import org.example.winnie.executor.Result;
import org.example.winnie.executor.TaskPortfolioExecutor;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;

public class BeesHunt {

    private final TaskPortfolioExecutor<Integer> taskPortfolioExecutor;

    public BeesHunt(TaskPortfolioExecutor<Integer> taskPortfolioExecutor) {
        this.taskPortfolioExecutor = taskPortfolioExecutor;
    }

    public int find(Forest forest) {
        List<Result<Integer>> resultList = new LinkedList<>();
        for (ForestArea area : forest) {
            Result<Integer> result = taskPortfolioExecutor.execute(new FindWinnieCallable(area));
            resultList.add(result);
        }
        int winnieId = -1;
        for (Result<Integer> result : resultList) {
            int areaId = result.get();
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
