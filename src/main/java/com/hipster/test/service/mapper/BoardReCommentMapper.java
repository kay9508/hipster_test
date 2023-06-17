package com.hipster.test.service.mapper;

import com.hipster.test.domain.BoardComment;
import com.hipster.test.domain.BoardReComment;
import com.hipster.test.service.dto.BoardCommentDTO;
import com.hipster.test.service.dto.BoardReCommentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BoardReComment} and its DTO {@link BoardReCommentDTO}.
 */
@Mapper(componentModel = "spring")
public interface BoardReCommentMapper extends EntityMapper<BoardReCommentDTO, BoardReComment> {
    @Mapping(target = "boardComent", source = "boardComent", qualifiedByName = "boardCommentId")
    @Mapping(target = "boardComment", source = "boardComment", qualifiedByName = "boardCommentId")
    BoardReCommentDTO toDto(BoardReComment s);

    @Named("boardCommentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BoardCommentDTO toDtoBoardCommentId(BoardComment boardComment);
}
