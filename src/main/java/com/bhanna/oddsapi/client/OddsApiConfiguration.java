package com.bhanna.oddsapi.client;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "odds-api.odds-client")
@Getter
@Setter
public class OddsApiConfiguration {
    private String token;
    private List<String> bookmakers;
}
