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

    // Define expected values based on the mock calculator values
//    Double expectedHomePrice = homeOdds;
//    Double expectedAwayPrice = awayOdds;
//    Double expectedImpliedProbabilityHome = 0.6666666666666666;
//    Double expectedImpliedProbabilityAway = 0.5;
//    Double expectedJuice = expectedImpliedProbabilityHome + expectedImpliedProbabilityAway - 1;
//    Double expectedNoVigProbabilityHome = expectedImpliedProbabilityHome / (expectedImpliedProbabilityHome + expectedImpliedProbabilityAway);
//    Double expectedNoVigProbabilityAway = expectedImpliedProbabilityAway / (expectedImpliedProbabilityAway + expectedImpliedProbabilityHome);
//    Double expectedNoVigOddsHome = 1 / expectedNoVigProbabilityHome;
//    Double expectedNoVigOddsAway = 1 / expectedNoVigProbabilityAway;
    @Test
    public void testCalculateJuiceHomeAway() {
        double impliedProbabilityHome = 0.5;
        double impliedProbabilityAway = 0.6666666666666666;

        double juiceHome = Calculator.calculateJuice(impliedProbabilityHome, impliedProbabilityAway);
        double juiceAway = Calculator.calculateJuice(impliedProbabilityAway, impliedProbabilityHome);

        double expectedJuiceHome = 0.16666666666666652;
        double expectedJuiceAway = 0.16666666666666652;

        assertEquals(expectedJuiceHome, expectedJuiceAway);
        assertEquals(expectedJuiceHome, juiceHome);
        assertEquals(expectedJuiceAway, juiceAway);
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
