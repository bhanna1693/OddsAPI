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
    private Double edgePercent; // calculated
    private Integer sharpestHomePrice; // filter to find
    private Integer sharpestAwayPrice; // filter to find
    private Integer zeroVigHomePrice; // calculated
    private Integer zeroVigAwayPrice; // calculated
    private Integer marketWidth; // calculated
    private SportKey sport; // provided
    private String sportTitle; // provided
    private MarketKey market; // provided
    private String marketTitle; // mapped
    private String eventId; // provided
    private LocalDateTime startDate; // provided
    private String homeTeam; // provided
    private String awayTeam; // provided
    private String bestPriceHomeName; // built
    private String bestPriceHomeOdd; // built
    private List<String> bestPriceHomeOddBooks; // filter to find
    private String bestPriceAwayName; // built
    private String bestPriceAwayOdd; // built
    private List<String> bestPriceAwayOddBooks; // filter to find
    private LocalDateTime created = LocalDateTime.now();
}
