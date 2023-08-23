package com.bhanna.oddsapi.service;

import com.bhanna.oddsapi.model.MarketKey;
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
    private final OddsApiClientService oddsApiClientService;

    public RefreshBetsService(OddsApiClientService oddsApiClientService) {
        this.oddsApiClientService = oddsApiClientService;
    }

    @Scheduled(cron = "0 0 * * * *")
    public void refreshPositiveEvBets() {
        List<MarketKey> markets = defaultMarkets.stream().map(MarketKey::valueOf).toList();

        oddsApiClientService.getExpectedValueForSportsEvents(defaultBookmakers, markets);
    }
}
