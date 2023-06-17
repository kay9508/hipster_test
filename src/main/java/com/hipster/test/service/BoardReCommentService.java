package com.hipster.test.service;

import com.hipster.test.domain.BoardReComment;
import com.hipster.test.repository.BoardReCommentRepository;
import com.hipster.test.service.dto.BoardReCommentDTO;
import com.hipster.test.service.mapper.BoardReCommentMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link BoardReComment}.
 */
@Service
@Transactional
public class BoardReCommentService {

    private final Logger log = LoggerFactory.getLogger(BoardReCommentService.class);

    private final BoardReCommentRepository boardReCommentRepository;

    private final BoardReCommentMapper boardReCommentMapper;

    public BoardReCommentService(BoardReCommentRepository boardReCommentRepository, BoardReCommentMapper boardReCommentMapper) {
        this.boardReCommentRepository = boardReCommentRepository;
        this.boardReCommentMapper = boardReCommentMapper;
    }

    /**
     * Save a boardReComment.
     *
     * @param boardReCommentDTO the entity to save.
     * @return the persisted entity.
     */
    public BoardReCommentDTO save(BoardReCommentDTO boardReCommentDTO) {
        log.debug("Request to save BoardReComment : {}", boardReCommentDTO);
        BoardReComment boardReComment = boardReCommentMapper.toEntity(boardReCommentDTO);
        boardReComment = boardReCommentRepository.save(boardReComment);
        return boardReCommentMapper.toDto(boardReComment);
    }

    /**
     * Update a boardReComment.
     *
     * @param boardReCommentDTO the entity to save.
     * @return the persisted entity.
     */
    public BoardReCommentDTO update(BoardReCommentDTO boardReCommentDTO) {
        log.debug("Request to update BoardReComment : {}", boardReCommentDTO);
        BoardReComment boardReComment = boardReCommentMapper.toEntity(boardReCommentDTO);
        boardReComment = boardReCommentRepository.save(boardReComment);
        return boardReCommentMapper.toDto(boardReComment);
    }

    /**
     * Partially update a boardReComment.
     *
     * @param boardReCommentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BoardReCommentDTO> partialUpdate(BoardReCommentDTO boardReCommentDTO) {
        log.debug("Request to partially update BoardReComment : {}", boardReCommentDTO);

        return boardReCommentRepository
            .findById(boardReCommentDTO.getId())
            .map(existingBoardReComment -> {
                boardReCommentMapper.partialUpdate(existingBoardReComment, boardReCommentDTO);

                return existingBoardReComment;
            })
            .map(boardReCommentRepository::save)
            .map(boardReCommentMapper::toDto);
    }

    /**
     * Get all the boardReComments.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<BoardReCommentDTO> findAll() {
        log.debug("Request to get all BoardReComments");
        return boardReCommentRepository
            .findAll()
            .stream()
            .map(boardReCommentMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one boardReComment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BoardReCommentDTO> findOne(Long id) {
        log.debug("Request to get BoardReComment : {}", id);
        return boardReCommentRepository.findById(id).map(boardReCommentMapper::toDto);
    }

    /**
     * Delete the boardReComment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete BoardReComment : {}", id);
        boardReCommentRepository.deleteById(id);
    }
}
