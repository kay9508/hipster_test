package com.hipster.test.service.mapper;

import com.hipster.test.domain.Board;
import com.hipster.test.service.dto.BoardDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Board} and its DTO {@link BoardDTO}.
 */
@Mapper(componentModel = "spring")
public interface BoardMapper extends EntityMapper<BoardDTO, Board> {}
