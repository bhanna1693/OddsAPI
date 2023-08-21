package com.bhanna.oddsapi.service;

import com.bhanna.oddsapi.model.OddsApi.OddsApiSportsEvent;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@Log4j2
public class ExpectedValueService {

    // the sharpest book
//    also consider "Circa"
    private static final String sharpestBook = "pinnacle";

    public OddsApiSportsEvent getExpectedValueForSportsEvent(OddsApiSportsEvent oddsApiSportsEvent) {
        Optional<OddsApiSportsEvent.Bookmaker> sharpestBookmaker = getSharpestBookmaker(oddsApiSportsEvent);

        if (sharpestBookmaker.isPresent()) {
            oddsApiSportsEvent.getBookmakers().stream()
                    .filter(b -> !Objects.equals(b.getKey(), sharpestBook))
                    .forEach(bookmaker -> processBookmaker(bookmaker, sharpestBookmaker.get()));
        } else {
            // TODO: IMPLEMENT
            log.warn("SHARPEST BOOK NOT FOUND. FIGURE OUT WHAT TO DO HERE");
        }

        return oddsApiSportsEvent;
    }


    private Optional<OddsApiSportsEvent.Bookmaker> getSharpestBookmaker(OddsApiSportsEvent oddsApiSportsEvent) {
        return oddsApiSportsEvent.getBookmakers().stream()
                .filter(bookmaker -> bookmaker.getKey().equals(sharpestBook))
                .findFirst();
    }

    private void processBookmaker(OddsApiSportsEvent.Bookmaker bookmaker, OddsApiSportsEvent.Bookmaker sharpestBookmaker) {
        bookmaker.getMarkets().forEach(market -> {
            getMarketMatch(market, sharpestBookmaker);
        });
    }

    private static Optional<OddsApiSportsEvent.Market> getMarketMatch(OddsApiSportsEvent.Market market, OddsApiSportsEvent.Bookmaker sharpestBookmaker) {
        return sharpestBookmaker.getMarkets().stream().filter(sharpestBookmakerMarket -> sharpestBookmakerMarket.getKey() == market.getKey()).findFirst();
    }

    private void processMarket(OddsApiSportsEvent.Market market, OddsApiSportsEvent.Market sharpestBookmakerMarket) {

    }

    private void processH2HMarket(OddsApiSportsEvent.Market market, OddsApiSportsEvent.Market sharpestBookmakerMarket) {
        OddsApiSportsEvent.Outcome team1 = market.getOutcomes().get(0);
        OddsApiSportsEvent.Outcome team2 = market.getOutcomes().get(1);
        market.getOutcomes().forEach(outcome -> {
            getSharpestBookmakerOutcomeMatch(sharpestBookmakerMarket, outcome).ifPresentOrElse(sharpestBookmakerOutcome -> {
                // Process with sharpestBookmaker outcome
//                outcome.setExpectedValue(getExpectedValueForOutcome(outcome.getPrice(), sharpestBookmakerOutcome.getPrice()));
//                outcome.setMarketWidth(calculateMarketWidth(outcome.getPrice(), sharpestBookmakerOutcome.getPrice()));
            }, () -> {
                // Process without sharpestBookmaker outcome
//                outcome.setExpectedValue(getExpectedValueForOutcome(outcome.getPrice()));
            });
        });
    }

    public static Optional<OddsApiSportsEvent.Outcome> getSharpestBookmakerOutcomeMatch(OddsApiSportsEvent.Market sharpestBookmakerMarket, OddsApiSportsEvent.Outcome outcome) {
        return sharpestBookmakerMarket.getOutcomes().stream().filter(sharpestBookmakerOutcome -> sharpestBookmakerOutcome.getName() == outcome.getName()).findFirst();
    }

}
