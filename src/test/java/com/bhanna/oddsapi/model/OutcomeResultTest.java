package com.bhanna.oddsapi.model;

import com.bhanna.oddsapi.util.Calculator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.mockito.Mockito.*;

public class OutcomeResultTest {

    @Test
    public void testGetResult() {
        MockedStatic<Calculator> calculatorMockedStatic = mockStatic(Calculator.class);

        calculatorMockedStatic.when(() -> Calculator.calculateImpliedProbability(anyDouble())).thenCallRealMethod();
        calculatorMockedStatic.when(() -> Calculator.calculateNoVigFairOddsProbability(anyDouble(), anyDouble())).thenCallRealMethod();
        calculatorMockedStatic.when(() -> Calculator.calculateOddsByProbability(anyDouble())).thenCallRealMethod();
        calculatorMockedStatic.when(() -> Calculator.calculateExpectedValuePercentage(anyDouble(), anyDouble())).thenCallRealMethod();

        OutcomeResult result = OutcomeResultMapper.generate("bookmaker", "homeName", "awayName", 1.0, 2.0);

        calculatorMockedStatic.verify(() -> Calculator.calculateImpliedProbability(anyDouble()), times(2));
        calculatorMockedStatic.verify(() -> Calculator.calculateNoVigFairOddsProbability(anyDouble(), anyDouble()), times(2));
        calculatorMockedStatic.verify(() -> Calculator.calculateOddsByProbability(anyDouble()), times(2));
        calculatorMockedStatic.verify(() -> Calculator.calculateExpectedValuePercentage(anyDouble(), anyDouble()), times(2));
        Assertions.assertNotNull(result);
    }

}
