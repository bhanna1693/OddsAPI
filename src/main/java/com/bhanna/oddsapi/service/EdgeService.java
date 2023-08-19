package com.bhanna.oddsapi.service;

import com.bhanna.oddsapi.model.EdgeData;
import com.bhanna.oddsapi.model.OddsApi.OddsApiSportsEvent;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class EdgeService {
    // the sharpest book
    // also consider "Circa"
    private static final String sharpestBook = "pinnacle";

    public EdgeData calculateEdgeForSportsEvent(OddsApiSportsEvent sportsEvent) {

    }

}
