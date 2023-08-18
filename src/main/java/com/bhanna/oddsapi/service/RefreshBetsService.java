package com.bhanna.oddsapi.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RefreshBetsService {

    @Value("odds-api.bookmakers")
    private List<String> defaultBookmakers;

    @Value("odds-api.markets")
    private List<String> defaultMarkets;
    private final OddsApiService oddsApiService;

    public RefreshBetsService(OddsApiService oddsApiService) {
        this.oddsApiService = oddsApiService;
    }

    @Scheduled(cron = "0 0 * * * *")
    public void refreshPositiveEvBets() {
        oddsApiService.getExpectedValueForSportsEvents(defaultBookmakers, defaultMarkets);
    }
}
