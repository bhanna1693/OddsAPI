package com.bhanna.oddsapi.model.OddsApi;

import com.bhanna.oddsapi.model.MarketKey;
import com.bhanna.oddsapi.model.SportKey;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OddsApiSportsEvent {

    @JsonProperty("id")
    private String id;

    @JsonProperty("sport_key")
    private SportKey sportKey;

    @JsonProperty("sport_title")
    private String sportTitle;

    @JsonProperty("commence_time")
    private LocalDateTime commenceTime;

    @JsonProperty("home_team")
    private String homeTeam;

    @JsonProperty("away_team")
    private String awayTeam;

    private List<Bookmaker> bookmakers = new ArrayList<>();

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Bookmaker {
        @JsonProperty("key")
        private String key;

        @JsonProperty("title")
        private String title;

        @JsonProperty("last_update")
        private LocalDateTime lastUpdate;

        @JsonProperty("markets")
        private List<Market> markets;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Market {
        @JsonProperty("key")
        private MarketKey key;

        @JsonProperty("last_update")
        private LocalDateTime lastUpdate;

        @JsonProperty("outcomes")
        private List<Outcome> outcomes;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Outcome {
        @JsonProperty("name")
        private String name;

        @JsonProperty("price")
        private Double price;

        @JsonProperty("point")
        @Nullable
        private Double point;

        @JsonProperty("description")
        @Nullable
        private String description;
    }
}
