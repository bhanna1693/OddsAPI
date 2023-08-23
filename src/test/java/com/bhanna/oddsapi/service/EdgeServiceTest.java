package com.bhanna.oddsapi.service;

import com.bhanna.oddsapi.model.EdgeData;
import com.bhanna.oddsapi.model.OddsApi.OddsApiSportsEvent;
import com.bhanna.oddsapi.model.OutcomeResult;
import com.bhanna.oddsapi.model.OutcomeResultMapper;
import com.bhanna.oddsapi.util.Calculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;

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
    EdgeData edgeData;

    @BeforeEach
    public void setup() {
        edgeData = new EdgeData();
        edgeData.setHomeTeam(homeName);
        edgeData.setAwayTeam(awayName);
    }

    public void setupSharpestBetterThanOptimal() {
        sharpestOutcomes = List.of(
                new OddsApiSportsEvent.Outcome(homeName, 2.5, null, null),
                new OddsApiSportsEvent.Outcome(awayName, 2.5, null, null)
        );
        sharpestOutcomeResult = OutcomeResultMapper.fromBookmakerOutcome("pinnacle", sharpestOutcomes, edgeData);
        edgeData.setSharpestOutcomeResult(sharpestOutcomeResult);

        optimalOutcomes = List.of(
                new OddsApiSportsEvent.Outcome(homeName, 2.7, null, null),
                new OddsApiSportsEvent.Outcome(awayName, 2.7, null, null)
        );
        optimalOutcomeResult = OutcomeResultMapper.fromBookmakerOutcome("fanduel", optimalOutcomes, edgeData);
        edgeData.setOutcomeResults(List.of(optimalOutcomeResult));
    }

    @Test
    public void testSharpestBetterThanOptimal() {
        setupSharpestBetterThanOptimal();
        double edgePercentHome = Calculator.calculateEdge(optimalOutcomeResult.impliedProbabilityHome(), edgeData.getSharpestOutcomeResult().noVigProbabilityHome());
        double edgePercentAway = Calculator.calculateEdge(optimalOutcomeResult.impliedProbabilityAway(), edgeData.getSharpestOutcomeResult().noVigProbabilityAway());

        StepVerifier.create(edgeService.getBestPlays(Flux.just(edgeData)))
                .expectNextMatches(data -> {
                    assertTrue(edgePercentHome > 0);
                    assertTrue(edgePercentAway > 0);
                    assertEquals(edgePercentHome, data.getHomeEdgePercent());
                    assertEquals(edgePercentAway, data.getAwayEdgePercent());
                    assertEquals(homeName, data.getBestPriceHomeName());
                    assertEquals(awayName, data.getBestPriceAwayName());
                    assertEquals(2.7, data.getBestPriceHomeOdds());
                    assertEquals(2.7, data.getBestPriceAwayOdds());
                    assertEquals(List.of("fanduel"), data.getBestPriceHomeBooks());
                    assertEquals(List.of("fanduel"), data.getBestPriceAwayBooks());
                    return true;
                })
                .verifyComplete();
    }

    @Test
    public void testMixedOutcomesFavoringHome() {
        setupMixedOutcomesFavoringHome();
        double edgePercentHome = Calculator.calculateEdge(mixedOutcomeResult.impliedProbabilityHome(), edgeData.getSharpestOutcomeResult().noVigProbabilityHome());
        double edgePercentAway = Calculator.calculateEdge(mixedOutcomeResult.impliedProbabilityAway(), edgeData.getSharpestOutcomeResult().noVigProbabilityAway());

        StepVerifier.create(edgeService.getBestPlays(Flux.just(edgeData)))
                .expectNextMatches(data -> {
                    assertTrue(edgePercentHome > 0);
                    assertTrue(edgePercentAway < 0);
                    assertEquals(edgePercentHome, data.getHomeEdgePercent());
                    assertEquals(edgePercentAway, data.getAwayEdgePercent());
                    assertEquals(homeName, data.getBestPriceHomeName());
                    assertEquals(awayName, data.getBestPriceAwayName());
                    assertEquals(2.2, data.getBestPriceHomeOdds());
                    assertEquals(1.8, data.getBestPriceAwayOdds());
                    assertEquals(List.of("draftkings"), data.getBestPriceHomeBooks());
                    assertEquals(List.of("draftkings"), data.getBestPriceAwayBooks());
                    return true;
                })
                .verifyComplete();
    }

    private void setupMixedOutcomesFavoringHome() {
        sharpestOutcomes = List.of(
                new OddsApiSportsEvent.Outcome(homeName, 1.8, null, null),
                new OddsApiSportsEvent.Outcome(awayName, 2.0, null, null)
        );

        sharpestOutcomeResult = OutcomeResultMapper.fromBookmakerOutcome("pinnacle", sharpestOutcomes, edgeData);
        edgeData.setSharpestOutcomeResult(sharpestOutcomeResult);

        mixedOutcomes = List.of(
                new OddsApiSportsEvent.Outcome(homeName, 2.2, null, null),
                new OddsApiSportsEvent.Outcome(awayName, 1.8, null, null)
        );
        mixedOutcomeResult = OutcomeResultMapper.fromBookmakerOutcome("draftkings", mixedOutcomes, edgeData);
        edgeData.setOutcomeResults(List.of(mixedOutcomeResult));
    }
    @Test
    public void testSharpestAndWorstInDifferentDirections() {
        setupSharpestAndWorstInDifferentDirections();
        double edgePercentHome = Calculator.calculateEdge(worstOutcomeResult.impliedProbabilityHome(), edgeData.getSharpestOutcomeResult().noVigProbabilityHome());
        double edgePercentAway = Calculator.calculateEdge(worstOutcomeResult.impliedProbabilityAway(), edgeData.getSharpestOutcomeResult().noVigProbabilityAway());

        StepVerifier.create(edgeService.getBestPlays(Flux.just(edgeData)))
                .expectNextMatches(data -> {
                    assertTrue(edgePercentHome < 0);
                    assertTrue(edgePercentAway < 0);
                    assertEquals(edgePercentHome, data.getHomeEdgePercent());
                    assertEquals(edgePercentAway, data.getAwayEdgePercent());
                    assertEquals(homeName, data.getBestPriceHomeName());
                    assertEquals(awayName, data.getBestPriceAwayName());
                    assertEquals(2.1, data.getBestPriceHomeOdds());
                    assertEquals(2.3, data.getBestPriceAwayOdds());
                    assertEquals(List.of("barstool"), data.getBestPriceHomeBooks());
                    assertEquals(List.of("barstool"), data.getBestPriceAwayBooks());
                    return true;
                })
                .verifyComplete();
    }

    private void setupSharpestAndWorstInDifferentDirections() {
        sharpestOutcomes = List.of(
                new OddsApiSportsEvent.Outcome(homeName, 2.4, null, null),
                new OddsApiSportsEvent.Outcome(awayName, 2.4, null, null)
        );
        sharpestOutcomeResult = OutcomeResultMapper.fromBookmakerOutcome("pinnacle", sharpestOutcomes, edgeData);
        edgeData.setSharpestOutcomeResult(sharpestOutcomeResult);
        worstOutcomes = List.of(
                new OddsApiSportsEvent.Outcome(homeName, 2.1, null, null),
                new OddsApiSportsEvent.Outcome(awayName, 2.3, null, null)
        );
        worstOutcomeResult = OutcomeResultMapper.fromBookmakerOutcome("barstool", worstOutcomes, edgeData);
        edgeData.setOutcomeResults(List.of(worstOutcomeResult));
    }
}