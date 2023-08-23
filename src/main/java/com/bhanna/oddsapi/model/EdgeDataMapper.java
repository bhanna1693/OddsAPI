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

    public static void addOutcomeResult(OddsApiSportsEvent.Bookmaker bookmaker, OddsApiSportsEvent.Market market, EdgeData edgeData) {
        OutcomeResult newOutcome = OutcomeResultMapper.generate(
                bookmaker.getKey(),
                edgeData.getHomeTeam(),
                edgeData.getAwayTeam(),
                getOutcomeByName(market.getOutcomes(), edgeData.getHomeTeam()).getPrice(),
                getOutcomeByName(market.getOutcomes(), edgeData.getAwayTeam()).getPrice()
        );
        edgeData.getOutcomeResults().add(newOutcome);
    }

    public static void setSharpestInfo(String sharpestBookmaker, List<OddsApiSportsEvent.Outcome> outcomes, EdgeData edgeData) {
        OutcomeResult outcomeResult = OutcomeResultMapper.generate(
                sharpestBookmaker,
                edgeData.getHomeTeam(),
                edgeData.getAwayTeam(),
                getOutcomeByName(outcomes, edgeData.getHomeTeam()).getPrice(),
                getOutcomeByName(outcomes, edgeData.getAwayTeam()).getPrice()
        );
        edgeData.setSharpestOutcomeResult(outcomeResult);
        edgeData.setMarketWidth(Calculator.calculateMarketWidth(outcomeResult.homePrice(), outcomeResult.awayPrice()));
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
