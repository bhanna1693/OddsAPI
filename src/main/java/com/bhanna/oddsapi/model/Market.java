package com.bhanna.oddsapi.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Market {
    private MarketKey key;
    private LocalDateTime lastUpdate;
    private List<Outcome> outcomes;
}
