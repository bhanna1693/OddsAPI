package com.bhanna.oddsapi.model.OddsApi;

import com.bhanna.oddsapi.model.MarketKey;
import com.bhanna.oddsapi.model.SportKey;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OddsApiSportsEvent {

    private String id;
    private SportKey sportKey;
    private String sportTitle;
    private LocalDateTime commenceTime;
    private String homeTeam;
    private String awayTeam;
    private List<Bookmaker> bookmakers = List.of();

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Bookmaker {
        private String key;
        private String title;
        private LocalDateTime lastUpdate;
        private List<Market> markets;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Market {
        private MarketKey key;
        private LocalDateTime lastUpdate;
        private List<Outcome> outcomes;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Outcome {
        // A label of the outcome. For totals markets, this can be 'Over' or 'Under'.
        // For all other markets, this will be the name of the team or participant, or 'Draw'
        private String name;

        //  The odds of the outcome.
        //  The format is determined by the oddsFormat query param.
        //  The format is decimal by default.
        private Double price;
        //  The handicap or points of the outcome,
        //  only applicable to spreads and totals markets
        //  (this property will be missing for h2h and outrights markets)
        @Nullable
        private Double point;

        @Nullable
        //  This field is only relevant for certain markets.
        //  It contains more information about the outcome
        //  (for example, for player prop markets, it includes the player's name)
        private String description;
    }
}
