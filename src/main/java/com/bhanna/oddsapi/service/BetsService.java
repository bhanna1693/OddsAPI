package com.bhanna.oddsapi.service;

import com.bhanna.oddsapi.model.MarketKey;
import com.bhanna.oddsapi.model.OddsApi.OddsApiSportsEvent;
import com.bhanna.oddsapi.repository.BetsRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
public class BetsService {
    private final BetsRepository betsRepository;
    private final OddsApiService oddsApiService;

    public BetsService(BetsRepository betsRepository, OddsApiService oddsApiService) {
        this.betsRepository = betsRepository;
        this.oddsApiService = oddsApiService;
    }

    public Flux<OddsApiSportsEvent> getExpectedValueBets(List<String> bookmakers, List<MarketKey> markets) {
        return oddsApiService.getExpectedValueForSportsEvents(bookmakers, markets);
    }
}
