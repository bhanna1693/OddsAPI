package com.bhanna.oddsapi.service;

import com.bhanna.oddsapi.model.EdgeData;
import com.bhanna.oddsapi.model.EdgeDataMapper;
import com.bhanna.oddsapi.model.OddsApi.OddsApiSportsEvent;
import com.bhanna.oddsapi.model.OutcomeResult;
import com.bhanna.oddsapi.util.Calculator;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.*;

@Service
@Log4j2
public class EdgeService {

    private static final String SHARPEST_BOOKMAKER = "pinnacle";

    public Flux<EdgeData> getEdgeDataFromSportsEvent(OddsApiSportsEvent sportsEvent) {
        try {
            Map<String, EdgeData> edgeDataMap = new HashMap<>();

            addSharpestEdgeDataToMap(sportsEvent, edgeDataMap);

            for (OddsApiSportsEvent.Bookmaker bookmaker : sportsEvent.getBookmakers()) {
                addEdgeDataToMap(sportsEvent, edgeDataMap, bookmaker);
            }
            log.info("GET EDGE DATA FROM SPORTS EVENT: {}", edgeDataMap.values());
            return Flux.fromIterable(edgeDataMap.values());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Flux.fromIterable(List.of());
        }
    }

    public void addSharpestEdgeDataToMap(OddsApiSportsEvent sportsEvent, Map<String, EdgeData> edgeDataMap) {
        try {
            OddsApiSportsEvent.Bookmaker sharpestBookmaker = sportsEvent.getBookmakers().stream()
                    .filter(bookmaker -> {
                        log.info("BOOKMAKER IS: {}", bookmaker.getKey());
                        return bookmaker.getKey().equals(SHARPEST_BOOKMAKER);
                    })
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException("Sharpest bookmaker not found for sports event"));

            addEdgeDataToMap(sportsEvent, edgeDataMap, sharpestBookmaker);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private static void addEdgeDataToMap(OddsApiSportsEvent sportsEvent, Map<String, EdgeData> edgeDataMap, OddsApiSportsEvent.Bookmaker bookmaker) {
        for (OddsApiSportsEvent.Market market : bookmaker.getMarkets()) {
            String sharpestEdgeDataIdentifier = String.format("%s:%s:%s:%s", market.getKey(), SHARPEST_BOOKMAKER, sportsEvent.getId(), sportsEvent.getCommenceTime());
            String uniqueEdgeDataIdentifier = String.format("%s:%s:%s:%s", market.getKey(), bookmaker.getKey(), sportsEvent.getId(), sportsEvent.getCommenceTime());

            if (Objects.equals(sharpestEdgeDataIdentifier, uniqueEdgeDataIdentifier)) {
                // already processed
                return;
            }

            EdgeData sharpestEdgeData = edgeDataMap.get(sharpestEdgeDataIdentifier);
            EdgeData edgeData = edgeDataMap.get(uniqueEdgeDataIdentifier);


            if (edgeData == null) {
                edgeData = EdgeDataMapper.buildFromSportsEvent(sportsEvent);
                edgeData.setMarket(market.getKey());
                edgeDataMap.put(uniqueEdgeDataIdentifier, edgeData);
            }

            switch (market.getKey()) {
                case h2h -> EdgeDataMapper.addOutcomeResultToEdgeData(bookmaker, market, edgeData);
                default -> throw new IllegalStateException("Market not implemented: " + market.getKey());
            }
        }
    }


    public Flux<EdgeData> getBestPlays(Flux<EdgeData> edgeDataFlux) {
        return edgeDataFlux.map(edgeData -> {
            double bestEdgePercentHome = -999.9;
            double bestEdgePercentAway = -999.9;
            String bestPriceAwayName = null;
            Double bestPriceAwayOdds = null;
            List<String> bestPriceAwayBooks = List.of();
            String bestPriceHomeName = null;
            Double bestPriceHomeOdds = null;
            List<String> bestPriceHomeBooks = List.of();

            for (OutcomeResult outcomeResult : edgeData.getOutcomeResults()) {
                double edgePercentHome = Calculator.calculateEdge(outcomeResult.impliedProbabilityHome(), edgeData.getSharpestOutcomeResult().impliedProbabilityHome());
                double edgePercentAway = Calculator.calculateEdge(outcomeResult.impliedProbabilityAway(), edgeData.getSharpestOutcomeResult().impliedProbabilityAway());
                if (edgePercentHome > bestEdgePercentHome) {
                    bestEdgePercentHome = edgePercentHome;
                    bestPriceHomeOdds = outcomeResult.homePrice();
                    bestPriceHomeName = outcomeResult.homeName();
                    bestPriceHomeBooks.clear();
                    bestPriceHomeBooks.add(outcomeResult.bookmaker());
                } else if (edgePercentHome == bestEdgePercentHome) {
                    bestPriceHomeBooks.add(outcomeResult.bookmaker());
                }
                if (edgePercentAway > bestEdgePercentAway) {
                    bestEdgePercentAway = edgePercentAway;
                    bestPriceAwayOdds = outcomeResult.awayPrice();
                    bestPriceAwayName = outcomeResult.awayName();
                    bestPriceAwayBooks.clear();
                    bestPriceAwayBooks.add(outcomeResult.bookmaker());
                } else if (edgePercentAway == bestEdgePercentAway) {
                    bestPriceAwayBooks.add(outcomeResult.bookmaker());
                }
            }

            edgeData.setHomeEdgePercent(bestEdgePercentHome);
            edgeData.setAwayEdgePercent(bestEdgePercentAway);
            edgeData.setBestPriceAwayName(bestPriceAwayName);
            edgeData.setBestPriceAwayOdds(bestPriceAwayOdds);
            edgeData.setBestPriceAwayBooks(bestPriceAwayBooks);
            edgeData.setBestPriceHomeName(bestPriceHomeName);
            edgeData.setBestPriceHomeOdds(bestPriceHomeOdds);
            edgeData.setBestPriceHomeBooks(bestPriceHomeBooks);
            log.info("EDGE DATA: {}", edgeData);
            return edgeData;
        });
    }
}
