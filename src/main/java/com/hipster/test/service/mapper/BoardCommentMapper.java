package com.hipster.test.service.mapper;

import com.hipster.test.domain.Board;
import com.hipster.test.domain.BoardComment;
import com.hipster.test.service.dto.BoardCommentDTO;
import com.hipster.test.service.dto.BoardDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BoardComment} and its DTO {@link BoardCommentDTO}.
 */
@Mapper(componentModel = "spring")
public interface BoardCommentMapper extends EntityMapper<BoardCommentDTO, BoardComment> {
    @Mapping(target = "board", source = "board", qualifiedByName = "boardId")
    @Mapping(target = "board", source = "board", qualifiedByName = "boardId")
    BoardCommentDTO toDto(BoardComment s);

    @Named("boardId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BoardDTO toDtoBoardId(Board board);
}
