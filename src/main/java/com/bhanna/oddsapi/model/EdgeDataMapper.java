package com.bhanna.oddsapi.model;

import com.bhanna.oddsapi.model.OddsApi.OddsApiSportsEvent;
import com.bhanna.oddsapi.util.Calculator;

import java.util.List;

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
        OutcomeResult newOutcome = OutcomeResultMapper.fromBookmakerOutcome(bookmaker.getKey(), market.getOutcomes(), edgeData);
        edgeData.getOutcomeResults().add(newOutcome);
    }

    public static void setSharpestInfo(String sharpestBookmaker, List<OddsApiSportsEvent.Outcome> outcomes, EdgeData edgeData) {
        OutcomeResult outcomeResult = OutcomeResultMapper.fromBookmakerOutcome(sharpestBookmaker, outcomes, edgeData);
        edgeData.setSharpestOutcomeResult(outcomeResult);
        edgeData.setMarketWidth(Calculator.calculateMarketWidth(outcomeResult.homePrice(), outcomeResult.awayPrice()));
    }

}
