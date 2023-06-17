package com.hipster.test.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A DTO for the {@link com.hipster.test.domain.BoardComment} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardCommentDTO implements Serializable {

    private Long id;

    @Size(max = 5000)
    private String content;

    private Boolean delAt;

    private BoardDTO board;
}
