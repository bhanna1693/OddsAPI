package com.bhanna.oddsapi.util;

public class Calculator {
    public static double UNIT_SIZE = 10.0;

    public static int decimalToAmericanOdds(double decimalOdds) {
        if (decimalOdds >= 2.00) {
            return (int) Math.round((decimalOdds - 1) * 100);
        } else {
            return (int) Math.round(-100 / (decimalOdds - 1));
        }
    }


    public static double calculateImpliedProbability(double odds) {
        if (odds > 0) {
            return 1 / odds;
        } else {
            return -odds / (-odds + 1);
        }
    }

    public static double calculateMarketWidth(double odds, double pinnacleOdds) {
        return (odds - pinnacleOdds) * 100;
    }

    public static double calculateProbabilityOfWinning(double odds) {
        return 1.0 / odds;
    }

    public static double calculateProbabilityOfLosing(double probabilityOfWinning) {
        return 1.0 - probabilityOfWinning;
    }


    /**
     * EV = (Pw * Pp) − (Pl * A)
     * <p>
     * Pw - Probability of winning
     * Pp - Potential profit
     * Pl - Potential of losing
     * A  - Amount wagered
     */
    public static double calculateExpectedValue(double fairWinProbability, double potentialProfit, double fairLossProbability, double amountWagered) {
        return (fairWinProbability * potentialProfit) - (fairLossProbability * amountWagered);
    }

    /**
     * f* = (bp - q) / b
     * <p>
     * f* is the fraction of the bankroll to bet.
     * b is the odds received on the bet (decimal odds, not fractional).
     * p is the probability of winning.
     * q is the probability of losing, which is 1 - p.
     */
    public static double calculateKellyPercentOfBankRoll(double odds) {
        double probabilityOfWinning = 1 - calculateProbabilityOfWinning(odds);
        double probabilityOfLosing = 1 - probabilityOfWinning;
        return (odds * probabilityOfWinning - probabilityOfLosing) / odds;
    }

    public double calculateKellyPercentOfBankRoll(double odds, double pinnacleOdds) {
        double probabilityOfWinning = 1 - calculateProbabilityOfWinning(pinnacleOdds);
        double probabilityOfLosing = 1 - probabilityOfWinning;
        return (pinnacleOdds * probabilityOfWinning - probabilityOfLosing) / pinnacleOdds;
    }
}
