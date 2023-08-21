package com.bhanna.oddsapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("edge_data")
public class EdgeData {
    @Id
    private Long id;
    private SportKey sport; // provided ***
    private String sportTitle; // provided ***
    private MarketKey market; // provided ***
    private String eventId; // provided ***
    private LocalDateTime startTime; // provided ***
    private String homeTeam; // provided ***
    private String awayTeam; // provided ***
    private Double edgePercent; // calculated
    private Integer marketWidth; // calculated
    private Double homeEdgePercent;
    private Double awayEdgePercent;
    private String bestPriceAwayName;
    private Double bestPriceAwayOdds;
    private List<String> bestPriceAwayBooks;
    private String bestPriceHomeName;
    private Double bestPriceHomeOdds;
    private List<String> bestPriceHomeBooks;
    private List<OutcomeResult> outcomeResults; // computed
    private OutcomeResult sharpestOutcomeResult; // computed
    private LocalDateTime created = LocalDateTime.now();
}
