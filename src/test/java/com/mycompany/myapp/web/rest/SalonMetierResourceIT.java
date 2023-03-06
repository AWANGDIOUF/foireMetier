package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.SalonMetier;
import com.mycompany.myapp.repository.SalonMetierRepository;
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
 * Integration tests for the {@link SalonMetierResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SalonMetierResourceIT {

    private static final String DEFAULT_SALON_METIER = "AAAAAAAAAA";
    private static final String UPDATED_SALON_METIER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/salon-metiers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SalonMetierRepository salonMetierRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSalonMetierMockMvc;

    private SalonMetier salonMetier;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SalonMetier createEntity(EntityManager em) {
        SalonMetier salonMetier = new SalonMetier().salonMetier(DEFAULT_SALON_METIER);
        return salonMetier;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SalonMetier createUpdatedEntity(EntityManager em) {
        SalonMetier salonMetier = new SalonMetier().salonMetier(UPDATED_SALON_METIER);
        return salonMetier;
    }

    @BeforeEach
    public void initTest() {
        salonMetier = createEntity(em);
    }

    @Test
    @Transactional
    void createSalonMetier() throws Exception {
        int databaseSizeBeforeCreate = salonMetierRepository.findAll().size();
        // Create the SalonMetier
        restSalonMetierMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(salonMetier)))
            .andExpect(status().isCreated());

        // Validate the SalonMetier in the database
        List<SalonMetier> salonMetierList = salonMetierRepository.findAll();
        assertThat(salonMetierList).hasSize(databaseSizeBeforeCreate + 1);
        SalonMetier testSalonMetier = salonMetierList.get(salonMetierList.size() - 1);
        assertThat(testSalonMetier.getSalonMetier()).isEqualTo(DEFAULT_SALON_METIER);
    }

    @Test
    @Transactional
    void createSalonMetierWithExistingId() throws Exception {
        // Create the SalonMetier with an existing ID
        salonMetier.setId(1L);

        int databaseSizeBeforeCreate = salonMetierRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSalonMetierMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(salonMetier)))
            .andExpect(status().isBadRequest());

        // Validate the SalonMetier in the database
        List<SalonMetier> salonMetierList = salonMetierRepository.findAll();
        assertThat(salonMetierList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSalonMetiers() throws Exception {
        // Initialize the database
        salonMetierRepository.saveAndFlush(salonMetier);

        // Get all the salonMetierList
        restSalonMetierMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(salonMetier.getId().intValue())))
            .andExpect(jsonPath("$.[*].salonMetier").value(hasItem(DEFAULT_SALON_METIER)));
    }

    @Test
    @Transactional
    void getSalonMetier() throws Exception {
        // Initialize the database
        salonMetierRepository.saveAndFlush(salonMetier);

        // Get the salonMetier
        restSalonMetierMockMvc
            .perform(get(ENTITY_API_URL_ID, salonMetier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(salonMetier.getId().intValue()))
            .andExpect(jsonPath("$.salonMetier").value(DEFAULT_SALON_METIER));
    }

    @Test
    @Transactional
    void getNonExistingSalonMetier() throws Exception {
        // Get the salonMetier
        restSalonMetierMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSalonMetier() throws Exception {
        // Initialize the database
        salonMetierRepository.saveAndFlush(salonMetier);

        int databaseSizeBeforeUpdate = salonMetierRepository.findAll().size();

        // Update the salonMetier
        SalonMetier updatedSalonMetier = salonMetierRepository.findById(salonMetier.getId()).get();
        // Disconnect from session so that the updates on updatedSalonMetier are not directly saved in db
        em.detach(updatedSalonMetier);
        updatedSalonMetier.salonMetier(UPDATED_SALON_METIER);

        restSalonMetierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSalonMetier.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSalonMetier))
            )
            .andExpect(status().isOk());

        // Validate the SalonMetier in the database
        List<SalonMetier> salonMetierList = salonMetierRepository.findAll();
        assertThat(salonMetierList).hasSize(databaseSizeBeforeUpdate);
        SalonMetier testSalonMetier = salonMetierList.get(salonMetierList.size() - 1);
        assertThat(testSalonMetier.getSalonMetier()).isEqualTo(UPDATED_SALON_METIER);
    }

    @Test
    @Transactional
    void putNonExistingSalonMetier() throws Exception {
        int databaseSizeBeforeUpdate = salonMetierRepository.findAll().size();
        salonMetier.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSalonMetierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, salonMetier.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(salonMetier))
            )
            .andExpect(status().isBadRequest());

        // Validate the SalonMetier in the database
        List<SalonMetier> salonMetierList = salonMetierRepository.findAll();
        assertThat(salonMetierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSalonMetier() throws Exception {
        int databaseSizeBeforeUpdate = salonMetierRepository.findAll().size();
        salonMetier.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSalonMetierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(salonMetier))
            )
            .andExpect(status().isBadRequest());

        // Validate the SalonMetier in the database
        List<SalonMetier> salonMetierList = salonMetierRepository.findAll();
        assertThat(salonMetierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSalonMetier() throws Exception {
        int databaseSizeBeforeUpdate = salonMetierRepository.findAll().size();
        salonMetier.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSalonMetierMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(salonMetier)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SalonMetier in the database
        List<SalonMetier> salonMetierList = salonMetierRepository.findAll();
        assertThat(salonMetierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSalonMetierWithPatch() throws Exception {
        // Initialize the database
        salonMetierRepository.saveAndFlush(salonMetier);

        int databaseSizeBeforeUpdate = salonMetierRepository.findAll().size();

        // Update the salonMetier using partial update
        SalonMetier partialUpdatedSalonMetier = new SalonMetier();
        partialUpdatedSalonMetier.setId(salonMetier.getId());

        restSalonMetierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSalonMetier.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSalonMetier))
            )
            .andExpect(status().isOk());

        // Validate the SalonMetier in the database
        List<SalonMetier> salonMetierList = salonMetierRepository.findAll();
        assertThat(salonMetierList).hasSize(databaseSizeBeforeUpdate);
        SalonMetier testSalonMetier = salonMetierList.get(salonMetierList.size() - 1);
        assertThat(testSalonMetier.getSalonMetier()).isEqualTo(DEFAULT_SALON_METIER);
    }

    @Test
    @Transactional
    void fullUpdateSalonMetierWithPatch() throws Exception {
        // Initialize the database
        salonMetierRepository.saveAndFlush(salonMetier);

        int databaseSizeBeforeUpdate = salonMetierRepository.findAll().size();

        // Update the salonMetier using partial update
        SalonMetier partialUpdatedSalonMetier = new SalonMetier();
        partialUpdatedSalonMetier.setId(salonMetier.getId());

        partialUpdatedSalonMetier.salonMetier(UPDATED_SALON_METIER);

        restSalonMetierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSalonMetier.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSalonMetier))
            )
            .andExpect(status().isOk());

        // Validate the SalonMetier in the database
        List<SalonMetier> salonMetierList = salonMetierRepository.findAll();
        assertThat(salonMetierList).hasSize(databaseSizeBeforeUpdate);
        SalonMetier testSalonMetier = salonMetierList.get(salonMetierList.size() - 1);
        assertThat(testSalonMetier.getSalonMetier()).isEqualTo(UPDATED_SALON_METIER);
    }

    @Test
    @Transactional
    void patchNonExistingSalonMetier() throws Exception {
        int databaseSizeBeforeUpdate = salonMetierRepository.findAll().size();
        salonMetier.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSalonMetierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, salonMetier.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(salonMetier))
            )
            .andExpect(status().isBadRequest());

        // Validate the SalonMetier in the database
        List<SalonMetier> salonMetierList = salonMetierRepository.findAll();
        assertThat(salonMetierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSalonMetier() throws Exception {
        int databaseSizeBeforeUpdate = salonMetierRepository.findAll().size();
        salonMetier.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSalonMetierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(salonMetier))
            )
            .andExpect(status().isBadRequest());

        // Validate the SalonMetier in the database
        List<SalonMetier> salonMetierList = salonMetierRepository.findAll();
        assertThat(salonMetierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSalonMetier() throws Exception {
        int databaseSizeBeforeUpdate = salonMetierRepository.findAll().size();
        salonMetier.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSalonMetierMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(salonMetier))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SalonMetier in the database
        List<SalonMetier> salonMetierList = salonMetierRepository.findAll();
        assertThat(salonMetierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSalonMetier() throws Exception {
        // Initialize the database
        salonMetierRepository.saveAndFlush(salonMetier);

        int databaseSizeBeforeDelete = salonMetierRepository.findAll().size();

        // Delete the salonMetier
        restSalonMetierMockMvc
            .perform(delete(ENTITY_API_URL_ID, salonMetier.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SalonMetier> salonMetierList = salonMetierRepository.findAll();
        assertThat(salonMetierList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
