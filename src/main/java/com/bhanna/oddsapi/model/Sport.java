package com.bhanna.oddsapi.model;

import lombok.Data;

@Data
public class Sport {
    private SportKey key;
    private Boolean active = true;
    private String group;
    private String description;
    private String title;
    private Boolean hasOutrights;
}
