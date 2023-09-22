package org.example.winnie;

import org.example.winnie.executor.CustomTaskPortfolioExecutor;
import org.example.winnie.executor.TaskPortfolioExecutor;

public class Main {
    public static void main(String[] args) {

        ForestGenerator forestGenerator = new ForestGeneratorImpl();
        Forest forest = forestGenerator.generateForest(10, 20, 10);

        TaskPortfolioExecutor<Integer> executor = new CustomTaskPortfolioExecutor<>(Runtime.getRuntime().availableProcessors()-1);

        BeesHunt hunt = new BeesHunt(executor);

        int winnieAreaId = hunt.find(forest);

        if (winnieAreaId != -1) {
            System.out.printf("Winnie was found in area %d\n", winnieAreaId);
        } else {
            System.out.println("Winnie was not found!");
        }

    }
}