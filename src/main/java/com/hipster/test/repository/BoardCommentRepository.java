package com.hipster.test.repository;

import com.hipster.test.domain.BoardComment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BoardComment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BoardCommentRepository extends JpaRepository<BoardComment, Long> {}
