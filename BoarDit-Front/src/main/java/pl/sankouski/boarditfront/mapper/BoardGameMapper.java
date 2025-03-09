package pl.sankouski.boarditfront.mapper;

import pl.sankouski.boarditfront.dto.BoardGameDTO;
import pl.sankouski.boarditfront.dto.BoardGameSummaryDto;

import java.util.List;
import java.util.stream.Collectors;

public class BoardGameMapper {

    public static BoardGameSummaryDto toSummary(BoardGameDTO boardGameDTO) {
        if (boardGameDTO == null) {
            return null;
        }

        BoardGameSummaryDto summaryDTO = new BoardGameSummaryDto();
        summaryDTO.setId(boardGameDTO.getId());
        summaryDTO.setName(boardGameDTO.getName());
        summaryDTO.setImageUrl(boardGameDTO.getImageLink());
        return summaryDTO;
    }

    public static List<BoardGameSummaryDto> toSummaryList(List<BoardGameDTO> boardGameDTOs) {
        if (boardGameDTOs == null || boardGameDTOs.isEmpty()) {
            return List.of();
        }

        return boardGameDTOs.stream()
                .map(BoardGameMapper::toSummary)
                .collect(Collectors.toList());
    }
}