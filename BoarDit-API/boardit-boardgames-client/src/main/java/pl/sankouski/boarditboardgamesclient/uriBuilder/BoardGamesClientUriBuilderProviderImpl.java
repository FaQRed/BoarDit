package pl.sankouski.boarditboardgamesclient.uriBuilder;

import org.springframework.beans.factory.annotation.Value;

public record BoardGamesClientUriBuilderProviderImpl(String api, String host,
                                                     int apiVersion) implements BoardGamesClientUriBuilderProvider {

    public BoardGamesClientUriBuilderProviderImpl(@Value("${bgg.api.key}") String api,
                                                  @Value("${bgg.api.host}") String host,
                                                  @Value("${bgg.api.version}") int apiVersion) {
        this.api = api;
        this.host = host;
        this.apiVersion = apiVersion;
    }
}
