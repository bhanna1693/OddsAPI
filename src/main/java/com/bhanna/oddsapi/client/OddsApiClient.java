package com.bhanna.oddsapi.client;

import com.bhanna.oddsapi.config.OddsApiProperties;
import com.bhanna.oddsapi.model.MarketKey;
import com.bhanna.oddsapi.model.OddsApi.OddsApiSport;
import com.bhanna.oddsapi.model.OddsApi.OddsApiSportsEvent;
import com.bhanna.oddsapi.model.SportKey;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

@Component
@Log4j2
public class OddsApiClient {

    private final WebClient webClient;
    private final OddsApiProperties oddsApiProperties;

    public OddsApiClient(OddsApiProperties oddsApiProperties) {
        this.oddsApiProperties = oddsApiProperties;
        this.webClient = WebClient.builder()
                .baseUrl(oddsApiProperties.getOddsClient().getUrl())
                .defaultHeaders(headers -> headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE))
                .build();
    }

    public Flux<OddsApiSport> getSports() {
        return Flux.just(new OddsApiSport());
//        return this.webClient.get()
//                .uri(uriBuilder -> uriBuilder
//                        .path("/sports")
//                        .queryParam("all", true)
//                        .build()
//                )
//                .exchangeToFlux(response -> response.bodyToFlux(Sport.class));
    }

    public Flux<OddsApiSportsEvent> getOddsForSport(SportKey sportKey, List<String> bookmakers, List<MarketKey> markets, Boolean includePinnacle) {
        if (includePinnacle) {
            bookmakers.add("pinnacle");
        }

        return this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/sports/{sport}/odds")
                        .queryParam("apiKey", oddsApiProperties.getOddsClient().getToken())
                        .queryParam("regions", "us")
                        .queryParam("markets", String.join(",", markets.stream().map(Enum::name).toList()))
                        .queryParam("bookmakers", String.join(",", bookmakers))
                        .build(Map.of("sport", "baseball_mlb"))
                )
                .retrieve()
                .bodyToFlux(OddsApiSportsEvent.class)
                .log();
    }
}
