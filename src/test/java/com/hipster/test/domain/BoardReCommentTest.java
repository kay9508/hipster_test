package com.hipster.test.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.hipster.test.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BoardReCommentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BoardReComment.class);
        BoardReComment boardReComment1 = new BoardReComment();
        boardReComment1.setId(1L);
        BoardReComment boardReComment2 = new BoardReComment();
        boardReComment2.setId(boardReComment1.getId());
        assertThat(boardReComment1).isEqualTo(boardReComment2);
        boardReComment2.setId(2L);
        assertThat(boardReComment1).isNotEqualTo(boardReComment2);
        boardReComment1.setId(null);
        assertThat(boardReComment1).isNotEqualTo(boardReComment2);
    }
}
