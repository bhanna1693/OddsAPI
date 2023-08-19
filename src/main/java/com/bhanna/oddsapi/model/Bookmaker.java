package com.bhanna.oddsapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bookmaker {
    private String key;
    private String title;
    private List<Outcome> outcomes;
    private LocalDateTime lastUpdate = LocalDateTime.now();
}
