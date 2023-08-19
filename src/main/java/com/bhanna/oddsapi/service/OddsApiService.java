package com.bhanna.oddsapi.service;

import com.bhanna.oddsapi.client.OddsApiClient;
import com.bhanna.oddsapi.model.EdgeData;
import com.bhanna.oddsapi.model.MarketKey;
import com.bhanna.oddsapi.model.OddsApi.OddsApiSport;
import com.bhanna.oddsapi.model.OddsApi.OddsApiSportsEvent;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
@Log4j2
public class OddsApiService {
    private final OddsApiClient oddsApiClient;
    private final ExpectedValueService expectedValueService;
    private final EdgeService edgeService;

    public OddsApiService(OddsApiClient oddsApiClient, ExpectedValueService expectedValueService, EdgeService edgeService) {
        this.oddsApiClient = oddsApiClient;
        this.expectedValueService = expectedValueService;
        this.edgeService = edgeService;
    }

    public Flux<OddsApiSportsEvent> getEventsForSport(String sportKey, List<String> bookmakers, List<MarketKey> markets) {
        return oddsApiClient.getOddsForSport(sportKey, bookmakers, markets, true);
    }

    public Flux<OddsApiSport> getSports() {
        return oddsApiClient.getSports();
//        return oddsApiClient.getSports().filter(Sport::getActive);
    }

    public Flux<EdgeData> getExpectedValueForSportsEvents(List<String> bookmakers, List<MarketKey> markets) {
        return getSports()
                .flatMap(oddsApiSport -> getEventsForSport(oddsApiSport.getKey(), bookmakers, markets))
                .flatMap(edgeService::getEdgeDataFromSportsEvent);
    }

}
