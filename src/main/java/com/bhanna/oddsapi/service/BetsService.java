package com.bhanna.oddsapi.service;

import com.bhanna.oddsapi.model.Bet;
import com.bhanna.oddsapi.model.SportsEvent;
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

    public Flux<Bet> getSavedEvBets() {
        return betsRepository.findAll();
    }

    public Flux<SportsEvent> getExpectedValueBets(List<String> bookmakers, List<String> markets) {
        return oddsApiService.getExpectedValueForSportsEvents(bookmakers, markets);
    }
}
