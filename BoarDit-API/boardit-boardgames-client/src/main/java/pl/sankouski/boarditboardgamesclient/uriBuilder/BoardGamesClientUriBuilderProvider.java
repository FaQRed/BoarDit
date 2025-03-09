package pl.sankouski.boarditboardgamesclient.uriBuilder;

import org.springframework.web.util.UriComponentsBuilder;

public interface BoardGamesClientUriBuilderProvider {

    String api();
    String host();
    int apiVersion();

    default UriComponentsBuilder builder(){
        return UriComponentsBuilder.newInstance()
                .scheme("http")
                .host(host())
                .path(api())
                .path(apiVersion()+"");

    }
}
