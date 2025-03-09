package pl.sankouski.boarditboardgamesclient.uriBuilder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import pl.sankouski.boarditboardgamesclient.BoardGameClientImpl;

@Configuration
public class BoarditClientConfig {

    @Bean
    public BoardGamesClientUriBuilderProvider boardGamesClientUriBuilderProvider(
            @Value("${bgg.api.key}") String apiKey,
            @Value("${bgg.api.host}") String host,
            @Value("${bgg.api.version}") int apiVersion){
        return new BoardGamesClientUriBuilderProviderImpl(apiKey, host, apiVersion);
    }


    @Bean
    @Scope("prototype")
    public BoardGameClientImpl moviesClient(BoardGamesClientUriBuilderProvider uriBuilderProvider) {
        return new BoardGameClientImpl(uriBuilderProvider);
    }
}
