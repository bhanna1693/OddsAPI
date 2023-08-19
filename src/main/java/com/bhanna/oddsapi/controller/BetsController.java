package com.bhanna.oddsapi.controller;

import com.bhanna.oddsapi.model.MarketKey;
import com.bhanna.oddsapi.model.OddsApi.OddsApiSportsEvent;
import com.bhanna.oddsapi.service.BetsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bets")
public class BetsController {

    private final BetsService betsService;

    public BetsController(BetsService betsService) {
        this.betsService = betsService;
    }

    @GetMapping("")
    public Flux<OddsApiSportsEvent> getPositiveEvBets(@RequestParam List<String> bookmakers, @RequestParam List<MarketKey> markets) {
        return betsService.getExpectedValueBets(bookmakers, markets);
    }
}
