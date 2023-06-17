package com.hipster.test.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.hipster.test.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BoardReCommentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BoardReCommentDTO.class);
        BoardReCommentDTO boardReCommentDTO1 = new BoardReCommentDTO();
        boardReCommentDTO1.setId(1L);
        BoardReCommentDTO boardReCommentDTO2 = new BoardReCommentDTO();
        assertThat(boardReCommentDTO1).isNotEqualTo(boardReCommentDTO2);
        boardReCommentDTO2.setId(boardReCommentDTO1.getId());
        assertThat(boardReCommentDTO1).isEqualTo(boardReCommentDTO2);
        boardReCommentDTO2.setId(2L);
        assertThat(boardReCommentDTO1).isNotEqualTo(boardReCommentDTO2);
        boardReCommentDTO1.setId(null);
        assertThat(boardReCommentDTO1).isNotEqualTo(boardReCommentDTO2);
    }
}
