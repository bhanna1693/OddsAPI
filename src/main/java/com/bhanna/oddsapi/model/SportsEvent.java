package com.bhanna.oddsapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("sport_events")
public class SportsEvent {
    @Id
    private Long id;
    private String oddsApiId;
    private String sportKey;
    private String sportTitle;
    private Instant commenceTime;
    private String homeTeam;
    private String awayTeam;
    private LocalDateTime lastUpdate = LocalDateTime.now();
    private List<Bookmaker> bookmakers;
}
