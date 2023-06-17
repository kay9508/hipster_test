package com.hipster.test.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.hipster.test.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BoardCommentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BoardComment.class);
        BoardComment boardComment1 = new BoardComment();
        boardComment1.setId(1L);
        BoardComment boardComment2 = new BoardComment();
        boardComment2.setId(boardComment1.getId());
        assertThat(boardComment1).isEqualTo(boardComment2);
        boardComment2.setId(2L);
        assertThat(boardComment1).isNotEqualTo(boardComment2);
        boardComment1.setId(null);
        assertThat(boardComment1).isNotEqualTo(boardComment2);
    }
}
