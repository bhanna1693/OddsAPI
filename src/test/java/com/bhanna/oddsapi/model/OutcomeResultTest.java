package com.bhanna.oddsapi.model;

import com.bhanna.oddsapi.model.OddsApi.OddsApiSportsEvent;
import com.bhanna.oddsapi.util.Calculator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class OutcomeResultTest {

    @Test
    public void testGetResult() {
        List<OddsApiSportsEvent.Outcome> outcomes = Arrays.asList(
                new OddsApiSportsEvent.Outcome("HomeOutcome", 1.5, null, null),
                new OddsApiSportsEvent.Outcome("AwayOutcome", 2.0, null, null)
        );

        MockedStatic<Calculator> calculatorMockedStatic = mockStatic(Calculator.class);

        calculatorMockedStatic.when(() -> Calculator.calculateImpliedProbability(anyDouble())).thenCallRealMethod();
        calculatorMockedStatic.when(() -> Calculator.calculateJuice(anyDouble(), anyDouble())).thenCallRealMethod();
        calculatorMockedStatic.when(() -> Calculator.calculateNoVigFairOddsProbability(anyDouble(), anyDouble())).thenCallRealMethod();
        calculatorMockedStatic.when(() -> Calculator.calculateOddsByProbability(anyDouble())).thenCallRealMethod();

        EdgeData edgeData = new EdgeData();
        edgeData.setHomeTeam("HomeOutcome");
        edgeData.setAwayTeam("AwayOutcome");
        OutcomeResult result = OutcomeResultMapper.fromBookmakerOutcome("bookmaker", outcomes, edgeData);

        calculatorMockedStatic.verify(() -> Calculator.calculateImpliedProbability(anyDouble()), times(2));
        calculatorMockedStatic.verify(() -> Calculator.calculateJuice(anyDouble(), anyDouble()), times(1));
        calculatorMockedStatic.verify(() -> Calculator.calculateNoVigFairOddsProbability(anyDouble(), anyDouble()), times(2));
        calculatorMockedStatic.verify(() -> Calculator.calculateOddsByProbability(anyDouble()), times(2));
        Assertions.assertNotNull(result);
    }

    @Test
    public void testGetOutcomeByName() {
        String expectedOutcomeName = "HomeOutcome";
        OddsApiSportsEvent.Outcome expectedOutcome = new OddsApiSportsEvent.Outcome(expectedOutcomeName, 1.5, null, null);
        List<OddsApiSportsEvent.Outcome> outcomes = Arrays.asList(
                new OddsApiSportsEvent.Outcome("AwayOutcome", 2.0, null, null),
                expectedOutcome
        );

        OddsApiSportsEvent.Outcome result = OutcomeResultMapper.getOutcomeByName(outcomes, "HomeOutcome");

        assertEquals(expectedOutcome, result);
    }

}
