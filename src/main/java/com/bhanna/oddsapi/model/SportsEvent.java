package com.bhanna.oddsapi.model;

import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class SportsEvent {

    private String id;
    private String sportKey;
    private String sportTitle;
    private Instant commenceTime;
    private String homeTeam;
    private String awayTeam;
    private List<Bookmaker> bookmakers = List.of();

    @Data
    public static class Bookmaker {
        private String key;
        private String title;
        private Instant lastUpdate;
        private List<Market> markets;
    }

    @Data
    public static class Market {
        private String key;
        private Instant lastUpdate;
        private List<Outcome> outcomes;
    }
}
