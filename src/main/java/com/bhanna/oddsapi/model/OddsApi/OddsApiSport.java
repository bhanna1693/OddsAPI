package com.bhanna.oddsapi.model.OddsApi;

import com.bhanna.oddsapi.model.SportKey;
import lombok.Data;

@Data
public class OddsApiSport {
    private SportKey key;
    private Boolean active;
    private String group;
    private String description;
    private String title;
    private Boolean hasOutrights;
}
