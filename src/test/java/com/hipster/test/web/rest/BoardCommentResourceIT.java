package com.hipster.test.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hipster.test.IntegrationTest;
import com.hipster.test.domain.BoardComment;
import com.hipster.test.repository.BoardCommentRepository;
import com.hipster.test.service.dto.BoardCommentDTO;
import com.hipster.test.service.mapper.BoardCommentMapper;
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
 * Integration tests for the {@link BoardCommentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BoardCommentResourceIT {

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final Boolean DEFAULT_DEL_AT = false;
    private static final Boolean UPDATED_DEL_AT = true;

    private static final String ENTITY_API_URL = "/api/board-comments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BoardCommentRepository boardCommentRepository;

    @Autowired
    private BoardCommentMapper boardCommentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBoardCommentMockMvc;

    private BoardComment boardComment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BoardComment createEntity(EntityManager em) {
        BoardComment boardComment = new BoardComment().content(DEFAULT_CONTENT).delAt(DEFAULT_DEL_AT);
        return boardComment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BoardComment createUpdatedEntity(EntityManager em) {
        BoardComment boardComment = new BoardComment().content(UPDATED_CONTENT).delAt(UPDATED_DEL_AT);
        return boardComment;
    }

    @BeforeEach
    public void initTest() {
        boardComment = createEntity(em);
    }

    @Test
    @Transactional
    void createBoardComment() throws Exception {
        int databaseSizeBeforeCreate = boardCommentRepository.findAll().size();
        // Create the BoardComment
        BoardCommentDTO boardCommentDTO = boardCommentMapper.toDto(boardComment);
        restBoardCommentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(boardCommentDTO))
            )
            .andExpect(status().isCreated());

        // Validate the BoardComment in the database
        List<BoardComment> boardCommentList = boardCommentRepository.findAll();
        assertThat(boardCommentList).hasSize(databaseSizeBeforeCreate + 1);
        BoardComment testBoardComment = boardCommentList.get(boardCommentList.size() - 1);
        assertThat(testBoardComment.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testBoardComment.getDelAt()).isEqualTo(DEFAULT_DEL_AT);
    }

    @Test
    @Transactional
    void createBoardCommentWithExistingId() throws Exception {
        // Create the BoardComment with an existing ID
        boardComment.setId(1L);
        BoardCommentDTO boardCommentDTO = boardCommentMapper.toDto(boardComment);

        int databaseSizeBeforeCreate = boardCommentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBoardCommentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(boardCommentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BoardComment in the database
        List<BoardComment> boardCommentList = boardCommentRepository.findAll();
        assertThat(boardCommentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBoardComments() throws Exception {
        // Initialize the database
        boardCommentRepository.saveAndFlush(boardComment);

        // Get all the boardCommentList
        restBoardCommentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(boardComment.getId().intValue())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].delAt").value(hasItem(DEFAULT_DEL_AT.booleanValue())));
    }

    @Test
    @Transactional
    void getBoardComment() throws Exception {
        // Initialize the database
        boardCommentRepository.saveAndFlush(boardComment);

        // Get the boardComment
        restBoardCommentMockMvc
            .perform(get(ENTITY_API_URL_ID, boardComment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(boardComment.getId().intValue()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.delAt").value(DEFAULT_DEL_AT.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingBoardComment() throws Exception {
        // Get the boardComment
        restBoardCommentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBoardComment() throws Exception {
        // Initialize the database
        boardCommentRepository.saveAndFlush(boardComment);

        int databaseSizeBeforeUpdate = boardCommentRepository.findAll().size();

        // Update the boardComment
        BoardComment updatedBoardComment = boardCommentRepository.findById(boardComment.getId()).get();
        // Disconnect from session so that the updates on updatedBoardComment are not directly saved in db
        em.detach(updatedBoardComment);
        updatedBoardComment.content(UPDATED_CONTENT).delAt(UPDATED_DEL_AT);
        BoardCommentDTO boardCommentDTO = boardCommentMapper.toDto(updatedBoardComment);

        restBoardCommentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, boardCommentDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(boardCommentDTO))
            )
            .andExpect(status().isOk());

        // Validate the BoardComment in the database
        List<BoardComment> boardCommentList = boardCommentRepository.findAll();
        assertThat(boardCommentList).hasSize(databaseSizeBeforeUpdate);
        BoardComment testBoardComment = boardCommentList.get(boardCommentList.size() - 1);
        assertThat(testBoardComment.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testBoardComment.getDelAt()).isEqualTo(UPDATED_DEL_AT);
    }

    @Test
    @Transactional
    void putNonExistingBoardComment() throws Exception {
        int databaseSizeBeforeUpdate = boardCommentRepository.findAll().size();
        boardComment.setId(count.incrementAndGet());

        // Create the BoardComment
        BoardCommentDTO boardCommentDTO = boardCommentMapper.toDto(boardComment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBoardCommentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, boardCommentDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(boardCommentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BoardComment in the database
        List<BoardComment> boardCommentList = boardCommentRepository.findAll();
        assertThat(boardCommentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBoardComment() throws Exception {
        int databaseSizeBeforeUpdate = boardCommentRepository.findAll().size();
        boardComment.setId(count.incrementAndGet());

        // Create the BoardComment
        BoardCommentDTO boardCommentDTO = boardCommentMapper.toDto(boardComment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBoardCommentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(boardCommentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BoardComment in the database
        List<BoardComment> boardCommentList = boardCommentRepository.findAll();
        assertThat(boardCommentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBoardComment() throws Exception {
        int databaseSizeBeforeUpdate = boardCommentRepository.findAll().size();
        boardComment.setId(count.incrementAndGet());

        // Create the BoardComment
        BoardCommentDTO boardCommentDTO = boardCommentMapper.toDto(boardComment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBoardCommentMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(boardCommentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BoardComment in the database
        List<BoardComment> boardCommentList = boardCommentRepository.findAll();
        assertThat(boardCommentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBoardCommentWithPatch() throws Exception {
        // Initialize the database
        boardCommentRepository.saveAndFlush(boardComment);

        int databaseSizeBeforeUpdate = boardCommentRepository.findAll().size();

        // Update the boardComment using partial update
        BoardComment partialUpdatedBoardComment = new BoardComment();
        partialUpdatedBoardComment.setId(boardComment.getId());

        partialUpdatedBoardComment.content(UPDATED_CONTENT).delAt(UPDATED_DEL_AT);

        restBoardCommentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBoardComment.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBoardComment))
            )
            .andExpect(status().isOk());

        // Validate the BoardComment in the database
        List<BoardComment> boardCommentList = boardCommentRepository.findAll();
        assertThat(boardCommentList).hasSize(databaseSizeBeforeUpdate);
        BoardComment testBoardComment = boardCommentList.get(boardCommentList.size() - 1);
        assertThat(testBoardComment.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testBoardComment.getDelAt()).isEqualTo(UPDATED_DEL_AT);
    }

    @Test
    @Transactional
    void fullUpdateBoardCommentWithPatch() throws Exception {
        // Initialize the database
        boardCommentRepository.saveAndFlush(boardComment);

        int databaseSizeBeforeUpdate = boardCommentRepository.findAll().size();

        // Update the boardComment using partial update
        BoardComment partialUpdatedBoardComment = new BoardComment();
        partialUpdatedBoardComment.setId(boardComment.getId());

        partialUpdatedBoardComment.content(UPDATED_CONTENT).delAt(UPDATED_DEL_AT);

        restBoardCommentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBoardComment.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBoardComment))
            )
            .andExpect(status().isOk());

        // Validate the BoardComment in the database
        List<BoardComment> boardCommentList = boardCommentRepository.findAll();
        assertThat(boardCommentList).hasSize(databaseSizeBeforeUpdate);
        BoardComment testBoardComment = boardCommentList.get(boardCommentList.size() - 1);
        assertThat(testBoardComment.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testBoardComment.getDelAt()).isEqualTo(UPDATED_DEL_AT);
    }

    @Test
    @Transactional
    void patchNonExistingBoardComment() throws Exception {
        int databaseSizeBeforeUpdate = boardCommentRepository.findAll().size();
        boardComment.setId(count.incrementAndGet());

        // Create the BoardComment
        BoardCommentDTO boardCommentDTO = boardCommentMapper.toDto(boardComment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBoardCommentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, boardCommentDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(boardCommentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BoardComment in the database
        List<BoardComment> boardCommentList = boardCommentRepository.findAll();
        assertThat(boardCommentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBoardComment() throws Exception {
        int databaseSizeBeforeUpdate = boardCommentRepository.findAll().size();
        boardComment.setId(count.incrementAndGet());

        // Create the BoardComment
        BoardCommentDTO boardCommentDTO = boardCommentMapper.toDto(boardComment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBoardCommentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(boardCommentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BoardComment in the database
        List<BoardComment> boardCommentList = boardCommentRepository.findAll();
        assertThat(boardCommentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBoardComment() throws Exception {
        int databaseSizeBeforeUpdate = boardCommentRepository.findAll().size();
        boardComment.setId(count.incrementAndGet());

        // Create the BoardComment
        BoardCommentDTO boardCommentDTO = boardCommentMapper.toDto(boardComment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBoardCommentMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(boardCommentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BoardComment in the database
        List<BoardComment> boardCommentList = boardCommentRepository.findAll();
        assertThat(boardCommentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBoardComment() throws Exception {
        // Initialize the database
        boardCommentRepository.saveAndFlush(boardComment);

        int databaseSizeBeforeDelete = boardCommentRepository.findAll().size();

        // Delete the boardComment
        restBoardCommentMockMvc
            .perform(delete(ENTITY_API_URL_ID, boardComment.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BoardComment> boardCommentList = boardCommentRepository.findAll();
        assertThat(boardCommentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
