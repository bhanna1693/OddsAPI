package com.bhanna.oddsapi.service;

import com.bhanna.oddsapi.client.OddsApiClient;
import com.bhanna.oddsapi.model.EdgeData;
import com.bhanna.oddsapi.model.MarketKey;
import com.bhanna.oddsapi.model.OddsApi.OddsApiSport;
import com.bhanna.oddsapi.model.OddsApi.OddsApiSportsEvent;
import com.bhanna.oddsapi.model.SportKey;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
@Log4j2
public class OddsApiClientService {
    private final OddsApiClient oddsApiClient;
    private final EdgeService edgeService;

    public OddsApiClientService(OddsApiClient oddsApiClient, EdgeService edgeService) {
        this.oddsApiClient = oddsApiClient;
        this.edgeService = edgeService;
    }

    public Flux<OddsApiSportsEvent> getEventsForSport(SportKey sportKey, List<String> bookmakers, List<MarketKey> markets) {
        return oddsApiClient.getOddsForSport(sportKey, bookmakers, markets, true);
    }

    public Flux<OddsApiSport> getSports() {
        return oddsApiClient.getSports();
//        return oddsApiClient.getSports().filter(Sport::getActive);
    }

    public Flux<EdgeData> getExpectedValueForSportsEvents(List<String> bookmakers, List<MarketKey> markets) {
        Flux<EdgeData> edgeData = getSports()
                .flatMap(oddsApiSport -> getEventsForSport(oddsApiSport.getKey(), bookmakers, markets))
                .flatMap(edgeService::getEdgeDataFromSportsEvent);

        return edgeService.getBestPlays(edgeData);
    }

}
