package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Inscription;
import com.mycompany.myapp.repository.InscriptionRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Inscription}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class InscriptionResource {

    private final Logger log = LoggerFactory.getLogger(InscriptionResource.class);

    private static final String ENTITY_NAME = "inscription";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InscriptionRepository inscriptionRepository;

    public InscriptionResource(InscriptionRepository inscriptionRepository) {
        this.inscriptionRepository = inscriptionRepository;
    }

    /**
     * {@code POST  /inscriptions} : Create a new inscription.
     *
     * @param inscription the inscription to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new inscription, or with status {@code 400 (Bad Request)} if the inscription has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/inscriptions")
    public ResponseEntity<Inscription> createInscription(@RequestBody Inscription inscription) throws URISyntaxException {
        log.debug("REST request to save Inscription : {}", inscription);
        if (inscription.getId() != null) {
            throw new BadRequestAlertException("A new inscription cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Inscription result = inscriptionRepository.save(inscription);
        return ResponseEntity
            .created(new URI("/api/inscriptions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /inscriptions/:id} : Updates an existing inscription.
     *
     * @param id the id of the inscription to save.
     * @param inscription the inscription to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inscription,
     * or with status {@code 400 (Bad Request)} if the inscription is not valid,
     * or with status {@code 500 (Internal Server Error)} if the inscription couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/inscriptions/{id}")
    public ResponseEntity<Inscription> updateInscription(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Inscription inscription
    ) throws URISyntaxException {
        log.debug("REST request to update Inscription : {}, {}", id, inscription);
        if (inscription.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inscription.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!inscriptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Inscription result = inscriptionRepository.save(inscription);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, inscription.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /inscriptions/:id} : Partial updates given fields of an existing inscription, field will ignore if it is null
     *
     * @param id the id of the inscription to save.
     * @param inscription the inscription to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inscription,
     * or with status {@code 400 (Bad Request)} if the inscription is not valid,
     * or with status {@code 404 (Not Found)} if the inscription is not found,
     * or with status {@code 500 (Internal Server Error)} if the inscription couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/inscriptions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Inscription> partialUpdateInscription(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Inscription inscription
    ) throws URISyntaxException {
        log.debug("REST request to partial update Inscription partially : {}, {}", id, inscription);
        if (inscription.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inscription.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!inscriptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Inscription> result = inscriptionRepository
            .findById(inscription.getId())
            .map(existingInscription -> {
                if (inscription.getPartenaire() != null) {
                    existingInscription.setPartenaire(inscription.getPartenaire());
                }
                if (inscription.getNon() != null) {
                    existingInscription.setNon(inscription.getNon());
                }
                if (inscription.getPrenon() != null) {
                    existingInscription.setPrenon(inscription.getPrenon());
                }
                if (inscription.getAdresseProfessionnelle() != null) {
                    existingInscription.setAdresseProfessionnelle(inscription.getAdresseProfessionnelle());
                }
                if (inscription.getTelephoneProfessionnelle() != null) {
                    existingInscription.setTelephoneProfessionnelle(inscription.getTelephoneProfessionnelle());
                }
                if (inscription.getEmailProfessionnelle() != null) {
                    existingInscription.setEmailProfessionnelle(inscription.getEmailProfessionnelle());
                }
                if (inscription.getDemandeStand() != null) {
                    existingInscription.setDemandeStand(inscription.getDemandeStand());
                }
                if (inscription.getTailleStand() != null) {
                    existingInscription.setTailleStand(inscription.getTailleStand());
                }
                if (inscription.getAutres() != null) {
                    existingInscription.setAutres(inscription.getAutres());
                }

                return existingInscription;
            })
            .map(inscriptionRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, inscription.getId().toString())
        );
    }

    /**
     * {@code GET  /inscriptions} : get all the inscriptions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of inscriptions in body.
     */
    @GetMapping("/inscriptions")
    public List<Inscription> getAllInscriptions() {
        log.debug("REST request to get all Inscriptions");
        return inscriptionRepository.findAll();
    }

    /**
     * {@code GET  /inscriptions/:id} : get the "id" inscription.
     *
     * @param id the id of the inscription to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the inscription, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/inscriptions/{id}")
    public ResponseEntity<Inscription> getInscription(@PathVariable Long id) {
        log.debug("REST request to get Inscription : {}", id);
        Optional<Inscription> inscription = inscriptionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(inscription);
    }

    /**
     * {@code DELETE  /inscriptions/:id} : delete the "id" inscription.
     *
     * @param id the id of the inscription to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/inscriptions/{id}")
    public ResponseEntity<Void> deleteInscription(@PathVariable Long id) {
        log.debug("REST request to delete Inscription : {}", id);
        inscriptionRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
