package com.bhanna.oddsapi.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "odds-api")
public class OddsApiProperties {
    private String bookmakers;
    private Cors cors;
    private String openai;
    private OddsClient oddsClient;

    @Data
    public static class Cors {
        private List<String> allowedOrigins;
    }

    @Data
    public static class OddsClient {
        private String token;
        private String url;
    }
}


