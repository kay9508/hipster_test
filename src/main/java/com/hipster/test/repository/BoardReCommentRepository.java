package com.hipster.test.repository;

import com.hipster.test.domain.BoardReComment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BoardReComment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BoardReCommentRepository extends JpaRepository<BoardReComment, Long> {}
