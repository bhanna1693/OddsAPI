package com.bhanna.oddsapi.service;

import com.bhanna.oddsapi.model.Outcome;
import com.bhanna.oddsapi.model.SportsEvent;
import org.springframework.stereotype.Service;

@Service
public class ExpectedValueService {

//    EV = (Probability of Winning * Potential Profit if Win) - (Probability of Losing * Amount Wagered)

    public SportsEvent getExpectedValueForSportsEvent(SportsEvent sportsEvent) {
        sportsEvent.getBookmakers().forEach(bookmaker ->
                bookmaker.getMarkets().forEach(market -> {
                    switch (market.getKey()) {
                        case "spread", "total" -> market.getOutcomes().forEach(outcome -> {
                            outcome.setExpectedValue(getExpectedValueForSpreadOrTotal(outcome));
                        });
                        case "outright", "h2h" -> market.getOutcomes().forEach(outcome -> {
                            outcome.setExpectedValue(getExpectedValueForOutrightOrH2H(outcome));
                        });
                    }
                })
        );

        return sportsEvent;
    }

    private Double getExpectedValueForOutrightOrH2H(Outcome outcome) {
        return null;
//        Double amountWagered = 100.0;
//        Double probabilityOfWinning = 1.0 / outcome.getPrice();
//        Double potentialProfit = probabilityOfWinning * amountWagered;
//        Double probabilityOfLosing = 1.0 - probabilityOfWinning;
//
//        return calculateExpectedValue(probabilityOfWinning, potentialProfit, probabilityOfLosing, amountWagered);
    }

    public Double getExpectedValueForSpreadOrTotal(Outcome outcome) {
        Double amountWagered = 100.0;
        Double probabilityOfWinning = 1.0 / outcome.getPrice();
        Double potentialProfit = probabilityOfWinning * amountWagered;
        Double probabilityOfLosing = 1.0 - probabilityOfWinning;

        return calculateExpectedValue(probabilityOfWinning, potentialProfit, probabilityOfLosing, amountWagered);
    }

    private Double calculateExpectedValue(Double probabilityOfWinning, Double potentialProfit, Double probabilityOfLosing, Double amountWagered) {
        return (probabilityOfWinning * potentialProfit) - (probabilityOfLosing * amountWagered);
    }
}
