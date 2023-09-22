package org.example.fist;

import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class FistRoadFight implements WinnerSelector {

    private final Random random = new Random();

    @Override
    public int selectWinner(int[] input) {
        ForkJoinPool pool = ForkJoinPool.commonPool();
        return pool.invoke(new MaxFinderTask(input, 0, input.length - 1));
    }

    private class MaxFinderTask extends RecursiveTask<Integer> {
        private final int[] array;
        private final int start;
        private final int end;

        public MaxFinderTask(int[] array, int start, int end) {
            this.array = array;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Integer compute() {
            if (start == end) {
                return start;
            }

            int middle = (start + end) >>> 1;

            MaxFinderTask leftTask = new MaxFinderTask(array, start, middle);
            MaxFinderTask rightTask = new MaxFinderTask(array, middle + 1, end);

            invokeAll(leftTask, rightTask);

            int leftMaxIndex = leftTask.join();
            int rightMaxIndex = rightTask.join();

            if (array[leftMaxIndex] ==array[rightMaxIndex]) {
                return random.nextBoolean() ? leftMaxIndex : rightMaxIndex;
            }

            return (array[leftMaxIndex] > array[rightMaxIndex]) ? leftMaxIndex : rightMaxIndex;
        }
    }
}
