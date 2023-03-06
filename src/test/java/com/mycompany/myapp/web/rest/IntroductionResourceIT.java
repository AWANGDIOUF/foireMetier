package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Introduction;
import com.mycompany.myapp.repository.IntroductionRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link IntroductionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IntroductionResourceIT {

    private static final String DEFAULT_INTRODUCTION = "AAAAAAAAAA";
    private static final String UPDATED_INTRODUCTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/introductions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private IntroductionRepository introductionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIntroductionMockMvc;

    private Introduction introduction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Introduction createEntity(EntityManager em) {
        Introduction introduction = new Introduction().introduction(DEFAULT_INTRODUCTION);
        return introduction;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Introduction createUpdatedEntity(EntityManager em) {
        Introduction introduction = new Introduction().introduction(UPDATED_INTRODUCTION);
        return introduction;
    }

    @BeforeEach
    public void initTest() {
        introduction = createEntity(em);
    }

    @Test
    @Transactional
    void createIntroduction() throws Exception {
        int databaseSizeBeforeCreate = introductionRepository.findAll().size();
        // Create the Introduction
        restIntroductionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(introduction)))
            .andExpect(status().isCreated());

        // Validate the Introduction in the database
        List<Introduction> introductionList = introductionRepository.findAll();
        assertThat(introductionList).hasSize(databaseSizeBeforeCreate + 1);
        Introduction testIntroduction = introductionList.get(introductionList.size() - 1);
        assertThat(testIntroduction.getIntroduction()).isEqualTo(DEFAULT_INTRODUCTION);
    }

    @Test
    @Transactional
    void createIntroductionWithExistingId() throws Exception {
        // Create the Introduction with an existing ID
        introduction.setId(1L);

        int databaseSizeBeforeCreate = introductionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIntroductionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(introduction)))
            .andExpect(status().isBadRequest());

        // Validate the Introduction in the database
        List<Introduction> introductionList = introductionRepository.findAll();
        assertThat(introductionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllIntroductions() throws Exception {
        // Initialize the database
        introductionRepository.saveAndFlush(introduction);

        // Get all the introductionList
        restIntroductionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(introduction.getId().intValue())))
            .andExpect(jsonPath("$.[*].introduction").value(hasItem(DEFAULT_INTRODUCTION)));
    }

    @Test
    @Transactional
    void getIntroduction() throws Exception {
        // Initialize the database
        introductionRepository.saveAndFlush(introduction);

        // Get the introduction
        restIntroductionMockMvc
            .perform(get(ENTITY_API_URL_ID, introduction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(introduction.getId().intValue()))
            .andExpect(jsonPath("$.introduction").value(DEFAULT_INTRODUCTION));
    }

    @Test
    @Transactional
    void getNonExistingIntroduction() throws Exception {
        // Get the introduction
        restIntroductionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewIntroduction() throws Exception {
        // Initialize the database
        introductionRepository.saveAndFlush(introduction);

        int databaseSizeBeforeUpdate = introductionRepository.findAll().size();

        // Update the introduction
        Introduction updatedIntroduction = introductionRepository.findById(introduction.getId()).get();
        // Disconnect from session so that the updates on updatedIntroduction are not directly saved in db
        em.detach(updatedIntroduction);
        updatedIntroduction.introduction(UPDATED_INTRODUCTION);

        restIntroductionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedIntroduction.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedIntroduction))
            )
            .andExpect(status().isOk());

        // Validate the Introduction in the database
        List<Introduction> introductionList = introductionRepository.findAll();
        assertThat(introductionList).hasSize(databaseSizeBeforeUpdate);
        Introduction testIntroduction = introductionList.get(introductionList.size() - 1);
        assertThat(testIntroduction.getIntroduction()).isEqualTo(UPDATED_INTRODUCTION);
    }

    @Test
    @Transactional
    void putNonExistingIntroduction() throws Exception {
        int databaseSizeBeforeUpdate = introductionRepository.findAll().size();
        introduction.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIntroductionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, introduction.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(introduction))
            )
            .andExpect(status().isBadRequest());

        // Validate the Introduction in the database
        List<Introduction> introductionList = introductionRepository.findAll();
        assertThat(introductionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIntroduction() throws Exception {
        int databaseSizeBeforeUpdate = introductionRepository.findAll().size();
        introduction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIntroductionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(introduction))
            )
            .andExpect(status().isBadRequest());

        // Validate the Introduction in the database
        List<Introduction> introductionList = introductionRepository.findAll();
        assertThat(introductionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIntroduction() throws Exception {
        int databaseSizeBeforeUpdate = introductionRepository.findAll().size();
        introduction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIntroductionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(introduction)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Introduction in the database
        List<Introduction> introductionList = introductionRepository.findAll();
        assertThat(introductionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIntroductionWithPatch() throws Exception {
        // Initialize the database
        introductionRepository.saveAndFlush(introduction);

        int databaseSizeBeforeUpdate = introductionRepository.findAll().size();

        // Update the introduction using partial update
        Introduction partialUpdatedIntroduction = new Introduction();
        partialUpdatedIntroduction.setId(introduction.getId());

        partialUpdatedIntroduction.introduction(UPDATED_INTRODUCTION);

        restIntroductionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIntroduction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIntroduction))
            )
            .andExpect(status().isOk());

        // Validate the Introduction in the database
        List<Introduction> introductionList = introductionRepository.findAll();
        assertThat(introductionList).hasSize(databaseSizeBeforeUpdate);
        Introduction testIntroduction = introductionList.get(introductionList.size() - 1);
        assertThat(testIntroduction.getIntroduction()).isEqualTo(UPDATED_INTRODUCTION);
    }

    @Test
    @Transactional
    void fullUpdateIntroductionWithPatch() throws Exception {
        // Initialize the database
        introductionRepository.saveAndFlush(introduction);

        int databaseSizeBeforeUpdate = introductionRepository.findAll().size();

        // Update the introduction using partial update
        Introduction partialUpdatedIntroduction = new Introduction();
        partialUpdatedIntroduction.setId(introduction.getId());

        partialUpdatedIntroduction.introduction(UPDATED_INTRODUCTION);

        restIntroductionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIntroduction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIntroduction))
            )
            .andExpect(status().isOk());

        // Validate the Introduction in the database
        List<Introduction> introductionList = introductionRepository.findAll();
        assertThat(introductionList).hasSize(databaseSizeBeforeUpdate);
        Introduction testIntroduction = introductionList.get(introductionList.size() - 1);
        assertThat(testIntroduction.getIntroduction()).isEqualTo(UPDATED_INTRODUCTION);
    }

    @Test
    @Transactional
    void patchNonExistingIntroduction() throws Exception {
        int databaseSizeBeforeUpdate = introductionRepository.findAll().size();
        introduction.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIntroductionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, introduction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(introduction))
            )
            .andExpect(status().isBadRequest());

        // Validate the Introduction in the database
        List<Introduction> introductionList = introductionRepository.findAll();
        assertThat(introductionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIntroduction() throws Exception {
        int databaseSizeBeforeUpdate = introductionRepository.findAll().size();
        introduction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIntroductionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(introduction))
            )
            .andExpect(status().isBadRequest());

        // Validate the Introduction in the database
        List<Introduction> introductionList = introductionRepository.findAll();
        assertThat(introductionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIntroduction() throws Exception {
        int databaseSizeBeforeUpdate = introductionRepository.findAll().size();
        introduction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIntroductionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(introduction))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Introduction in the database
        List<Introduction> introductionList = introductionRepository.findAll();
        assertThat(introductionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIntroduction() throws Exception {
        // Initialize the database
        introductionRepository.saveAndFlush(introduction);

        int databaseSizeBeforeDelete = introductionRepository.findAll().size();

        // Delete the introduction
        restIntroductionMockMvc
            .perform(delete(ENTITY_API_URL_ID, introduction.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Introduction> introductionList = introductionRepository.findAll();
        assertThat(introductionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
