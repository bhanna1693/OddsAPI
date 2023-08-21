package com.bhanna.oddsapi.model;

import com.bhanna.oddsapi.model.OddsApi.OddsApiSportsEvent;
import com.bhanna.oddsapi.util.Calculator;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@Log4j2
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

        OutcomeResult result = EdgeDataMapper.buildOutcomeResult("bookmaker", outcomes, "HomeOutcome", "AwayOutcome");

        calculatorMockedStatic.verify(() -> Calculator.calculateImpliedProbability(anyDouble()), times(2));
        calculatorMockedStatic.verify(() -> Calculator.calculateJuice(anyDouble(), anyDouble()), times(1));
        calculatorMockedStatic.verify(() -> Calculator.calculateNoVigFairOddsProbability(anyDouble(), anyDouble()), times(2));
        calculatorMockedStatic.verify(() -> Calculator.calculateOddsByProbability(anyDouble()), times(2));
        Assertions.assertNotNull(result);
        log.info(result);
    }
}
