package com.bhanna.oddsapi.model.OddsApi;

import lombok.Data;

@Data
public class OddsApiSport {
    private String key;
    private Boolean active;
    private String group;
    private String description;
    private String title;
    private Boolean hasOutrights;
}
