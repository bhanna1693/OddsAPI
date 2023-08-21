package com.bhanna.oddsapi.model;

public record OutcomeResult(
        String bookmaker,
        String homeName,
        String awayName,
        Double homePrice,
        Double awayPrice,
        Double impliedProbabilityHome,
        Double impliedProbabilityAway,

        Double noVigProbabilityHome,
        Double noVigProbabilityAway,
        Double noVigOddsHome,
        Double noVigOddsAway,
        Double juice
) {
}
