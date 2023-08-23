package com.bhanna.oddsapi.service;

import com.bhanna.oddsapi.model.EdgeData;
import com.bhanna.oddsapi.model.OutcomeResult;
import com.bhanna.oddsapi.model.OutcomeResultMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EdgeServiceTest {
    private EdgeService edgeService;
    final String homeName = "Pittsburgh Steelers";
    final String awayName = "New England Patriots";
    EdgeData edgeData;
    OutcomeResult sharpestOutcomeResult;
    OutcomeResult worstOutcomeResult;
    OutcomeResult mixedOutcomeResult;
    OutcomeResult optimalOutcomeResult;

    @BeforeEach
    public void setup() {
        edgeService = new EdgeService();
        edgeData = new EdgeData();
        edgeData.setHomeTeam(homeName);
        edgeData.setAwayTeam(awayName);
        sharpestOutcomeResult = OutcomeResultMapper.generate(
                "pinnacle",
                homeName,
                awayName,
                5.0,
                1.0
        );
        edgeData.setSharpestOutcomeResult(sharpestOutcomeResult);

        optimalOutcomeResult = OutcomeResultMapper.generate("fanduel", homeName, awayName, 5.1, 1.1, edgeData.getSharpestOutcomeResult());
        mixedOutcomeResult = OutcomeResultMapper.generate("draftkings", homeName, awayName, 5.1, 0.9, edgeData.getSharpestOutcomeResult());
        worstOutcomeResult = OutcomeResultMapper.generate("barstool", homeName, awayName, 4.9, 0.9, edgeData.getSharpestOutcomeResult());

        edgeData.setOutcomeResults(List.of(worstOutcomeResult, mixedOutcomeResult, optimalOutcomeResult));
    }

    @Test
    public void testExpectedValues() {

        StepVerifier.create(edgeService.getBestPlays(Flux.just(edgeData)))
                .expectNextMatches(data -> {
                    assertTrue(mixedOutcomeResult.homeEdgePercent() > 0);
                    assertTrue(optimalOutcomeResult.awayEdgePercent() > 0);
                    assertEquals(mixedOutcomeResult.homeEdgePercent(), data.getHomeEdgePercent());
                    assertEquals(optimalOutcomeResult.awayEdgePercent(), data.getAwayEdgePercent());
                    return true;
                })
                .verifyComplete();
    }

    @Test
    public void testNames() {

        StepVerifier.create(edgeService.getBestPlays(Flux.just(edgeData)))
                .expectNextMatches(data -> {
                    assertEquals(homeName, data.getBestPriceHomeName());
                    assertEquals(awayName, data.getBestPriceAwayName());
                    assertEquals(mixedOutcomeResult.homePrice(), data.getBestPriceHomeOdds());
                    assertEquals(optimalOutcomeResult.awayPrice(), data.getBestPriceAwayOdds());
                    return true;
                })
                .verifyComplete();
    }

    @Test
    public void testOdds() {

        StepVerifier.create(edgeService.getBestPlays(Flux.just(edgeData)))
                .expectNextMatches(data -> {
                    assertEquals(mixedOutcomeResult.homePrice(), data.getBestPriceHomeOdds());
                    assertEquals(optimalOutcomeResult.awayPrice(), data.getBestPriceAwayOdds());
                    return true;
                })
                .verifyComplete();
    }

    @Test
    public void testBooks() {

        StepVerifier.create(edgeService.getBestPlays(Flux.just(edgeData)))
                .expectNextMatches(data -> {
                    assertTrue(data.getBestPriceHomeBooks().containsAll(List.of("fanduel", "draftkings")));
                    assertFalse(data.getBestPriceHomeBooks().contains("barstool"));
                    assertTrue(data.getBestPriceAwayBooks().contains("fanduel"));
                    assertFalse(data.getBestPriceAwayBooks().contains("draftkings"));
                    assertFalse(data.getBestPriceAwayBooks().contains("barstool"));
                    return true;
                })
                .verifyComplete();
    }

}