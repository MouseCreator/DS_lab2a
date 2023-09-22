package org.example.fist;

public class Main {
    public static void main(String[] args) {
        EnergyGenerator energyGenerator = new EnergyGeneratorImpl();

        int N = 10_000_000;
        int energyLimit = 10_000;

        int[] fighters = energyGenerator.generate(N, energyLimit);

        WinnerSelector fistRoadFight = new FistRoadFight();
        int winner = fistRoadFight.selectWinner(fighters);

        String palace = winner < N >>> 1 ? "Guan Yin" : "Guan-Yan";

        System.out.printf("The winner is fighter number %d from %s with Qi energy of %d!", winner, palace, fighters[winner]);
    }
}
