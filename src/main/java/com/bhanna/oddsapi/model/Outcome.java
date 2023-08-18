package com.bhanna.oddsapi.model;

import jakarta.annotation.Nullable;
import lombok.Data;

@Data
public class Outcome {
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

    private Double expectedValue;
}