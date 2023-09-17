package org.example.winnie;

import org.example.winnie.executor.TaskPortfolioExecutor;

import java.util.concurrent.Callable;

public class BeesHunt {
    private final Forest forest;

    private final TaskPortfolioExecutor<Boolean> taskPortfolioExecutor;

    public BeesHunt(Forest forest, TaskPortfolioExecutor<Boolean> taskPortfolioExecutor) {
        this.forest = forest;
        this.taskPortfolioExecutor = taskPortfolioExecutor;
    }

    public boolean find() {
        for (ForestArea area : forest) {
            taskPortfolioExecutor.execute(new FindWinnieCallable(area));
        }
        return taskPortfolioExecutor.getResult();
    }


    private static class FindWinnieCallable implements Callable<Boolean> {

        private final ForestArea area;

        private FindWinnieCallable(ForestArea area) {
            this.area = area;
        }

        @Override
        public Boolean call() throws Exception {
            for (AreaNode node : area) {
                Thread.sleep(node.getProcessTime());
                if (node.hasWinniePooh())
                    return true;
            }
            return false;
        }
    }


}
