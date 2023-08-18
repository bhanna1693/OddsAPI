package com.bhanna.oddsapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("bets")
public class Bet {
    @Id
    private Long id;
    private String bookmaker;
    private String oddsApiId;
    private String sportKey;
    private String sportTitle;
    private Instant commenceTime;
    private Outcome outcome;
    private Double expectedValue;
    private LocalDateTime createdAt = LocalDateTime.now();
}
