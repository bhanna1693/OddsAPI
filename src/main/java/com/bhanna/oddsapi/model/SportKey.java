package com.bhanna.oddsapi.model;

import lombok.Getter;

@Getter
public enum SportKey {
    BASEBALL_MLB("baseball_mlb"),
    FOOTBALL_NFL("americanfootball_nfl"),
    SOCCER_EPL("soccer_epl"),
    SOCCER_MLS("soccer_usa_mls"),
    ;

    private final String key;

    SportKey(String key) {
        this.key = key;
    }

}
