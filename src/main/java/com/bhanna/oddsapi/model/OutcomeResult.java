package com.bhanna.oddsapi.model;

public record OutcomeResult(
        Double homePrice,
        Double awayPrice,
        Double impliedProbabilityHome,
        Double impliedProbabilityAway,

        Double noVigFairOddsLineHome,
        Double noVigFairOddsLineAway,
        Double juice
) {
}
