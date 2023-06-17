package com.hipster.test.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BoardCommentMapperTest {

    private BoardCommentMapper boardCommentMapper;

    @BeforeEach
    public void setUp() {
        boardCommentMapper = new BoardCommentMapperImpl();
    }
}
