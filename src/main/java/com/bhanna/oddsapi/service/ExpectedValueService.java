package com.bhanna.oddsapi.service;

import com.bhanna.oddsapi.model.Outcome;
import com.bhanna.oddsapi.model.SportsEvent;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpectedValueService {

    private static List<String> sharpestBooks = List.of("pinnacle");
//    get odds from pinnacle and calculate ev using their lines

    public SportsEvent getExpectedValueForSportsEvent(SportsEvent sportsEvent) {
        sportsEvent.getBookmakers().forEach(bookmaker ->
                bookmaker.getMarkets().forEach(market -> {
                    switch (market.getKey()) {
                        case "h2h" -> market.getOutcomes().forEach(outcome -> {
                            double kellyBetSize = calculateKellyPercentOfBankRoll(outcome);
                            outcome.setExpectedValue(getExpectedValueForOutcome(outcome));
                        });
                        case "spread", "total" -> market.getOutcomes().forEach(outcome -> {
                            outcome.setExpectedValue(getExpectedValueForOutcome(outcome));
                        });
                        case "outright" -> market.getOutcomes().forEach(outcome -> {
                            outcome.setExpectedValue(getExpectedValueForOutcome(outcome));
                        });
                        default -> throw new IllegalStateException("Unexpected value: " + market.getKey());
                    }
                })
        );

        return sportsEvent;
    }

    /**
     * f* = (bp - q) / b
     * <p>
     * f* is the fraction of the bankroll to bet.
     * b is the odds received on the bet (decimal odds, not fractional).
     * p is the probability of winning.
     * q is the probability of losing, which is 1 - p.
     */
    private double calculateKellyPercentOfBankRoll(Outcome outcome) {
        double probabilityOfWinning = 1 - calculateProbabilityOfWinning(outcome.getPrice());
        double probabilityOfLosing = 1 - probabilityOfWinning;
        return (outcome.getPrice() * probabilityOfWinning - probabilityOfLosing) / outcome.getPrice();
    }

    private double calculateProbabilityOfWinning(Double odds) {
        return 1.0 / odds;
    }

    private double calculateProbabilityOfLosing(Double probabilityOfWinning) {
        return 1.0 - probabilityOfWinning;
    }


    private Double getExpectedValueForOutcome(Outcome outcome) {
        Double amountWagered = 100.0;
        Double probabilityOfWinning = calculateProbabilityOfWinning(outcome.getPrice());
        Double potentialProfit = probabilityOfWinning * amountWagered;
        Double probabilityOfLosing = calculateProbabilityOfLosing(probabilityOfWinning);

        return calculateExpectedValue(probabilityOfWinning, potentialProfit, probabilityOfLosing, amountWagered);
    }

    public Double getExpectedValueForSpreadOrTotal(Outcome outcome) {
        Double amountWagered = 100.0;
        Double probabilityOfWinning = 1.0 / outcome.getPrice();
        Double potentialProfit = probabilityOfWinning * amountWagered;
        Double probabilityOfLosing = 1.0 - probabilityOfWinning;

        return calculateExpectedValue(probabilityOfWinning, potentialProfit, probabilityOfLosing, amountWagered);
    }


    /**
     * EV = (Pw * Pp) − (Pl * A)
     * <p>
     * Pw - Probability of winning
     * Pp - Potential profit
     * Pl - Potential of losing
     * A  - Amount wagered
     */
    private Double calculateExpectedValue(Double probabilityOfWinning, Double potentialProfit, Double probabilityOfLosing, Double amountWagered) {
        return (probabilityOfWinning * potentialProfit) - (probabilityOfLosing * amountWagered);
    }
}
