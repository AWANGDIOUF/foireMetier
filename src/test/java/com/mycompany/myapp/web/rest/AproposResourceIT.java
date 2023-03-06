package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Apropos;
import com.mycompany.myapp.repository.AproposRepository;
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
 * Integration tests for the {@link AproposResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AproposResourceIT {

    private static final String DEFAULT_TEXTE = "AAAAAAAAAA";
    private static final String UPDATED_TEXTE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/apropos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AproposRepository aproposRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAproposMockMvc;

    private Apropos apropos;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Apropos createEntity(EntityManager em) {
        Apropos apropos = new Apropos().texte(DEFAULT_TEXTE);
        return apropos;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Apropos createUpdatedEntity(EntityManager em) {
        Apropos apropos = new Apropos().texte(UPDATED_TEXTE);
        return apropos;
    }

    @BeforeEach
    public void initTest() {
        apropos = createEntity(em);
    }

    @Test
    @Transactional
    void createApropos() throws Exception {
        int databaseSizeBeforeCreate = aproposRepository.findAll().size();
        // Create the Apropos
        restAproposMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apropos)))
            .andExpect(status().isCreated());

        // Validate the Apropos in the database
        List<Apropos> aproposList = aproposRepository.findAll();
        assertThat(aproposList).hasSize(databaseSizeBeforeCreate + 1);
        Apropos testApropos = aproposList.get(aproposList.size() - 1);
        assertThat(testApropos.getTexte()).isEqualTo(DEFAULT_TEXTE);
    }

    @Test
    @Transactional
    void createAproposWithExistingId() throws Exception {
        // Create the Apropos with an existing ID
        apropos.setId(1L);

        int databaseSizeBeforeCreate = aproposRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAproposMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apropos)))
            .andExpect(status().isBadRequest());

        // Validate the Apropos in the database
        List<Apropos> aproposList = aproposRepository.findAll();
        assertThat(aproposList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllApropos() throws Exception {
        // Initialize the database
        aproposRepository.saveAndFlush(apropos);

        // Get all the aproposList
        restAproposMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(apropos.getId().intValue())))
            .andExpect(jsonPath("$.[*].texte").value(hasItem(DEFAULT_TEXTE)));
    }

    @Test
    @Transactional
    void getApropos() throws Exception {
        // Initialize the database
        aproposRepository.saveAndFlush(apropos);

        // Get the apropos
        restAproposMockMvc
            .perform(get(ENTITY_API_URL_ID, apropos.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(apropos.getId().intValue()))
            .andExpect(jsonPath("$.texte").value(DEFAULT_TEXTE));
    }

    @Test
    @Transactional
    void getNonExistingApropos() throws Exception {
        // Get the apropos
        restAproposMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewApropos() throws Exception {
        // Initialize the database
        aproposRepository.saveAndFlush(apropos);

        int databaseSizeBeforeUpdate = aproposRepository.findAll().size();

        // Update the apropos
        Apropos updatedApropos = aproposRepository.findById(apropos.getId()).get();
        // Disconnect from session so that the updates on updatedApropos are not directly saved in db
        em.detach(updatedApropos);
        updatedApropos.texte(UPDATED_TEXTE);

        restAproposMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedApropos.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedApropos))
            )
            .andExpect(status().isOk());

        // Validate the Apropos in the database
        List<Apropos> aproposList = aproposRepository.findAll();
        assertThat(aproposList).hasSize(databaseSizeBeforeUpdate);
        Apropos testApropos = aproposList.get(aproposList.size() - 1);
        assertThat(testApropos.getTexte()).isEqualTo(UPDATED_TEXTE);
    }

    @Test
    @Transactional
    void putNonExistingApropos() throws Exception {
        int databaseSizeBeforeUpdate = aproposRepository.findAll().size();
        apropos.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAproposMockMvc
            .perform(
                put(ENTITY_API_URL_ID, apropos.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(apropos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Apropos in the database
        List<Apropos> aproposList = aproposRepository.findAll();
        assertThat(aproposList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchApropos() throws Exception {
        int databaseSizeBeforeUpdate = aproposRepository.findAll().size();
        apropos.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAproposMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(apropos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Apropos in the database
        List<Apropos> aproposList = aproposRepository.findAll();
        assertThat(aproposList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamApropos() throws Exception {
        int databaseSizeBeforeUpdate = aproposRepository.findAll().size();
        apropos.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAproposMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apropos)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Apropos in the database
        List<Apropos> aproposList = aproposRepository.findAll();
        assertThat(aproposList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAproposWithPatch() throws Exception {
        // Initialize the database
        aproposRepository.saveAndFlush(apropos);

        int databaseSizeBeforeUpdate = aproposRepository.findAll().size();

        // Update the apropos using partial update
        Apropos partialUpdatedApropos = new Apropos();
        partialUpdatedApropos.setId(apropos.getId());

        restAproposMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApropos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApropos))
            )
            .andExpect(status().isOk());

        // Validate the Apropos in the database
        List<Apropos> aproposList = aproposRepository.findAll();
        assertThat(aproposList).hasSize(databaseSizeBeforeUpdate);
        Apropos testApropos = aproposList.get(aproposList.size() - 1);
        assertThat(testApropos.getTexte()).isEqualTo(DEFAULT_TEXTE);
    }

    @Test
    @Transactional
    void fullUpdateAproposWithPatch() throws Exception {
        // Initialize the database
        aproposRepository.saveAndFlush(apropos);

        int databaseSizeBeforeUpdate = aproposRepository.findAll().size();

        // Update the apropos using partial update
        Apropos partialUpdatedApropos = new Apropos();
        partialUpdatedApropos.setId(apropos.getId());

        partialUpdatedApropos.texte(UPDATED_TEXTE);

        restAproposMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApropos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApropos))
            )
            .andExpect(status().isOk());

        // Validate the Apropos in the database
        List<Apropos> aproposList = aproposRepository.findAll();
        assertThat(aproposList).hasSize(databaseSizeBeforeUpdate);
        Apropos testApropos = aproposList.get(aproposList.size() - 1);
        assertThat(testApropos.getTexte()).isEqualTo(UPDATED_TEXTE);
    }

    @Test
    @Transactional
    void patchNonExistingApropos() throws Exception {
        int databaseSizeBeforeUpdate = aproposRepository.findAll().size();
        apropos.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAproposMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, apropos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(apropos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Apropos in the database
        List<Apropos> aproposList = aproposRepository.findAll();
        assertThat(aproposList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchApropos() throws Exception {
        int databaseSizeBeforeUpdate = aproposRepository.findAll().size();
        apropos.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAproposMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(apropos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Apropos in the database
        List<Apropos> aproposList = aproposRepository.findAll();
        assertThat(aproposList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamApropos() throws Exception {
        int databaseSizeBeforeUpdate = aproposRepository.findAll().size();
        apropos.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAproposMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(apropos)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Apropos in the database
        List<Apropos> aproposList = aproposRepository.findAll();
        assertThat(aproposList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteApropos() throws Exception {
        // Initialize the database
        aproposRepository.saveAndFlush(apropos);

        int databaseSizeBeforeDelete = aproposRepository.findAll().size();

        // Delete the apropos
        restAproposMockMvc
            .perform(delete(ENTITY_API_URL_ID, apropos.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Apropos> aproposList = aproposRepository.findAll();
        assertThat(aproposList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
