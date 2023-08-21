package com.bhanna.oddsapi.model;

import com.bhanna.oddsapi.model.OddsApi.OddsApiSportsEvent;
import com.bhanna.oddsapi.util.Calculator;

import java.util.List;
import java.util.Objects;

public class EdgeDataMapper {

    public static EdgeData buildFromSportsEvent(OddsApiSportsEvent sportsEvent) {
        EdgeData edgeData = new EdgeData();
        edgeData.setEventId(sportsEvent.getId());
        edgeData.setSport(sportsEvent.getSportKey());
        edgeData.setSportTitle(sportsEvent.getSportTitle());
        edgeData.setHomeTeam(sportsEvent.getHomeTeam());
        edgeData.setAwayTeam(sportsEvent.getAwayTeam());
        edgeData.setStartTime(sportsEvent.getCommenceTime());
        return edgeData;
    }

    public static void addOutcomeResultToEdgeData(OddsApiSportsEvent.Bookmaker bookmaker, OddsApiSportsEvent.Market market, EdgeData edgeData) {
        OutcomeResult newOutcome = buildOutcomeResult(bookmaker.getKey(), market.getOutcomes(), edgeData.getHomeTeam(), edgeData.getAwayTeam());
        edgeData.getOutcomeResults().add(newOutcome);

        if (Objects.equals(bookmaker.getKey(), "pinnacle")) {
            edgeData.setSharpestOutcomeResult(newOutcome);
            edgeData.setMarketWidth(Calculator.calculateMarketWidth(newOutcome.homePrice(), newOutcome.awayPrice()));
        }
    }

    public static OutcomeResult buildOutcomeResult(String bookmaker, List<OddsApiSportsEvent.Outcome> outcomes, String homeOutcomeName, String awayOutcomeName) {
        Double homePrice = getOutcomeByName(outcomes, homeOutcomeName).getPrice();
        Double awayPrice = getOutcomeByName(outcomes, awayOutcomeName).getPrice();
        double impliedProbabilityHome = Calculator.calculateImpliedProbability(homePrice);
        double impliedProbabilityAway = Calculator.calculateImpliedProbability(awayPrice);
        double juice = Calculator.calculateJuice(impliedProbabilityHome, impliedProbabilityAway);
        double noVigProbabilityHome = Calculator.calculateNoVigFairOddsProbability(impliedProbabilityHome, impliedProbabilityAway);
        double noVigProbabilityAway = Calculator.calculateNoVigFairOddsProbability(impliedProbabilityAway, impliedProbabilityHome);
        double noVigOddsHome = Calculator.calculateOddsByProbability(noVigProbabilityHome);
        double noVigOddsAway = Calculator.calculateOddsByProbability(noVigProbabilityAway);

        return new OutcomeResult(
                bookmaker,
                homeOutcomeName,
                awayOutcomeName,
                homePrice,
                awayPrice,
                impliedProbabilityHome,
                impliedProbabilityAway,
                noVigProbabilityHome,
                noVigProbabilityAway,
                noVigOddsHome,
                noVigOddsAway,
                juice
        );
    }

    private static OddsApiSportsEvent.Outcome getOutcomeByName(List<OddsApiSportsEvent.Outcome> outcomes, String outcomeName) {
        return outcomes.stream()
                .filter(outcome -> Objects.equals(outcome.getName(), outcomeName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(
                        String.format("Outcome not found for name: [name:%s]", outcomeName)
                ));
    }
}
