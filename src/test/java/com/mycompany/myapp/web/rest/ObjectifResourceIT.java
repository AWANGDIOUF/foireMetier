package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Objectif;
import com.mycompany.myapp.repository.ObjectifRepository;
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
 * Integration tests for the {@link ObjectifResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ObjectifResourceIT {

    private static final String DEFAULT_OBJECTIF = "AAAAAAAAAA";
    private static final String UPDATED_OBJECTIF = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/objectifs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectifRepository objectifRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restObjectifMockMvc;

    private Objectif objectif;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Objectif createEntity(EntityManager em) {
        Objectif objectif = new Objectif().objectif(DEFAULT_OBJECTIF);
        return objectif;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Objectif createUpdatedEntity(EntityManager em) {
        Objectif objectif = new Objectif().objectif(UPDATED_OBJECTIF);
        return objectif;
    }

    @BeforeEach
    public void initTest() {
        objectif = createEntity(em);
    }

    @Test
    @Transactional
    void createObjectif() throws Exception {
        int databaseSizeBeforeCreate = objectifRepository.findAll().size();
        // Create the Objectif
        restObjectifMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(objectif)))
            .andExpect(status().isCreated());

        // Validate the Objectif in the database
        List<Objectif> objectifList = objectifRepository.findAll();
        assertThat(objectifList).hasSize(databaseSizeBeforeCreate + 1);
        Objectif testObjectif = objectifList.get(objectifList.size() - 1);
        assertThat(testObjectif.getObjectif()).isEqualTo(DEFAULT_OBJECTIF);
    }

    @Test
    @Transactional
    void createObjectifWithExistingId() throws Exception {
        // Create the Objectif with an existing ID
        objectif.setId(1L);

        int databaseSizeBeforeCreate = objectifRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restObjectifMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(objectif)))
            .andExpect(status().isBadRequest());

        // Validate the Objectif in the database
        List<Objectif> objectifList = objectifRepository.findAll();
        assertThat(objectifList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllObjectifs() throws Exception {
        // Initialize the database
        objectifRepository.saveAndFlush(objectif);

        // Get all the objectifList
        restObjectifMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(objectif.getId().intValue())))
            .andExpect(jsonPath("$.[*].objectif").value(hasItem(DEFAULT_OBJECTIF)));
    }

    @Test
    @Transactional
    void getObjectif() throws Exception {
        // Initialize the database
        objectifRepository.saveAndFlush(objectif);

        // Get the objectif
        restObjectifMockMvc
            .perform(get(ENTITY_API_URL_ID, objectif.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(objectif.getId().intValue()))
            .andExpect(jsonPath("$.objectif").value(DEFAULT_OBJECTIF));
    }

    @Test
    @Transactional
    void getNonExistingObjectif() throws Exception {
        // Get the objectif
        restObjectifMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewObjectif() throws Exception {
        // Initialize the database
        objectifRepository.saveAndFlush(objectif);

        int databaseSizeBeforeUpdate = objectifRepository.findAll().size();

        // Update the objectif
        Objectif updatedObjectif = objectifRepository.findById(objectif.getId()).get();
        // Disconnect from session so that the updates on updatedObjectif are not directly saved in db
        em.detach(updatedObjectif);
        updatedObjectif.objectif(UPDATED_OBJECTIF);

        restObjectifMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedObjectif.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedObjectif))
            )
            .andExpect(status().isOk());

        // Validate the Objectif in the database
        List<Objectif> objectifList = objectifRepository.findAll();
        assertThat(objectifList).hasSize(databaseSizeBeforeUpdate);
        Objectif testObjectif = objectifList.get(objectifList.size() - 1);
        assertThat(testObjectif.getObjectif()).isEqualTo(UPDATED_OBJECTIF);
    }

    @Test
    @Transactional
    void putNonExistingObjectif() throws Exception {
        int databaseSizeBeforeUpdate = objectifRepository.findAll().size();
        objectif.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restObjectifMockMvc
            .perform(
                put(ENTITY_API_URL_ID, objectif.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(objectif))
            )
            .andExpect(status().isBadRequest());

        // Validate the Objectif in the database
        List<Objectif> objectifList = objectifRepository.findAll();
        assertThat(objectifList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchObjectif() throws Exception {
        int databaseSizeBeforeUpdate = objectifRepository.findAll().size();
        objectif.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restObjectifMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(objectif))
            )
            .andExpect(status().isBadRequest());

        // Validate the Objectif in the database
        List<Objectif> objectifList = objectifRepository.findAll();
        assertThat(objectifList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamObjectif() throws Exception {
        int databaseSizeBeforeUpdate = objectifRepository.findAll().size();
        objectif.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restObjectifMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(objectif)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Objectif in the database
        List<Objectif> objectifList = objectifRepository.findAll();
        assertThat(objectifList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateObjectifWithPatch() throws Exception {
        // Initialize the database
        objectifRepository.saveAndFlush(objectif);

        int databaseSizeBeforeUpdate = objectifRepository.findAll().size();

        // Update the objectif using partial update
        Objectif partialUpdatedObjectif = new Objectif();
        partialUpdatedObjectif.setId(objectif.getId());

        restObjectifMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedObjectif.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedObjectif))
            )
            .andExpect(status().isOk());

        // Validate the Objectif in the database
        List<Objectif> objectifList = objectifRepository.findAll();
        assertThat(objectifList).hasSize(databaseSizeBeforeUpdate);
        Objectif testObjectif = objectifList.get(objectifList.size() - 1);
        assertThat(testObjectif.getObjectif()).isEqualTo(DEFAULT_OBJECTIF);
    }

    @Test
    @Transactional
    void fullUpdateObjectifWithPatch() throws Exception {
        // Initialize the database
        objectifRepository.saveAndFlush(objectif);

        int databaseSizeBeforeUpdate = objectifRepository.findAll().size();

        // Update the objectif using partial update
        Objectif partialUpdatedObjectif = new Objectif();
        partialUpdatedObjectif.setId(objectif.getId());

        partialUpdatedObjectif.objectif(UPDATED_OBJECTIF);

        restObjectifMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedObjectif.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedObjectif))
            )
            .andExpect(status().isOk());

        // Validate the Objectif in the database
        List<Objectif> objectifList = objectifRepository.findAll();
        assertThat(objectifList).hasSize(databaseSizeBeforeUpdate);
        Objectif testObjectif = objectifList.get(objectifList.size() - 1);
        assertThat(testObjectif.getObjectif()).isEqualTo(UPDATED_OBJECTIF);
    }

    @Test
    @Transactional
    void patchNonExistingObjectif() throws Exception {
        int databaseSizeBeforeUpdate = objectifRepository.findAll().size();
        objectif.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restObjectifMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, objectif.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(objectif))
            )
            .andExpect(status().isBadRequest());

        // Validate the Objectif in the database
        List<Objectif> objectifList = objectifRepository.findAll();
        assertThat(objectifList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchObjectif() throws Exception {
        int databaseSizeBeforeUpdate = objectifRepository.findAll().size();
        objectif.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restObjectifMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(objectif))
            )
            .andExpect(status().isBadRequest());

        // Validate the Objectif in the database
        List<Objectif> objectifList = objectifRepository.findAll();
        assertThat(objectifList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamObjectif() throws Exception {
        int databaseSizeBeforeUpdate = objectifRepository.findAll().size();
        objectif.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restObjectifMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(objectif)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Objectif in the database
        List<Objectif> objectifList = objectifRepository.findAll();
        assertThat(objectifList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteObjectif() throws Exception {
        // Initialize the database
        objectifRepository.saveAndFlush(objectif);

        int databaseSizeBeforeDelete = objectifRepository.findAll().size();

        // Delete the objectif
        restObjectifMockMvc
            .perform(delete(ENTITY_API_URL_ID, objectif.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Objectif> objectifList = objectifRepository.findAll();
        assertThat(objectifList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
