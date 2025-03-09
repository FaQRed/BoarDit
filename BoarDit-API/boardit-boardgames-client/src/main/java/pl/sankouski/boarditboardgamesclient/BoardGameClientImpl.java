package pl.sankouski.boarditboardgamesclient;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.client.RestTemplate;
import pl.sankouski.boarditboardgamesclient.dto.BoardGameDto;
import pl.sankouski.boarditboardgamesclient.dto.ExpansionDto;
import pl.sankouski.boarditboardgamesclient.dto.ItemsBoardGame;
import pl.sankouski.boarditboardgamesclient.dto.ItemsExpansion;
import pl.sankouski.boarditboardgamesclient.exception.BoardGameNotFoundException;
import pl.sankouski.boarditboardgamesclient.uriBuilder.BoardGamesClientUriBuilderProvider;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class BoardGameClientImpl implements BoardGameClient{

    private static final Logger log = LogManager.getLogger(BoardGameClientImpl.class);
    RestTemplate restClient;
    BoardGamesClientUriBuilderProvider provider;

    public BoardGameClientImpl(BoardGamesClientUriBuilderProvider boardGamesClientUriBuilderProvider) {
        this.restClient = new RestTemplate();
        this.provider = boardGamesClientUriBuilderProvider;
    }

    @Override
    public BoardGameDto getBoardGameByIdFromApi(Long id) {
        var uri = "https://boardgamegeek.com/xmlapi2/thing?id=" + id;

        try {
            JAXBContext context = JAXBContext.newInstance(ItemsBoardGame.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            String response = restClient.getForObject(uri, String.class);
            if (response == null || response.isEmpty()) {
                throw new BoardGameNotFoundException(id);
            }

            StringReader reader = new StringReader(response);
            ItemsBoardGame items = (ItemsBoardGame) unmarshaller.unmarshal(reader);

            if (items.getItems() == null) {
                throw new BoardGameNotFoundException(id);
            }

            items.getItems().get(0).setBggId(id);
            return items.getItems().get(0);

        } catch (JAXBException e) {
            log.error("Error unmarshalling XML response: ", e);
            throw new RuntimeException("Failed to parse API response", e); // Проброс исключения
        } catch (Exception e) {
            log.error("Error retrieving or processing data from API for BoardGame ID: {}", id, e);
            throw e;
        }
    }

    @Override
    public List<ExpansionDto> getExpansionByIdFromApi(List<Long> ids) {


        var uri = "https://boardgamegeek.com/xmlapi2/thing?&type=boardgameexpansion&id=";

        List<ExpansionDto> allExpansionDtos = new ArrayList<>();


        final int batchSize = 20;
        for (int i = 0; i < ids.size(); i += batchSize) {
            List<Long> batch = ids.subList(i, Math.min(i + batchSize, ids.size()));
            String joinedIds = batch.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));
            String finalUri = uri + joinedIds;


            try {
                JAXBContext context = JAXBContext.newInstance(ItemsExpansion.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();

                String response = restClient.getForObject(finalUri, String.class);
                if (response == null || response.isEmpty()) {
                    continue;
                }
                StringReader reader = new StringReader(response);
                ItemsExpansion itemsExpansion = (ItemsExpansion) unmarshaller.unmarshal(reader);


                List<ExpansionDto> expansionDtos = itemsExpansion.getItems();

                for (int j = 0; j < batch.size(); j++) {
                    if (j < expansionDtos.size()) {
                        expansionDtos.get(j).setBggId(batch.get(j));
                    }
                }
               allExpansionDtos.addAll(expansionDtos);

                Thread.sleep(1000);
            } catch (JAXBException e) {
                log.error("Error unmarshalling XML response: ", e);
            } catch (Exception e) {
                log.error("Error retrieving or processing data from API for IDs: {}", joinedIds, e);
            }


        }
        return allExpansionDtos;
    }

}
