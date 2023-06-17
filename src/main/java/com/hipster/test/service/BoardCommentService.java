package com.hipster.test.service;

import com.hipster.test.domain.BoardComment;
import com.hipster.test.repository.BoardCommentRepository;
import com.hipster.test.service.dto.BoardCommentDTO;
import com.hipster.test.service.mapper.BoardCommentMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link BoardComment}.
 */
@Service
@Transactional
public class BoardCommentService {

    private final Logger log = LoggerFactory.getLogger(BoardCommentService.class);

    private final BoardCommentRepository boardCommentRepository;

    private final BoardCommentMapper boardCommentMapper;

    public BoardCommentService(BoardCommentRepository boardCommentRepository, BoardCommentMapper boardCommentMapper) {
        this.boardCommentRepository = boardCommentRepository;
        this.boardCommentMapper = boardCommentMapper;
    }

    /**
     * Save a boardComment.
     *
     * @param boardCommentDTO the entity to save.
     * @return the persisted entity.
     */
    public BoardCommentDTO save(BoardCommentDTO boardCommentDTO) {
        log.debug("Request to save BoardComment : {}", boardCommentDTO);
        BoardComment boardComment = boardCommentMapper.toEntity(boardCommentDTO);
        boardComment = boardCommentRepository.save(boardComment);
        return boardCommentMapper.toDto(boardComment);
    }

    /**
     * Update a boardComment.
     *
     * @param boardCommentDTO the entity to save.
     * @return the persisted entity.
     */
    public BoardCommentDTO update(BoardCommentDTO boardCommentDTO) {
        log.debug("Request to update BoardComment : {}", boardCommentDTO);
        BoardComment boardComment = boardCommentMapper.toEntity(boardCommentDTO);
        boardComment = boardCommentRepository.save(boardComment);
        return boardCommentMapper.toDto(boardComment);
    }

    /**
     * Partially update a boardComment.
     *
     * @param boardCommentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BoardCommentDTO> partialUpdate(BoardCommentDTO boardCommentDTO) {
        log.debug("Request to partially update BoardComment : {}", boardCommentDTO);

        return boardCommentRepository
            .findById(boardCommentDTO.getId())
            .map(existingBoardComment -> {
                boardCommentMapper.partialUpdate(existingBoardComment, boardCommentDTO);

                return existingBoardComment;
            })
            .map(boardCommentRepository::save)
            .map(boardCommentMapper::toDto);
    }

    /**
     * Get all the boardComments.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<BoardCommentDTO> findAll() {
        log.debug("Request to get all BoardComments");
        return boardCommentRepository.findAll().stream().map(boardCommentMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one boardComment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BoardCommentDTO> findOne(Long id) {
        log.debug("Request to get BoardComment : {}", id);
        return boardCommentRepository.findById(id).map(boardCommentMapper::toDto);
    }

    /**
     * Delete the boardComment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete BoardComment : {}", id);
        boardCommentRepository.deleteById(id);
    }
}
