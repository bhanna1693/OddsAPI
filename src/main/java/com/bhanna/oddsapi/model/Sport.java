package com.bhanna.oddsapi.model;

import lombok.Data;

@Data
public class Sport {
    private String key;
    private Boolean active;
    private String group;
    private String description;
    private String title;
    private Boolean hasOutrights;
}
