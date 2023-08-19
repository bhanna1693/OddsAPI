package com.bhanna.oddsapi.util;

public class Calculator {

    public static int decimalToAmericanOdds(double decimalOdds) {
        if (decimalOdds >= 2.00) {
            return (int) Math.round((decimalOdds - 1) * 100);
        } else {
            return (int) Math.round(-100 / (decimalOdds - 1));
        }
    }


    /**
     * decimal odds are always positive
     * @param odds
     * @return the implied probability of a bet based on the odds expressed as a double (i.e. 50% = 0.5)
     */
    public static double calculateImpliedProbability(double odds) {
        return 1.0 / odds;
    }

    /**
     *
     * @param impliedProbabilitySideOne
     * @param impliedProbabilitySideTwo
     * @return the probability of a bet with the vig (juice) removed expressed as a double (i.e. 50% = 0.5)
     */
    public static double calculateNoVigFairOddsProbability(double impliedProbabilitySideOne, double impliedProbabilitySideTwo) {
        return impliedProbabilitySideOne / (impliedProbabilitySideOne + impliedProbabilitySideTwo);
    }

    /**
     *
     * @param probability likelihood of an outcome expressed as a double (i.e. 50% = 0.5)
     * @return the expected odds based on the probability expressed in decimal format
     */
    public static double calculateOddsByProbability(double probability) {
        return 1 / probability;
    }

    /**
     * @param impliedProbabilitySideOne
     * @param impliedProbabilitySideTwo
     * @return the amount of juice/vig for a bet that has 2 outcomes
     */
    public static double calculateJuice(double impliedProbabilitySideOne, double impliedProbabilitySideTwo) {
        return (impliedProbabilitySideOne + impliedProbabilitySideTwo) - 1;
    }

    public static double calculateMarketWidth(double odds, double pinnacleOdds) {
        return (odds - pinnacleOdds) * 100;
    }

}
