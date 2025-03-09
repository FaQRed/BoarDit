package pl.sankouski.boarditupdater.boardGames.mapper;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.sankouski.boarditboardgamesclient.dto.BoardGameDto;
import pl.sankouski.boarditdata.model.boardgame.Category;
import pl.sankouski.boarditdata.repository.BoarDitDataCatalog;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryMapper {

    @Transactional
    public  List<Category> mapCategory(BoardGameDto boardGameDto, BoarDitDataCatalog boarDitDataCatalog){
        return boardGameDto.getLinks().stream()
                .filter(link -> "boardgamecategory".equals(link.getType()))
                .map(link -> {
                    Category category = new Category().setName(link.getValue()).setBggId(Long.parseLong(link.getId()));
                    Category existingCategory = boarDitDataCatalog.getCategories().findByBggId(category.getBggId()).orElse(null);

                    if(existingCategory == null){
                        boarDitDataCatalog.getCategories().save(category);
                        return category;
                    }
                    return existingCategory;
                })
                .collect(Collectors.toList());
    }
}