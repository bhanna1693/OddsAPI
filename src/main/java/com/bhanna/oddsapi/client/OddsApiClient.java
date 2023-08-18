package com.bhanna.oddsapi.client;

import com.bhanna.oddsapi.model.Sport;
import com.bhanna.oddsapi.model.SportsEvent;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class OddsApiClient {

    private final WebClient webClient;

    public OddsApiClient(OddsApiConfiguration oddsApiConfiguration) {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.the-odds-api.com/v4")
                .defaultUriVariables(Collections.singletonMap("apiKey", oddsApiConfiguration.getToken()))
                .defaultHeaders(headers -> headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE))
                .build();
    }

    public Flux<Sport> getSports() {
        return Flux.just(new Sport());
//        return this.webClient.get()
//                .uri(uriBuilder -> uriBuilder
//                        .path("/sports")
//                        .queryParam("all", true)
//                        .build()
//                )
//                .exchangeToFlux(response -> response.bodyToFlux(Sport.class));
    }

    public Flux<SportsEvent> getOddsForSport(String sportKey, List<String> bookmakers) {
        return this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/sports/{sport}/odds")
                        .queryParam("regions", "us")
                        .queryParam("bookmakers", bookmakers)
                        .build(Map.of("sport", sportKey))
                )
                .exchangeToFlux(response -> response.bodyToFlux(SportsEvent.class));
    }
}
