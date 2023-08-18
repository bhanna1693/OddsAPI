package com.bhanna.oddsapi.service;

import com.bhanna.oddsapi.model.Bet;
import com.bhanna.oddsapi.model.SportsEvent;
import com.bhanna.oddsapi.repository.BetsRepository;
import reactor.core.publisher.Flux;

import java.util.List;

public class BetsService {
    private final BetsRepository betsRepository;
    private final OddsApiService oddsApiService;

    public BetsService(BetsRepository betsRepository, OddsApiService oddsApiService) {
        this.betsRepository = betsRepository;
        this.oddsApiService = oddsApiService;
    }

    public Flux<Bet> getPositiveEvBets() {
        return betsRepository.findAll();
    }

    public Flux<SportsEvent> getPositiveEvBets(List<String> bookmakers) {
        return oddsApiService.getEventsForAllSports(bookmakers);
    }
}
