package com.bhanna.oddsapi.service;

import com.bhanna.oddsapi.model.EdgeData;
import com.bhanna.oddsapi.model.OddsApi.OddsApiSportsEvent;
import com.bhanna.oddsapi.model.OutcomeResult;
import com.bhanna.oddsapi.model.OutcomeResultMapper;
import com.bhanna.oddsapi.util.Calculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class EdgeServiceTest {
    EdgeService edgeService = new EdgeService();
    List<OddsApiSportsEvent.Outcome> optimalOutcomes;
    List<OddsApiSportsEvent.Outcome> worstOutcomes;
    List<OddsApiSportsEvent.Outcome> mixedOutcomes;
    List<OddsApiSportsEvent.Outcome> sharpestOutcomes;
    OutcomeResult optimalOutcomeResult;
    OutcomeResult worstOutcomeResult;
    OutcomeResult mixedOutcomeResult;
    OutcomeResult sharpestOutcomeResult;
    List<OutcomeResult> outcomeResults;
    final String homeName = "Pittsburgh Steelers";
    final String awayName = "New England Patriots";

    @BeforeEach
    public void setup() {
        EdgeData edgeData = new EdgeData();
        edgeData.setHomeTeam(homeName);
        edgeData.setAwayTeam(awayName);

        sharpestOutcomes = List.of(
                new OddsApiSportsEvent.Outcome(homeName, 1.9, null, null),
                new OddsApiSportsEvent.Outcome(awayName, 1.9, null, null)
        );
        sharpestOutcomeResult = OutcomeResultMapper.fromBookmakerOutcome("pinnacle", sharpestOutcomes, edgeData);

        edgeData.setSharpestOutcomeResult(sharpestOutcomeResult);

        optimalOutcomes = List.of(
                new OddsApiSportsEvent.Outcome(homeName, 1.95, null, null),
                new OddsApiSportsEvent.Outcome(awayName, 1.95, null, null)
        );
        optimalOutcomeResult = OutcomeResultMapper.fromBookmakerOutcome("fanduel", optimalOutcomes, edgeData);

        worstOutcomes = List.of(
                new OddsApiSportsEvent.Outcome(homeName, 1.8, null, null),
                new OddsApiSportsEvent.Outcome(awayName, 1.8, null, null)
        );
        worstOutcomeResult = OutcomeResultMapper.fromBookmakerOutcome("draftkings", worstOutcomes, edgeData);

        mixedOutcomes = List.of(
                new OddsApiSportsEvent.Outcome(homeName, 1.95, null, null),
                new OddsApiSportsEvent.Outcome(awayName, 1.8, null, null)
        );
        mixedOutcomeResult = OutcomeResultMapper.fromBookmakerOutcome("barstool", mixedOutcomes, edgeData);

        outcomeResults = List.of(optimalOutcomeResult, worstOutcomeResult, mixedOutcomeResult);
    }

    @Test
    public void testGetBestPlays() {
        EdgeData edgeData = new EdgeData();
        edgeData.setSharpestOutcomeResult(sharpestOutcomeResult);
        edgeData.setOutcomeResults(outcomeResults);
        double edgePercentHome = Calculator.calculateExpectedValue(optimalOutcomeResult.impliedProbabilityHome(), edgeData.getSharpestOutcomeResult().noVigProbabilityHome());
        double edgePercentAway = Calculator.calculateExpectedValue(optimalOutcomeResult.impliedProbabilityAway(), edgeData.getSharpestOutcomeResult().noVigProbabilityAway());

        StepVerifier.create(edgeService.getBestPlays(Flux.just(edgeData)))
                .expectNextMatches(data -> {
                    System.out.println(data);

                    assertEquals(edgePercentHome, data.getHomeEdgePercent());
                    assertEquals(edgePercentAway, data.getAwayEdgePercent());
                    assertEquals(homeName, data.getBestPriceHomeName());
                    assertEquals(awayName, data.getBestPriceAwayName());
                    assertEquals(2.0, data.getBestPriceHomeOdds());
                    assertEquals(3.0, data.getBestPriceAwayOdds());
                    assertEquals(List.of("fanduel", "barstool"), data.getBestPriceHomeBooks());
                    assertEquals(List.of("fanduel"), data.getBestPriceAwayBooks());
                    return true;
                })
                .verifyComplete();
    }
}