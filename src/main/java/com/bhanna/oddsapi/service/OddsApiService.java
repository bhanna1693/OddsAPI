package com.bhanna.oddsapi.service;

import com.bhanna.oddsapi.client.OddsApiClient;
import com.bhanna.oddsapi.model.Sport;
import com.bhanna.oddsapi.model.SportsEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
public class OddsApiService {
    private final OddsApiClient oddsApiClient;
    private final ExpectedValueService expectedValueService;

    public OddsApiService(OddsApiClient oddsApiClient, ExpectedValueService expectedValueService) {
        this.oddsApiClient = oddsApiClient;
        this.expectedValueService = expectedValueService;
    }

    public Flux<SportsEvent> getEventsForSport(String sportKey, List<String> bookmakers, List<String> markets) {
        return oddsApiClient.getOddsForSport(sportKey, bookmakers, markets);

    }

    public Flux<Sport> getSports() {
        return oddsApiClient.getSports();
//        return oddsApiClient.getSports().filter(Sport::getActive);
    }

    public Flux<SportsEvent> getExpectedValueForSportsEvents(List<String> bookmakers, List<String> markets) {
        return getSports()
                .flatMap(sport -> getEventsForSport("baseball_mlb", bookmakers, markets))
                .map(expectedValueService::getExpectedValueForSportsEvent);
    }

}
