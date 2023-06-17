package com.hipster.test.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.hipster.test.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BoardCommentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BoardCommentDTO.class);
        BoardCommentDTO boardCommentDTO1 = new BoardCommentDTO();
        boardCommentDTO1.setId(1L);
        BoardCommentDTO boardCommentDTO2 = new BoardCommentDTO();
        assertThat(boardCommentDTO1).isNotEqualTo(boardCommentDTO2);
        boardCommentDTO2.setId(boardCommentDTO1.getId());
        assertThat(boardCommentDTO1).isEqualTo(boardCommentDTO2);
        boardCommentDTO2.setId(2L);
        assertThat(boardCommentDTO1).isNotEqualTo(boardCommentDTO2);
        boardCommentDTO1.setId(null);
        assertThat(boardCommentDTO1).isNotEqualTo(boardCommentDTO2);
    }
}
