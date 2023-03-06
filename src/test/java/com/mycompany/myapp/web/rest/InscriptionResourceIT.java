package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Inscription;
import com.mycompany.myapp.domain.enumeration.DemandeStand;
import com.mycompany.myapp.repository.InscriptionRepository;
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
 * Integration tests for the {@link InscriptionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InscriptionResourceIT {

    private static final String DEFAULT_PARTENAIRE = "AAAAAAAAAA";
    private static final String UPDATED_PARTENAIRE = "BBBBBBBBBB";

    private static final String DEFAULT_NON = "AAAAAAAAAA";
    private static final String UPDATED_NON = "BBBBBBBBBB";

    private static final String DEFAULT_PRENON = "AAAAAAAAAA";
    private static final String UPDATED_PRENON = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESSE_PROFESSIONNELLE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE_PROFESSIONNELLE = "BBBBBBBBBB";

    private static final String DEFAULT_TELEPHONE_PROFESSIONNELLE = "AAAAAAAAAA";
    private static final String UPDATED_TELEPHONE_PROFESSIONNELLE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL_PROFESSIONNELLE = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_PROFESSIONNELLE = "BBBBBBBBBB";

    private static final DemandeStand DEFAULT_DEMANDE_STAND = DemandeStand.OUI;
    private static final DemandeStand UPDATED_DEMANDE_STAND = DemandeStand.NON;

    private static final String DEFAULT_TAILLE_STAND = "AAAAAAAAAA";
    private static final String UPDATED_TAILLE_STAND = "BBBBBBBBBB";

    private static final String DEFAULT_AUTRES = "AAAAAAAAAA";
    private static final String UPDATED_AUTRES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/inscriptions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InscriptionRepository inscriptionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInscriptionMockMvc;

    private Inscription inscription;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Inscription createEntity(EntityManager em) {
        Inscription inscription = new Inscription()
            .partenaire(DEFAULT_PARTENAIRE)
            .non(DEFAULT_NON)
            .prenon(DEFAULT_PRENON)
            .adresseProfessionnelle(DEFAULT_ADRESSE_PROFESSIONNELLE)
            .telephoneProfessionnelle(DEFAULT_TELEPHONE_PROFESSIONNELLE)
            .emailProfessionnelle(DEFAULT_EMAIL_PROFESSIONNELLE)
            .demandeStand(DEFAULT_DEMANDE_STAND)
            .tailleStand(DEFAULT_TAILLE_STAND)
            .autres(DEFAULT_AUTRES);
        return inscription;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Inscription createUpdatedEntity(EntityManager em) {
        Inscription inscription = new Inscription()
            .partenaire(UPDATED_PARTENAIRE)
            .non(UPDATED_NON)
            .prenon(UPDATED_PRENON)
            .adresseProfessionnelle(UPDATED_ADRESSE_PROFESSIONNELLE)
            .telephoneProfessionnelle(UPDATED_TELEPHONE_PROFESSIONNELLE)
            .emailProfessionnelle(UPDATED_EMAIL_PROFESSIONNELLE)
            .demandeStand(UPDATED_DEMANDE_STAND)
            .tailleStand(UPDATED_TAILLE_STAND)
            .autres(UPDATED_AUTRES);
        return inscription;
    }

    @BeforeEach
    public void initTest() {
        inscription = createEntity(em);
    }

    @Test
    @Transactional
    void createInscription() throws Exception {
        int databaseSizeBeforeCreate = inscriptionRepository.findAll().size();
        // Create the Inscription
        restInscriptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(inscription)))
            .andExpect(status().isCreated());

        // Validate the Inscription in the database
        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeCreate + 1);
        Inscription testInscription = inscriptionList.get(inscriptionList.size() - 1);
        assertThat(testInscription.getPartenaire()).isEqualTo(DEFAULT_PARTENAIRE);
        assertThat(testInscription.getNon()).isEqualTo(DEFAULT_NON);
        assertThat(testInscription.getPrenon()).isEqualTo(DEFAULT_PRENON);
        assertThat(testInscription.getAdresseProfessionnelle()).isEqualTo(DEFAULT_ADRESSE_PROFESSIONNELLE);
        assertThat(testInscription.getTelephoneProfessionnelle()).isEqualTo(DEFAULT_TELEPHONE_PROFESSIONNELLE);
        assertThat(testInscription.getEmailProfessionnelle()).isEqualTo(DEFAULT_EMAIL_PROFESSIONNELLE);
        assertThat(testInscription.getDemandeStand()).isEqualTo(DEFAULT_DEMANDE_STAND);
        assertThat(testInscription.getTailleStand()).isEqualTo(DEFAULT_TAILLE_STAND);
        assertThat(testInscription.getAutres()).isEqualTo(DEFAULT_AUTRES);
    }

    @Test
    @Transactional
    void createInscriptionWithExistingId() throws Exception {
        // Create the Inscription with an existing ID
        inscription.setId(1L);

        int databaseSizeBeforeCreate = inscriptionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInscriptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(inscription)))
            .andExpect(status().isBadRequest());

        // Validate the Inscription in the database
        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllInscriptions() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        // Get all the inscriptionList
        restInscriptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inscription.getId().intValue())))
            .andExpect(jsonPath("$.[*].partenaire").value(hasItem(DEFAULT_PARTENAIRE)))
            .andExpect(jsonPath("$.[*].non").value(hasItem(DEFAULT_NON)))
            .andExpect(jsonPath("$.[*].prenon").value(hasItem(DEFAULT_PRENON)))
            .andExpect(jsonPath("$.[*].adresseProfessionnelle").value(hasItem(DEFAULT_ADRESSE_PROFESSIONNELLE)))
            .andExpect(jsonPath("$.[*].telephoneProfessionnelle").value(hasItem(DEFAULT_TELEPHONE_PROFESSIONNELLE)))
            .andExpect(jsonPath("$.[*].emailProfessionnelle").value(hasItem(DEFAULT_EMAIL_PROFESSIONNELLE)))
            .andExpect(jsonPath("$.[*].demandeStand").value(hasItem(DEFAULT_DEMANDE_STAND.toString())))
            .andExpect(jsonPath("$.[*].tailleStand").value(hasItem(DEFAULT_TAILLE_STAND)))
            .andExpect(jsonPath("$.[*].autres").value(hasItem(DEFAULT_AUTRES)));
    }

    @Test
    @Transactional
    void getInscription() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        // Get the inscription
        restInscriptionMockMvc
            .perform(get(ENTITY_API_URL_ID, inscription.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(inscription.getId().intValue()))
            .andExpect(jsonPath("$.partenaire").value(DEFAULT_PARTENAIRE))
            .andExpect(jsonPath("$.non").value(DEFAULT_NON))
            .andExpect(jsonPath("$.prenon").value(DEFAULT_PRENON))
            .andExpect(jsonPath("$.adresseProfessionnelle").value(DEFAULT_ADRESSE_PROFESSIONNELLE))
            .andExpect(jsonPath("$.telephoneProfessionnelle").value(DEFAULT_TELEPHONE_PROFESSIONNELLE))
            .andExpect(jsonPath("$.emailProfessionnelle").value(DEFAULT_EMAIL_PROFESSIONNELLE))
            .andExpect(jsonPath("$.demandeStand").value(DEFAULT_DEMANDE_STAND.toString()))
            .andExpect(jsonPath("$.tailleStand").value(DEFAULT_TAILLE_STAND))
            .andExpect(jsonPath("$.autres").value(DEFAULT_AUTRES));
    }

    @Test
    @Transactional
    void getNonExistingInscription() throws Exception {
        // Get the inscription
        restInscriptionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewInscription() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        int databaseSizeBeforeUpdate = inscriptionRepository.findAll().size();

        // Update the inscription
        Inscription updatedInscription = inscriptionRepository.findById(inscription.getId()).get();
        // Disconnect from session so that the updates on updatedInscription are not directly saved in db
        em.detach(updatedInscription);
        updatedInscription
            .partenaire(UPDATED_PARTENAIRE)
            .non(UPDATED_NON)
            .prenon(UPDATED_PRENON)
            .adresseProfessionnelle(UPDATED_ADRESSE_PROFESSIONNELLE)
            .telephoneProfessionnelle(UPDATED_TELEPHONE_PROFESSIONNELLE)
            .emailProfessionnelle(UPDATED_EMAIL_PROFESSIONNELLE)
            .demandeStand(UPDATED_DEMANDE_STAND)
            .tailleStand(UPDATED_TAILLE_STAND)
            .autres(UPDATED_AUTRES);

        restInscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInscription.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedInscription))
            )
            .andExpect(status().isOk());

        // Validate the Inscription in the database
        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeUpdate);
        Inscription testInscription = inscriptionList.get(inscriptionList.size() - 1);
        assertThat(testInscription.getPartenaire()).isEqualTo(UPDATED_PARTENAIRE);
        assertThat(testInscription.getNon()).isEqualTo(UPDATED_NON);
        assertThat(testInscription.getPrenon()).isEqualTo(UPDATED_PRENON);
        assertThat(testInscription.getAdresseProfessionnelle()).isEqualTo(UPDATED_ADRESSE_PROFESSIONNELLE);
        assertThat(testInscription.getTelephoneProfessionnelle()).isEqualTo(UPDATED_TELEPHONE_PROFESSIONNELLE);
        assertThat(testInscription.getEmailProfessionnelle()).isEqualTo(UPDATED_EMAIL_PROFESSIONNELLE);
        assertThat(testInscription.getDemandeStand()).isEqualTo(UPDATED_DEMANDE_STAND);
        assertThat(testInscription.getTailleStand()).isEqualTo(UPDATED_TAILLE_STAND);
        assertThat(testInscription.getAutres()).isEqualTo(UPDATED_AUTRES);
    }

    @Test
    @Transactional
    void putNonExistingInscription() throws Exception {
        int databaseSizeBeforeUpdate = inscriptionRepository.findAll().size();
        inscription.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inscription.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inscription))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inscription in the database
        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInscription() throws Exception {
        int databaseSizeBeforeUpdate = inscriptionRepository.findAll().size();
        inscription.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inscription))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inscription in the database
        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInscription() throws Exception {
        int databaseSizeBeforeUpdate = inscriptionRepository.findAll().size();
        inscription.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInscriptionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(inscription)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Inscription in the database
        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInscriptionWithPatch() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        int databaseSizeBeforeUpdate = inscriptionRepository.findAll().size();

        // Update the inscription using partial update
        Inscription partialUpdatedInscription = new Inscription();
        partialUpdatedInscription.setId(inscription.getId());

        partialUpdatedInscription
            .partenaire(UPDATED_PARTENAIRE)
            .prenon(UPDATED_PRENON)
            .adresseProfessionnelle(UPDATED_ADRESSE_PROFESSIONNELLE)
            .telephoneProfessionnelle(UPDATED_TELEPHONE_PROFESSIONNELLE)
            .tailleStand(UPDATED_TAILLE_STAND);

        restInscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInscription.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInscription))
            )
            .andExpect(status().isOk());

        // Validate the Inscription in the database
        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeUpdate);
        Inscription testInscription = inscriptionList.get(inscriptionList.size() - 1);
        assertThat(testInscription.getPartenaire()).isEqualTo(UPDATED_PARTENAIRE);
        assertThat(testInscription.getNon()).isEqualTo(DEFAULT_NON);
        assertThat(testInscription.getPrenon()).isEqualTo(UPDATED_PRENON);
        assertThat(testInscription.getAdresseProfessionnelle()).isEqualTo(UPDATED_ADRESSE_PROFESSIONNELLE);
        assertThat(testInscription.getTelephoneProfessionnelle()).isEqualTo(UPDATED_TELEPHONE_PROFESSIONNELLE);
        assertThat(testInscription.getEmailProfessionnelle()).isEqualTo(DEFAULT_EMAIL_PROFESSIONNELLE);
        assertThat(testInscription.getDemandeStand()).isEqualTo(DEFAULT_DEMANDE_STAND);
        assertThat(testInscription.getTailleStand()).isEqualTo(UPDATED_TAILLE_STAND);
        assertThat(testInscription.getAutres()).isEqualTo(DEFAULT_AUTRES);
    }

    @Test
    @Transactional
    void fullUpdateInscriptionWithPatch() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        int databaseSizeBeforeUpdate = inscriptionRepository.findAll().size();

        // Update the inscription using partial update
        Inscription partialUpdatedInscription = new Inscription();
        partialUpdatedInscription.setId(inscription.getId());

        partialUpdatedInscription
            .partenaire(UPDATED_PARTENAIRE)
            .non(UPDATED_NON)
            .prenon(UPDATED_PRENON)
            .adresseProfessionnelle(UPDATED_ADRESSE_PROFESSIONNELLE)
            .telephoneProfessionnelle(UPDATED_TELEPHONE_PROFESSIONNELLE)
            .emailProfessionnelle(UPDATED_EMAIL_PROFESSIONNELLE)
            .demandeStand(UPDATED_DEMANDE_STAND)
            .tailleStand(UPDATED_TAILLE_STAND)
            .autres(UPDATED_AUTRES);

        restInscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInscription.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInscription))
            )
            .andExpect(status().isOk());

        // Validate the Inscription in the database
        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeUpdate);
        Inscription testInscription = inscriptionList.get(inscriptionList.size() - 1);
        assertThat(testInscription.getPartenaire()).isEqualTo(UPDATED_PARTENAIRE);
        assertThat(testInscription.getNon()).isEqualTo(UPDATED_NON);
        assertThat(testInscription.getPrenon()).isEqualTo(UPDATED_PRENON);
        assertThat(testInscription.getAdresseProfessionnelle()).isEqualTo(UPDATED_ADRESSE_PROFESSIONNELLE);
        assertThat(testInscription.getTelephoneProfessionnelle()).isEqualTo(UPDATED_TELEPHONE_PROFESSIONNELLE);
        assertThat(testInscription.getEmailProfessionnelle()).isEqualTo(UPDATED_EMAIL_PROFESSIONNELLE);
        assertThat(testInscription.getDemandeStand()).isEqualTo(UPDATED_DEMANDE_STAND);
        assertThat(testInscription.getTailleStand()).isEqualTo(UPDATED_TAILLE_STAND);
        assertThat(testInscription.getAutres()).isEqualTo(UPDATED_AUTRES);
    }

    @Test
    @Transactional
    void patchNonExistingInscription() throws Exception {
        int databaseSizeBeforeUpdate = inscriptionRepository.findAll().size();
        inscription.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, inscription.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(inscription))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inscription in the database
        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInscription() throws Exception {
        int databaseSizeBeforeUpdate = inscriptionRepository.findAll().size();
        inscription.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(inscription))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inscription in the database
        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInscription() throws Exception {
        int databaseSizeBeforeUpdate = inscriptionRepository.findAll().size();
        inscription.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(inscription))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Inscription in the database
        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInscription() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        int databaseSizeBeforeDelete = inscriptionRepository.findAll().size();

        // Delete the inscription
        restInscriptionMockMvc
            .perform(delete(ENTITY_API_URL_ID, inscription.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
