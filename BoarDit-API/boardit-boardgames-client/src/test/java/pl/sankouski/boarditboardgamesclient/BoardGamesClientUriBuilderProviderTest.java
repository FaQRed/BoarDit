package pl.sankouski.boarditboardgamesclient;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.sankouski.boarditboardgamesclient.uriBuilder.BoardGamesClientUriBuilderProviderImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BoardGamesClientUriBuilderProviderTest {

    private BoardGamesClientUriBuilderProviderImpl provider;

    @BeforeEach
    public void setup() {
        provider = new BoardGamesClientUriBuilderProviderImpl("xmlapi", "boardgamegeek.com", 2);
    }

    @Test
    public void builder_shouldBuildCorrectUri() {
        var uri = provider.builder()
                .pathSegment("thing")
                .queryParam("id", 123)
                .toUriString();

        assertEquals("http://boardgamegeek.com/xmlapi2/thing?id=123", uri);
    }

    @Test
    public void api_shouldReturnCorrectValue() {
        assertEquals("xmlapi", provider.api());
    }

    @Test
    public void host_shouldReturnCorrectValue() {
        assertEquals("boardgamegeek.com", provider.host());
    }

    @Test
    public void apiVersion_shouldReturnCorrectValue() {
        assertEquals(2, provider.apiVersion());
    }
}