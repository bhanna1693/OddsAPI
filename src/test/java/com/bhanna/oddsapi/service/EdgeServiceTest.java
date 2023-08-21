package com.bhanna.oddsapi.service;

import com.bhanna.oddsapi.model.EdgeData;
import com.bhanna.oddsapi.model.OutcomeResult;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EdgeServiceTest {
    EdgeService edgeService = new EdgeService();

    @Test
    public void testGetBestPlays() {
        OutcomeResult sharpestOutcomeResult = new OutcomeResult();
        List<OutcomeResult> outcomeResults = List.of(
                new OutcomeResult()
        );
        EdgeData edgeData = new EdgeData();
        edgeData.setSharpestOutcomeResult();
        edgeData.setOutcomeResults(outcomeResults);
        Flux<EdgeData> edgeDataFlux = Flux.fromIterable(List.of(

        ));
        edgeService.getBestPlays(edgeDataFlux).subscribe(data -> {
            assertTrue(data.getHomeEdgePercent() > data.getAwayEdgePercent());
            assertTrue(Objects.equals(data.getBestPriceHomeName(), "Home team"));
            assertTrue(data.getBestPriceHomeOdds() == 150.0);
            assertTrue(Objects.equals(data.getBestPriceHomeBooks(), List.of("fanduel")));
        });
    }
}