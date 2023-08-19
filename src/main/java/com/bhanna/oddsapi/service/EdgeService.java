package com.bhanna.oddsapi.service;

import com.bhanna.oddsapi.model.EdgeData;
import com.bhanna.oddsapi.model.EdgeDataMapper;
import com.bhanna.oddsapi.model.MarketKey;
import com.bhanna.oddsapi.model.OddsApi.OddsApiSportsEvent;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Log4j2
public class EdgeService {
    // the sharpest book
    // also consider "Circa"
    private static final String sharpestBook = "pinnacle";

    public Flux<EdgeData> getEdgeDataFromSportsEvent(OddsApiSportsEvent sportsEvent) {
        try {
            Map<String, EdgeData> edgeDataMap = new HashMap<>();

            for (OddsApiSportsEvent.Bookmaker bookmaker : sportsEvent.getBookmakers()) {
                for (OddsApiSportsEvent.Market market : bookmaker.getMarkets()) {
                    MarketKey marketKey = MarketKey.valueOf(market.getKey());
                    String uniqueEdgeDataIdentifier = String.format("%s:%s:%s:%s", marketKey, bookmaker.getKey(), sportsEvent.getId(), sportsEvent.getCommenceTime());

                    EdgeData edgeData = edgeDataMap.get(uniqueEdgeDataIdentifier);

                    if (edgeData == null) {
                        edgeData = EdgeDataMapper.buildFromSportsEvent(sportsEvent);
                        edgeData.setMarket(MarketKey.valueOf(market.getKey()));
                        edgeDataMap.put(uniqueEdgeDataIdentifier, edgeData);
                    }

                    switch (marketKey) {
                        case H2H -> {
                            EdgeDataMapper.extracted(market, edgeData);
                        }
                        default -> throw new IllegalStateException("Market not implemented: " + marketKey);
                    }
                }
            }

            return Flux.fromIterable(edgeDataMap.values());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Flux.fromIterable(List.of());
        }
    }


}
