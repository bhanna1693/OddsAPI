package com.bhanna.oddsapi.model;

import com.bhanna.oddsapi.model.OddsApi.OddsApiSportsEvent;
import com.bhanna.oddsapi.util.Calculator;

import java.util.List;
import java.util.Objects;

public class OutcomeResultMapper {
    public static OutcomeResult fromBookmakerOutcome(String bookmaker, List<OddsApiSportsEvent.Outcome> outcomes, EdgeData edgeData) {
        Double homePrice = getOutcomeByName(outcomes, edgeData.getHomeTeam()).getPrice();
        Double awayPrice = getOutcomeByName(outcomes, edgeData.getAwayTeam()).getPrice();
        double impliedProbabilityHome = Calculator.calculateImpliedProbability(homePrice);
        double impliedProbabilityAway = Calculator.calculateImpliedProbability(awayPrice);
        double juice = Calculator.calculateJuice(impliedProbabilityHome, impliedProbabilityAway);
        double noVigProbabilityHome = Calculator.calculateNoVigFairOddsProbability(impliedProbabilityHome, impliedProbabilityAway);
        double noVigProbabilityAway = Calculator.calculateNoVigFairOddsProbability(impliedProbabilityAway, impliedProbabilityHome);
        double noVigOddsHome = Calculator.calculateOddsByProbability(noVigProbabilityHome);
        double noVigOddsAway = Calculator.calculateOddsByProbability(noVigProbabilityAway);
        double homeEdgePercent;
        double awayEdgePercent;

        if (Objects.equals(bookmaker, "pinnacle")) {
            homeEdgePercent = Calculator.calculateExpectedValue(homePrice, noVigProbabilityHome);
            awayEdgePercent = Calculator.calculateExpectedValue(awayPrice, noVigProbabilityAway);
        } else {
            homeEdgePercent = Calculator.calculateExpectedValue(homePrice, edgeData.getSharpestOutcomeResult().noVigProbabilityHome());
            awayEdgePercent = Calculator.calculateExpectedValue(awayPrice, edgeData.getSharpestOutcomeResult().noVigProbabilityAway());
        }

        return new OutcomeResult(
                bookmaker,
                edgeData.getHomeTeam(),
                edgeData.getAwayTeam(),
                homePrice,
                awayPrice,
                impliedProbabilityHome,
                impliedProbabilityAway,
                noVigProbabilityHome,
                noVigProbabilityAway,
                noVigOddsHome,
                noVigOddsAway,
                juice,
                homeEdgePercent,
                awayEdgePercent
        );
    }

    public static OddsApiSportsEvent.Outcome getOutcomeByName(List<OddsApiSportsEvent.Outcome> outcomes, String outcomeName) {
        return outcomes.stream()
                .filter(outcome -> Objects.equals(outcome.getName(), outcomeName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(
                        String.format("Outcome not found for name: [name:%s]", outcomeName)
                ));
    }
}
