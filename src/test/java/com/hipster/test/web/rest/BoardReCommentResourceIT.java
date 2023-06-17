package com.hipster.test.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hipster.test.IntegrationTest;
import com.hipster.test.domain.BoardReComment;
import com.hipster.test.repository.BoardReCommentRepository;
import com.hipster.test.service.dto.BoardReCommentDTO;
import com.hipster.test.service.mapper.BoardReCommentMapper;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link BoardReCommentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BoardReCommentResourceIT {

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final Boolean DEFAULT_DEL_AT = false;
    private static final Boolean UPDATED_DEL_AT = true;

    private static final String ENTITY_API_URL = "/api/board-re-comments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BoardReCommentRepository boardReCommentRepository;

    @Autowired
    private BoardReCommentMapper boardReCommentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBoardReCommentMockMvc;

    private BoardReComment boardReComment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BoardReComment createEntity(EntityManager em) {
        BoardReComment boardReComment = new BoardReComment().content(DEFAULT_CONTENT).delAt(DEFAULT_DEL_AT);
        return boardReComment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BoardReComment createUpdatedEntity(EntityManager em) {
        BoardReComment boardReComment = new BoardReComment().content(UPDATED_CONTENT).delAt(UPDATED_DEL_AT);
        return boardReComment;
    }

    @BeforeEach
    public void initTest() {
        boardReComment = createEntity(em);
    }

    @Test
    @Transactional
    void createBoardReComment() throws Exception {
        int databaseSizeBeforeCreate = boardReCommentRepository.findAll().size();
        // Create the BoardReComment
        BoardReCommentDTO boardReCommentDTO = boardReCommentMapper.toDto(boardReComment);
        restBoardReCommentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(boardReCommentDTO))
            )
            .andExpect(status().isCreated());

        // Validate the BoardReComment in the database
        List<BoardReComment> boardReCommentList = boardReCommentRepository.findAll();
        assertThat(boardReCommentList).hasSize(databaseSizeBeforeCreate + 1);
        BoardReComment testBoardReComment = boardReCommentList.get(boardReCommentList.size() - 1);
        assertThat(testBoardReComment.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testBoardReComment.getDelAt()).isEqualTo(DEFAULT_DEL_AT);
    }

    @Test
    @Transactional
    void createBoardReCommentWithExistingId() throws Exception {
        // Create the BoardReComment with an existing ID
        boardReComment.setId(1L);
        BoardReCommentDTO boardReCommentDTO = boardReCommentMapper.toDto(boardReComment);

        int databaseSizeBeforeCreate = boardReCommentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBoardReCommentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(boardReCommentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BoardReComment in the database
        List<BoardReComment> boardReCommentList = boardReCommentRepository.findAll();
        assertThat(boardReCommentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBoardReComments() throws Exception {
        // Initialize the database
        boardReCommentRepository.saveAndFlush(boardReComment);

        // Get all the boardReCommentList
        restBoardReCommentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(boardReComment.getId().intValue())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].delAt").value(hasItem(DEFAULT_DEL_AT.booleanValue())));
    }

    @Test
    @Transactional
    void getBoardReComment() throws Exception {
        // Initialize the database
        boardReCommentRepository.saveAndFlush(boardReComment);

        // Get the boardReComment
        restBoardReCommentMockMvc
            .perform(get(ENTITY_API_URL_ID, boardReComment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(boardReComment.getId().intValue()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.delAt").value(DEFAULT_DEL_AT.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingBoardReComment() throws Exception {
        // Get the boardReComment
        restBoardReCommentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBoardReComment() throws Exception {
        // Initialize the database
        boardReCommentRepository.saveAndFlush(boardReComment);

        int databaseSizeBeforeUpdate = boardReCommentRepository.findAll().size();

        // Update the boardReComment
        BoardReComment updatedBoardReComment = boardReCommentRepository.findById(boardReComment.getId()).get();
        // Disconnect from session so that the updates on updatedBoardReComment are not directly saved in db
        em.detach(updatedBoardReComment);
        updatedBoardReComment.content(UPDATED_CONTENT).delAt(UPDATED_DEL_AT);
        BoardReCommentDTO boardReCommentDTO = boardReCommentMapper.toDto(updatedBoardReComment);

        restBoardReCommentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, boardReCommentDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(boardReCommentDTO))
            )
            .andExpect(status().isOk());

        // Validate the BoardReComment in the database
        List<BoardReComment> boardReCommentList = boardReCommentRepository.findAll();
        assertThat(boardReCommentList).hasSize(databaseSizeBeforeUpdate);
        BoardReComment testBoardReComment = boardReCommentList.get(boardReCommentList.size() - 1);
        assertThat(testBoardReComment.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testBoardReComment.getDelAt()).isEqualTo(UPDATED_DEL_AT);
    }

    @Test
    @Transactional
    void putNonExistingBoardReComment() throws Exception {
        int databaseSizeBeforeUpdate = boardReCommentRepository.findAll().size();
        boardReComment.setId(count.incrementAndGet());

        // Create the BoardReComment
        BoardReCommentDTO boardReCommentDTO = boardReCommentMapper.toDto(boardReComment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBoardReCommentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, boardReCommentDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(boardReCommentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BoardReComment in the database
        List<BoardReComment> boardReCommentList = boardReCommentRepository.findAll();
        assertThat(boardReCommentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBoardReComment() throws Exception {
        int databaseSizeBeforeUpdate = boardReCommentRepository.findAll().size();
        boardReComment.setId(count.incrementAndGet());

        // Create the BoardReComment
        BoardReCommentDTO boardReCommentDTO = boardReCommentMapper.toDto(boardReComment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBoardReCommentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(boardReCommentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BoardReComment in the database
        List<BoardReComment> boardReCommentList = boardReCommentRepository.findAll();
        assertThat(boardReCommentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBoardReComment() throws Exception {
        int databaseSizeBeforeUpdate = boardReCommentRepository.findAll().size();
        boardReComment.setId(count.incrementAndGet());

        // Create the BoardReComment
        BoardReCommentDTO boardReCommentDTO = boardReCommentMapper.toDto(boardReComment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBoardReCommentMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(boardReCommentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BoardReComment in the database
        List<BoardReComment> boardReCommentList = boardReCommentRepository.findAll();
        assertThat(boardReCommentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBoardReCommentWithPatch() throws Exception {
        // Initialize the database
        boardReCommentRepository.saveAndFlush(boardReComment);

        int databaseSizeBeforeUpdate = boardReCommentRepository.findAll().size();

        // Update the boardReComment using partial update
        BoardReComment partialUpdatedBoardReComment = new BoardReComment();
        partialUpdatedBoardReComment.setId(boardReComment.getId());

        partialUpdatedBoardReComment.content(UPDATED_CONTENT);

        restBoardReCommentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBoardReComment.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBoardReComment))
            )
            .andExpect(status().isOk());

        // Validate the BoardReComment in the database
        List<BoardReComment> boardReCommentList = boardReCommentRepository.findAll();
        assertThat(boardReCommentList).hasSize(databaseSizeBeforeUpdate);
        BoardReComment testBoardReComment = boardReCommentList.get(boardReCommentList.size() - 1);
        assertThat(testBoardReComment.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testBoardReComment.getDelAt()).isEqualTo(DEFAULT_DEL_AT);
    }

    @Test
    @Transactional
    void fullUpdateBoardReCommentWithPatch() throws Exception {
        // Initialize the database
        boardReCommentRepository.saveAndFlush(boardReComment);

        int databaseSizeBeforeUpdate = boardReCommentRepository.findAll().size();

        // Update the boardReComment using partial update
        BoardReComment partialUpdatedBoardReComment = new BoardReComment();
        partialUpdatedBoardReComment.setId(boardReComment.getId());

        partialUpdatedBoardReComment.content(UPDATED_CONTENT).delAt(UPDATED_DEL_AT);

        restBoardReCommentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBoardReComment.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBoardReComment))
            )
            .andExpect(status().isOk());

        // Validate the BoardReComment in the database
        List<BoardReComment> boardReCommentList = boardReCommentRepository.findAll();
        assertThat(boardReCommentList).hasSize(databaseSizeBeforeUpdate);
        BoardReComment testBoardReComment = boardReCommentList.get(boardReCommentList.size() - 1);
        assertThat(testBoardReComment.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testBoardReComment.getDelAt()).isEqualTo(UPDATED_DEL_AT);
    }

    @Test
    @Transactional
    void patchNonExistingBoardReComment() throws Exception {
        int databaseSizeBeforeUpdate = boardReCommentRepository.findAll().size();
        boardReComment.setId(count.incrementAndGet());

        // Create the BoardReComment
        BoardReCommentDTO boardReCommentDTO = boardReCommentMapper.toDto(boardReComment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBoardReCommentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, boardReCommentDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(boardReCommentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BoardReComment in the database
        List<BoardReComment> boardReCommentList = boardReCommentRepository.findAll();
        assertThat(boardReCommentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBoardReComment() throws Exception {
        int databaseSizeBeforeUpdate = boardReCommentRepository.findAll().size();
        boardReComment.setId(count.incrementAndGet());

        // Create the BoardReComment
        BoardReCommentDTO boardReCommentDTO = boardReCommentMapper.toDto(boardReComment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBoardReCommentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(boardReCommentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BoardReComment in the database
        List<BoardReComment> boardReCommentList = boardReCommentRepository.findAll();
        assertThat(boardReCommentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBoardReComment() throws Exception {
        int databaseSizeBeforeUpdate = boardReCommentRepository.findAll().size();
        boardReComment.setId(count.incrementAndGet());

        // Create the BoardReComment
        BoardReCommentDTO boardReCommentDTO = boardReCommentMapper.toDto(boardReComment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBoardReCommentMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(boardReCommentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BoardReComment in the database
        List<BoardReComment> boardReCommentList = boardReCommentRepository.findAll();
        assertThat(boardReCommentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBoardReComment() throws Exception {
        // Initialize the database
        boardReCommentRepository.saveAndFlush(boardReComment);

        int databaseSizeBeforeDelete = boardReCommentRepository.findAll().size();

        // Delete the boardReComment
        restBoardReCommentMockMvc
            .perform(delete(ENTITY_API_URL_ID, boardReComment.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BoardReComment> boardReCommentList = boardReCommentRepository.findAll();
        assertThat(boardReCommentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
