package com.bhanna.oddsapi.model;

import lombok.Getter;

@Getter
public enum MarketKey {
    H2H("h2h"),
    SPREADS("spreads"),
    TOTALS("totals"),
    OUTRIGHTS("outrights"),
    H2H_LAY("h2h_lay"),
    OUTRIGHTS_LAY("outrights_lay"),
    ALT_SPREADS("alternate_spreads"),
    ALT_TOTALS("alternate_totals"),
    BTTS("btts"),
    DRAW_NO_BET("draw_no_bet"),
    H2H_3_WAY("h2h_3_way"),
    ;

    private final String key;

    MarketKey(String key) {
        this.key = key;
    }

}
