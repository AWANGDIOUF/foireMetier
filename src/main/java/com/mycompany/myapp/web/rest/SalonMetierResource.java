package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.SalonMetier;
import com.mycompany.myapp.repository.SalonMetierRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.SalonMetier}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SalonMetierResource {

    private final Logger log = LoggerFactory.getLogger(SalonMetierResource.class);

    private static final String ENTITY_NAME = "salonMetier";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SalonMetierRepository salonMetierRepository;

    public SalonMetierResource(SalonMetierRepository salonMetierRepository) {
        this.salonMetierRepository = salonMetierRepository;
    }

    /**
     * {@code POST  /salon-metiers} : Create a new salonMetier.
     *
     * @param salonMetier the salonMetier to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new salonMetier, or with status {@code 400 (Bad Request)} if the salonMetier has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/salon-metiers")
    public ResponseEntity<SalonMetier> createSalonMetier(@RequestBody SalonMetier salonMetier) throws URISyntaxException {
        log.debug("REST request to save SalonMetier : {}", salonMetier);
        if (salonMetier.getId() != null) {
            throw new BadRequestAlertException("A new salonMetier cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SalonMetier result = salonMetierRepository.save(salonMetier);
        return ResponseEntity
            .created(new URI("/api/salon-metiers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /salon-metiers/:id} : Updates an existing salonMetier.
     *
     * @param id the id of the salonMetier to save.
     * @param salonMetier the salonMetier to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated salonMetier,
     * or with status {@code 400 (Bad Request)} if the salonMetier is not valid,
     * or with status {@code 500 (Internal Server Error)} if the salonMetier couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/salon-metiers/{id}")
    public ResponseEntity<SalonMetier> updateSalonMetier(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SalonMetier salonMetier
    ) throws URISyntaxException {
        log.debug("REST request to update SalonMetier : {}, {}", id, salonMetier);
        if (salonMetier.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, salonMetier.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!salonMetierRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SalonMetier result = salonMetierRepository.save(salonMetier);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, salonMetier.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /salon-metiers/:id} : Partial updates given fields of an existing salonMetier, field will ignore if it is null
     *
     * @param id the id of the salonMetier to save.
     * @param salonMetier the salonMetier to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated salonMetier,
     * or with status {@code 400 (Bad Request)} if the salonMetier is not valid,
     * or with status {@code 404 (Not Found)} if the salonMetier is not found,
     * or with status {@code 500 (Internal Server Error)} if the salonMetier couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/salon-metiers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SalonMetier> partialUpdateSalonMetier(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SalonMetier salonMetier
    ) throws URISyntaxException {
        log.debug("REST request to partial update SalonMetier partially : {}, {}", id, salonMetier);
        if (salonMetier.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, salonMetier.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!salonMetierRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SalonMetier> result = salonMetierRepository
            .findById(salonMetier.getId())
            .map(existingSalonMetier -> {
                if (salonMetier.getSalonMetier() != null) {
                    existingSalonMetier.setSalonMetier(salonMetier.getSalonMetier());
                }

                return existingSalonMetier;
            })
            .map(salonMetierRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, salonMetier.getId().toString())
        );
    }

    /**
     * {@code GET  /salon-metiers} : get all the salonMetiers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of salonMetiers in body.
     */
    @GetMapping("/salon-metiers")
    public List<SalonMetier> getAllSalonMetiers() {
        log.debug("REST request to get all SalonMetiers");
        return salonMetierRepository.findAll();
    }

    /**
     * {@code GET  /salon-metiers/:id} : get the "id" salonMetier.
     *
     * @param id the id of the salonMetier to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the salonMetier, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/salon-metiers/{id}")
    public ResponseEntity<SalonMetier> getSalonMetier(@PathVariable Long id) {
        log.debug("REST request to get SalonMetier : {}", id);
        Optional<SalonMetier> salonMetier = salonMetierRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(salonMetier);
    }

    /**
     * {@code DELETE  /salon-metiers/:id} : delete the "id" salonMetier.
     *
     * @param id the id of the salonMetier to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/salon-metiers/{id}")
    public ResponseEntity<Void> deleteSalonMetier(@PathVariable Long id) {
        log.debug("REST request to delete SalonMetier : {}", id);
        salonMetierRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
