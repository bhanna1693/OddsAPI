package com.bhanna.oddsapi.service;

import com.bhanna.oddsapi.model.EdgeData;
import com.bhanna.oddsapi.model.MarketKey;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
public class BetsService {
    private final OddsApiService oddsApiService;

    public BetsService(OddsApiService oddsApiService) {
        this.oddsApiService = oddsApiService;
    }

    public Flux<EdgeData> getExpectedValueBets(List<String> bookmakers, List<MarketKey> markets) {
        return oddsApiService.getExpectedValueForSportsEvents(bookmakers, markets);
    }
}
