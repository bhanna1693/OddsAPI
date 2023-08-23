package com.bhanna.oddsapi.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculatorTest {

    @Test
    public void testCalculateImpliedProbability() {
        double homeOdds = 2.0; // +100
        double awayOdds = 1.5; // -200

        double impliedProbabilityHome = Calculator.calculateImpliedProbability(homeOdds);
        double impliedProbabilityAway = Calculator.calculateImpliedProbability(awayOdds);

        double expectedImpliedProbabilityHome = 0.5;
        double expectedImpliedProbabilityAway = 0.6666666666666666;
        assertEquals(expectedImpliedProbabilityHome, impliedProbabilityHome);
        assertEquals(expectedImpliedProbabilityAway, impliedProbabilityAway);
    }

    @Test
    public void testCalculateNoVigFairOddsProbabilityHomeAway() {
        double impliedProbabilityHome = 0.5;
        double impliedProbabilityAway = 0.6666666666666666;

        double noVigFairOddsProbabilityHome = Calculator.calculateNoVigFairOddsProbability(impliedProbabilityHome, impliedProbabilityAway);
        double noVigFairOddsProbabilityAway = Calculator.calculateNoVigFairOddsProbability(impliedProbabilityAway, impliedProbabilityHome);

        double expectedNoVigProbabilityHome = 0.4285714285714286;
        double expectedNoVigProbabilityAway = 0.5714285714285715;

        double totalNoVigProbability = expectedNoVigProbabilityHome + expectedNoVigProbabilityAway;
        assertEquals(totalNoVigProbability, 1.0);
        assertEquals(expectedNoVigProbabilityHome, noVigFairOddsProbabilityHome);
        assertEquals(expectedNoVigProbabilityAway, noVigFairOddsProbabilityAway);
    }

    @Test
    public void testCalculateNoVigOdds() {
        double noVigProbabilityHome = 0.4285714285714286;
        double noVigProbabilityAway = 0.5714285714285715;

        double noVigOddsHome = Calculator.calculateOddsByProbability(noVigProbabilityHome);
        double noVigOddsAway = Calculator.calculateOddsByProbability(noVigProbabilityAway);

        double expectedNoVigOddsHome = 2.333333333333333;
        double expectedNoVigOddsAway = 1.7499999999999998;

        assertEquals(expectedNoVigOddsHome, noVigOddsHome);
        assertEquals(expectedNoVigOddsAway, noVigOddsAway);
    }

}
