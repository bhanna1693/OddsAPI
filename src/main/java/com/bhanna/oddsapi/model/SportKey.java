package com.bhanna.oddsapi.model;

public enum SportKey {
    BASEBALL_MLB("baseball_mlb"),
    ;

    private final String key;

    SportKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
