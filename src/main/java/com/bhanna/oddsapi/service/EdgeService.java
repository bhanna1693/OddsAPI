package com.bhanna.oddsapi.service;

import com.bhanna.oddsapi.model.EdgeData;
import com.bhanna.oddsapi.model.EdgeDataMapper;
import com.bhanna.oddsapi.model.OddsApi.OddsApiSportsEvent;
import com.bhanna.oddsapi.model.OutcomeResult;
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

            for (OddsApiSportsEvent.Bookmaker bookmaker : sportsEvent.getBookmakers()) {
                addEdgeDataToMap(sportsEvent, edgeDataMap, bookmaker);
            }
            log.info("EDGE DATA RETRIEVED FOR SPORTS EVENT: [sportsEvent:{}, eventId:{}]", sportsEvent.getSportTitle(), sportsEvent.getId());
            return Flux.fromIterable(edgeDataMap.values());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Flux.fromIterable(List.of());
        }
    }

    private static void addEdgeDataToMap(OddsApiSportsEvent sportsEvent, Map<String, EdgeData> edgeDataMap, OddsApiSportsEvent.Bookmaker bookmaker) {
        for (OddsApiSportsEvent.Market market : bookmaker.getMarkets()) {
            // get corresponding sharpest market
            // if it does not exist, skip by throwing
            OddsApiSportsEvent.Bookmaker sharpestBookmaker = sportsEvent.getBookmakers().stream()
                    .filter(bookmaker1 -> Objects.equals(bookmaker1.getKey(), SHARPEST_BOOKMAKER))
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException(
                            String.format("Sharpest bookmaker not found for this sport event: [sportEvent:%s, eventId:%s]", sportsEvent.getSportTitle(), sportsEvent.getId())
                    ));
            List<OddsApiSportsEvent.Outcome> sharpestOutcomes = sharpestBookmaker.getMarkets().stream()
                    .filter(market1 -> market1.getKey() == market.getKey())
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException(
                            String.format("Sharpest bookmaker does not contain outcomes for this sport event market: [sportEvent:%s, eventId:%s, market:%s]", sportsEvent.getSportTitle(), sportsEvent.getId(), market.getKey())
                    )).getOutcomes();

            String uniqueEdgeDataIdentifier = String.format("%s:%s:%s:%s", market.getKey(), bookmaker.getKey(), sportsEvent.getId(), sportsEvent.getCommenceTime());
            EdgeData edgeData = edgeDataMap.get(uniqueEdgeDataIdentifier);

            if (edgeData == null) {
                log.info("Creating edge data for market: [sportsEvent:{}, market:{}]", sportsEvent.getSportTitle(), market.getKey());
                edgeData = EdgeDataMapper.buildFromSportsEvent(sportsEvent);
                edgeData.setMarket(market.getKey());
                EdgeDataMapper.setSharpestInfo(sharpestBookmaker.getKey(), sharpestOutcomes, edgeData);
                edgeDataMap.put(uniqueEdgeDataIdentifier, edgeData);
            }

            switch (market.getKey()) {
                case h2h -> EdgeDataMapper.addOutcomeResult(bookmaker, market, edgeData);
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
            List<String> bestPriceAwayBooks = new ArrayList<>();
            String bestPriceHomeName = null;
            Double bestPriceHomeOdds = null;
            List<String> bestPriceHomeBooks = new ArrayList<>();

            for (OutcomeResult outcomeResult : edgeData.getOutcomeResults()) {
                if (outcomeResult.homeEdgePercent() > bestEdgePercentHome) {
                    bestEdgePercentHome = outcomeResult.homeEdgePercent();
                    bestPriceHomeOdds = outcomeResult.homePrice();
                    bestPriceHomeName = outcomeResult.homeName();
                    bestPriceHomeBooks.clear();
                    bestPriceHomeBooks.add(outcomeResult.bookmaker());
                } else if (outcomeResult.homeEdgePercent() == bestEdgePercentHome) {
                    bestPriceHomeBooks.add(outcomeResult.bookmaker());
                }
                if (outcomeResult.awayEdgePercent() > bestEdgePercentAway) {
                    bestEdgePercentAway = outcomeResult.awayEdgePercent();
                    bestPriceAwayOdds = outcomeResult.awayPrice();
                    bestPriceAwayName = outcomeResult.awayName();
                    bestPriceAwayBooks.clear();
                    bestPriceAwayBooks.add(outcomeResult.bookmaker());
                } else if (outcomeResult.awayEdgePercent() == bestEdgePercentAway) {
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
            return edgeData;
        });
    }
}
