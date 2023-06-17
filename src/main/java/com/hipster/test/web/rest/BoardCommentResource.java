package com.hipster.test.web.rest;

import com.hipster.test.repository.BoardCommentRepository;
import com.hipster.test.service.BoardCommentService;
import com.hipster.test.service.dto.BoardCommentDTO;
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
 * REST controller for managing {@link com.hipster.test.domain.BoardComment}.
 */
@RestController
@RequestMapping("/api")
public class BoardCommentResource {

    private final Logger log = LoggerFactory.getLogger(BoardCommentResource.class);

    private static final String ENTITY_NAME = "boardComment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BoardCommentService boardCommentService;

    private final BoardCommentRepository boardCommentRepository;

    public BoardCommentResource(BoardCommentService boardCommentService, BoardCommentRepository boardCommentRepository) {
        this.boardCommentService = boardCommentService;
        this.boardCommentRepository = boardCommentRepository;
    }

    /**
     * {@code POST  /board-comments} : Create a new boardComment.
     *
     * @param boardCommentDTO the boardCommentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new boardCommentDTO, or with status {@code 400 (Bad Request)} if the boardComment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/board-comments")
    public ResponseEntity<BoardCommentDTO> createBoardComment(@Valid @RequestBody BoardCommentDTO boardCommentDTO)
        throws URISyntaxException {
        log.debug("REST request to save BoardComment : {}", boardCommentDTO);
        if (boardCommentDTO.getId() != null) {
            throw new BadRequestAlertException("A new boardComment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BoardCommentDTO result = boardCommentService.save(boardCommentDTO);
        return ResponseEntity
            .created(new URI("/api/board-comments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /board-comments/:id} : Updates an existing boardComment.
     *
     * @param id the id of the boardCommentDTO to save.
     * @param boardCommentDTO the boardCommentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated boardCommentDTO,
     * or with status {@code 400 (Bad Request)} if the boardCommentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the boardCommentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/board-comments/{id}")
    public ResponseEntity<BoardCommentDTO> updateBoardComment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BoardCommentDTO boardCommentDTO
    ) throws URISyntaxException {
        log.debug("REST request to update BoardComment : {}, {}", id, boardCommentDTO);
        if (boardCommentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, boardCommentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!boardCommentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BoardCommentDTO result = boardCommentService.update(boardCommentDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, boardCommentDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /board-comments/:id} : Partial updates given fields of an existing boardComment, field will ignore if it is null
     *
     * @param id the id of the boardCommentDTO to save.
     * @param boardCommentDTO the boardCommentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated boardCommentDTO,
     * or with status {@code 400 (Bad Request)} if the boardCommentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the boardCommentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the boardCommentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/board-comments/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BoardCommentDTO> partialUpdateBoardComment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BoardCommentDTO boardCommentDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update BoardComment partially : {}, {}", id, boardCommentDTO);
        if (boardCommentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, boardCommentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!boardCommentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BoardCommentDTO> result = boardCommentService.partialUpdate(boardCommentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, boardCommentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /board-comments} : get all the boardComments.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of boardComments in body.
     */
    @GetMapping("/board-comments")
    public List<BoardCommentDTO> getAllBoardComments() {
        log.debug("REST request to get all BoardComments");
        return boardCommentService.findAll();
    }

    /**
     * {@code GET  /board-comments/:id} : get the "id" boardComment.
     *
     * @param id the id of the boardCommentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the boardCommentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/board-comments/{id}")
    public ResponseEntity<BoardCommentDTO> getBoardComment(@PathVariable Long id) {
        log.debug("REST request to get BoardComment : {}", id);
        Optional<BoardCommentDTO> boardCommentDTO = boardCommentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(boardCommentDTO);
    }

    /**
     * {@code DELETE  /board-comments/:id} : delete the "id" boardComment.
     *
     * @param id the id of the boardCommentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/board-comments/{id}")
    public ResponseEntity<Void> deleteBoardComment(@PathVariable Long id) {
        log.debug("REST request to delete BoardComment : {}", id);
        boardCommentService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
