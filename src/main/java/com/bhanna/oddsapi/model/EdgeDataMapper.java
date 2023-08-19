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

    public static void extracted(OddsApiSportsEvent.Market market, EdgeData edgeData) {
        OutcomeResult newOutcome = getResult(market.getOutcomes(), edgeData.getHomeTeam(), edgeData.getAwayTeam());
        edgeData.getOutcomeResults().add(newOutcome);
    }

    public static OutcomeResult getResult(List<OddsApiSportsEvent.Outcome> outcomes, String homeOutcomeName, String awayOutcomeName) {
        Double homePrice = getOutcomeByName(outcomes, homeOutcomeName).getPrice();
        Double awayPrice = getOutcomeByName(outcomes, awayOutcomeName).getPrice();
        Double impliedProbabilityHome = Calculator.calculateImpliedProbability(homePrice);
        Double impliedProbabilityAway = Calculator.calculateImpliedProbability(awayPrice);
        Double juice = (impliedProbabilityHome + impliedProbabilityAway) - 1;
        Double noVigFairOddsLineHome = impliedProbabilityHome / (impliedProbabilityHome + impliedProbabilityAway);
        Double noVigFairOddsLineAway = impliedProbabilityAway / (impliedProbabilityAway + impliedProbabilityAway);
        return new OutcomeResult(homePrice, awayPrice, impliedProbabilityHome, impliedProbabilityAway, noVigFairOddsLineHome, noVigFairOddsLineAway, juice);
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
