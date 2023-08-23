package com.bhanna.oddsapi.model;

import com.bhanna.oddsapi.util.Calculator;

public class OutcomeResultMapper {
    /**
     * generate OutcomeResult for Sharpest bookmaker
     *
     * @param bookmaker
     * @param homeName
     * @param awayName
     * @param homeOdds
     * @param awayOdds
     * @return
     */
    public static OutcomeResult generate(String bookmaker, String homeName, String awayName, double homeOdds, double awayOdds) {
        double impliedProbabilityHome = Calculator.calculateImpliedProbability(homeOdds);
        double impliedProbabilityAway = Calculator.calculateImpliedProbability(awayOdds);
        double noVigProbabilityHome = Calculator.calculateNoVigFairOddsProbability(impliedProbabilityHome, impliedProbabilityAway);
        double noVigProbabilityAway = Calculator.calculateNoVigFairOddsProbability(impliedProbabilityAway, impliedProbabilityHome);
        double noVigOddsHome = Calculator.calculateOddsByProbability(noVigProbabilityHome);
        double noVigOddsAway = Calculator.calculateOddsByProbability(noVigProbabilityAway);
        double homeEdgePercent = Calculator.calculateExpectedValuePercentage(homeOdds, noVigProbabilityHome);
        double awayEdgePercent = Calculator.calculateExpectedValuePercentage(awayOdds, noVigProbabilityAway);

        return new OutcomeResult(bookmaker, homeName, awayName, homeOdds, awayOdds, impliedProbabilityHome, impliedProbabilityAway, noVigProbabilityHome, noVigProbabilityAway, noVigOddsHome, noVigOddsAway, homeEdgePercent, awayEdgePercent);
    }

    public static OutcomeResult generate(String bookmaker, String homeName, String awayName, double homeOdds, double awayOdds, OutcomeResult sharpestOutcome) {
        double impliedProbabilityHome = Calculator.calculateImpliedProbability(homeOdds);
        double impliedProbabilityAway = Calculator.calculateImpliedProbability(awayOdds);
        double noVigProbabilityHome = Calculator.calculateNoVigFairOddsProbability(impliedProbabilityHome, impliedProbabilityAway);
        double noVigProbabilityAway = Calculator.calculateNoVigFairOddsProbability(impliedProbabilityAway, impliedProbabilityHome);
        double noVigOddsHome = Calculator.calculateOddsByProbability(noVigProbabilityHome);
        double noVigOddsAway = Calculator.calculateOddsByProbability(noVigProbabilityAway);
        double homeEdgePercent = Calculator.calculateExpectedValuePercentage(homeOdds, sharpestOutcome.noVigProbabilityHome());
        double awayEdgePercent = Calculator.calculateExpectedValuePercentage(awayOdds, sharpestOutcome.noVigProbabilityAway());

        return new OutcomeResult(bookmaker, homeName, awayName, homeOdds, awayOdds, impliedProbabilityHome, impliedProbabilityAway, noVigProbabilityHome, noVigProbabilityAway, noVigOddsHome, noVigOddsAway, homeEdgePercent, awayEdgePercent);
    }

}
