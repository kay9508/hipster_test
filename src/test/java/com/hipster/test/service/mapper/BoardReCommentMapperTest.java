package com.hipster.test.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BoardReCommentMapperTest {

    private BoardReCommentMapper boardReCommentMapper;

    @BeforeEach
    public void setUp() {
        boardReCommentMapper = new BoardReCommentMapperImpl();
    }
}
