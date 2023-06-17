package com.hipster.test.web.rest;

import com.hipster.test.repository.BoardReCommentRepository;
import com.hipster.test.service.BoardReCommentService;
import com.hipster.test.service.dto.BoardReCommentDTO;
import com.hipster.test.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.hipster.test.domain.BoardReComment}.
 */
@RestController
@RequestMapping("/api")
public class BoardReCommentResource {

    private final Logger log = LoggerFactory.getLogger(BoardReCommentResource.class);

    private static final String ENTITY_NAME = "boardReComment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BoardReCommentService boardReCommentService;

    private final BoardReCommentRepository boardReCommentRepository;

    public BoardReCommentResource(BoardReCommentService boardReCommentService, BoardReCommentRepository boardReCommentRepository) {
        this.boardReCommentService = boardReCommentService;
        this.boardReCommentRepository = boardReCommentRepository;
    }

    /**
     * {@code POST  /board-re-comments} : Create a new boardReComment.
     *
     * @param boardReCommentDTO the boardReCommentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new boardReCommentDTO, or with status {@code 400 (Bad Request)} if the boardReComment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/board-re-comments")
    public ResponseEntity<BoardReCommentDTO> createBoardReComment(@Valid @RequestBody BoardReCommentDTO boardReCommentDTO)
        throws URISyntaxException {
        log.debug("REST request to save BoardReComment : {}", boardReCommentDTO);
        if (boardReCommentDTO.getId() != null) {
            throw new BadRequestAlertException("A new boardReComment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BoardReCommentDTO result = boardReCommentService.save(boardReCommentDTO);
        return ResponseEntity
            .created(new URI("/api/board-re-comments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /board-re-comments/:id} : Updates an existing boardReComment.
     *
     * @param id the id of the boardReCommentDTO to save.
     * @param boardReCommentDTO the boardReCommentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated boardReCommentDTO,
     * or with status {@code 400 (Bad Request)} if the boardReCommentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the boardReCommentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/board-re-comments/{id}")
    public ResponseEntity<BoardReCommentDTO> updateBoardReComment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BoardReCommentDTO boardReCommentDTO
    ) throws URISyntaxException {
        log.debug("REST request to update BoardReComment : {}, {}", id, boardReCommentDTO);
        if (boardReCommentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, boardReCommentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!boardReCommentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BoardReCommentDTO result = boardReCommentService.update(boardReCommentDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, boardReCommentDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /board-re-comments/:id} : Partial updates given fields of an existing boardReComment, field will ignore if it is null
     *
     * @param id the id of the boardReCommentDTO to save.
     * @param boardReCommentDTO the boardReCommentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated boardReCommentDTO,
     * or with status {@code 400 (Bad Request)} if the boardReCommentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the boardReCommentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the boardReCommentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/board-re-comments/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BoardReCommentDTO> partialUpdateBoardReComment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BoardReCommentDTO boardReCommentDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update BoardReComment partially : {}, {}", id, boardReCommentDTO);
        if (boardReCommentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, boardReCommentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!boardReCommentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BoardReCommentDTO> result = boardReCommentService.partialUpdate(boardReCommentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, boardReCommentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /board-re-comments} : get all the boardReComments.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of boardReComments in body.
     */
    @GetMapping("/board-re-comments")
    public List<BoardReCommentDTO> getAllBoardReComments() {
        log.debug("REST request to get all BoardReComments");
        return boardReCommentService.findAll();
    }

    /**
     * {@code GET  /board-re-comments/:id} : get the "id" boardReComment.
     *
     * @param id the id of the boardReCommentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the boardReCommentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/board-re-comments/{id}")
    public ResponseEntity<BoardReCommentDTO> getBoardReComment(@PathVariable Long id) {
        log.debug("REST request to get BoardReComment : {}", id);
        Optional<BoardReCommentDTO> boardReCommentDTO = boardReCommentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(boardReCommentDTO);
    }

    /**
     * {@code DELETE  /board-re-comments/:id} : delete the "id" boardReComment.
     *
     * @param id the id of the boardReCommentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/board-re-comments/{id}")
    public ResponseEntity<Void> deleteBoardReComment(@PathVariable Long id) {
        log.debug("REST request to delete BoardReComment : {}", id);
        boardReCommentService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
