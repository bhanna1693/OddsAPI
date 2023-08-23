package com.bhanna.oddsapi.service;

import com.bhanna.oddsapi.model.EdgeData;
import com.bhanna.oddsapi.model.MarketKey;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
public class BetsService {
    private final OddsApiClientService oddsApiClientService;

    public BetsService(OddsApiClientService oddsApiClientService) {
        this.oddsApiClientService = oddsApiClientService;
    }

    public Flux<EdgeData> getExpectedValueBets(List<String> bookmakers, List<MarketKey> markets) {
        return oddsApiClientService.getExpectedValueForSportsEvents(bookmakers, markets);
    }
}
