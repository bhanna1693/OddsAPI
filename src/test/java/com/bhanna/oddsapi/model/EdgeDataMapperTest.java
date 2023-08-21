package com.bhanna.oddsapi.model;

import com.bhanna.oddsapi.model.OddsApi.OddsApiSportsEvent;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EdgeDataMapperTest {
    @Test
    public void testGetOutcomeByName() {
        String expectedOutcomeName = "HomeOutcome";
        OddsApiSportsEvent.Outcome expectedOutcome = new OddsApiSportsEvent.Outcome(expectedOutcomeName, 1.5, null, null);
        List<OddsApiSportsEvent.Outcome> outcomes = Arrays.asList(
                new OddsApiSportsEvent.Outcome("AwayOutcome", 2.0, null, null),
                expectedOutcome
        );

        OddsApiSportsEvent.Outcome result = EdgeDataMapper.getOutcomeByName(outcomes, "HomeOutcome");

        assertEquals(expectedOutcome, result);
    }
}
