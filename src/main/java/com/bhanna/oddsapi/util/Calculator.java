package com.bhanna.oddsapi.util;

public class Calculator {

    public static double calculateImpliedProbability(double odds) {
        if (odds > 0) {
            return 1 / (odds + 1);
        } else {
            return -odds / (-odds + 1);
        }
    }

    public static double calculateZeroVigOdds(double impliedProbability) {
        if (impliedProbability > 0.5) {
            return 1 / (2 * impliedProbability - 1);
        } else {
            return -1 / (2 * impliedProbability - 1);
        }
    }

    public static double calculateMarketWidth(double odds, double pinnacleOdds) {
        return (odds - pinnacleOdds) * 100;
    }


    public double getExpectedValueForOutcome(double odds, double pinnacleOdds) {
        double amountWagered = 100.0;
        double probabilityOfWinning = calculateProbabilityOfWinning(pinnacleOdds);
        double potentialProfit = probabilityOfWinning * amountWagered;
        double probabilityOfLosing = calculateProbabilityOfLosing(probabilityOfWinning);

        return calculateExpectedValue(probabilityOfWinning, potentialProfit, probabilityOfLosing, amountWagered);
    }

    public static double getExpectedValueForOutcome(double odds) {
        double amountWagered = 100.0;
        double probabilityOfWinning = calculateProbabilityOfWinning(odds);
        double potentialProfit = probabilityOfWinning * amountWagered;
        double probabilityOfLosing = calculateProbabilityOfLosing(probabilityOfWinning);

        return calculateExpectedValue(probabilityOfWinning, potentialProfit, probabilityOfLosing, amountWagered);
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
