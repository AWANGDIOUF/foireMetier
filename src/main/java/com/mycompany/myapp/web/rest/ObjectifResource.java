package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Objectif;
import com.mycompany.myapp.repository.ObjectifRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Objectif}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ObjectifResource {

    private final Logger log = LoggerFactory.getLogger(ObjectifResource.class);

    private static final String ENTITY_NAME = "objectif";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ObjectifRepository objectifRepository;

    public ObjectifResource(ObjectifRepository objectifRepository) {
        this.objectifRepository = objectifRepository;
    }

    /**
     * {@code POST  /objectifs} : Create a new objectif.
     *
     * @param objectif the objectif to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new objectif, or with status {@code 400 (Bad Request)} if the objectif has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/objectifs")
    public ResponseEntity<Objectif> createObjectif(@RequestBody Objectif objectif) throws URISyntaxException {
        log.debug("REST request to save Objectif : {}", objectif);
        if (objectif.getId() != null) {
            throw new BadRequestAlertException("A new objectif cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Objectif result = objectifRepository.save(objectif);
        return ResponseEntity
            .created(new URI("/api/objectifs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /objectifs/:id} : Updates an existing objectif.
     *
     * @param id the id of the objectif to save.
     * @param objectif the objectif to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated objectif,
     * or with status {@code 400 (Bad Request)} if the objectif is not valid,
     * or with status {@code 500 (Internal Server Error)} if the objectif couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/objectifs/{id}")
    public ResponseEntity<Objectif> updateObjectif(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Objectif objectif
    ) throws URISyntaxException {
        log.debug("REST request to update Objectif : {}, {}", id, objectif);
        if (objectif.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, objectif.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!objectifRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Objectif result = objectifRepository.save(objectif);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, objectif.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /objectifs/:id} : Partial updates given fields of an existing objectif, field will ignore if it is null
     *
     * @param id the id of the objectif to save.
     * @param objectif the objectif to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated objectif,
     * or with status {@code 400 (Bad Request)} if the objectif is not valid,
     * or with status {@code 404 (Not Found)} if the objectif is not found,
     * or with status {@code 500 (Internal Server Error)} if the objectif couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/objectifs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Objectif> partialUpdateObjectif(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Objectif objectif
    ) throws URISyntaxException {
        log.debug("REST request to partial update Objectif partially : {}, {}", id, objectif);
        if (objectif.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, objectif.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!objectifRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Objectif> result = objectifRepository
            .findById(objectif.getId())
            .map(existingObjectif -> {
                if (objectif.getObjectif() != null) {
                    existingObjectif.setObjectif(objectif.getObjectif());
                }

                return existingObjectif;
            })
            .map(objectifRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, objectif.getId().toString())
        );
    }

    /**
     * {@code GET  /objectifs} : get all the objectifs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of objectifs in body.
     */
    @GetMapping("/objectifs")
    public List<Objectif> getAllObjectifs() {
        log.debug("REST request to get all Objectifs");
        return objectifRepository.findAll();
    }

    /**
     * {@code GET  /objectifs/:id} : get the "id" objectif.
     *
     * @param id the id of the objectif to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the objectif, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/objectifs/{id}")
    public ResponseEntity<Objectif> getObjectif(@PathVariable Long id) {
        log.debug("REST request to get Objectif : {}", id);
        Optional<Objectif> objectif = objectifRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(objectif);
    }

    /**
     * {@code DELETE  /objectifs/:id} : delete the "id" objectif.
     *
     * @param id the id of the objectif to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/objectifs/{id}")
    public ResponseEntity<Void> deleteObjectif(@PathVariable Long id) {
        log.debug("REST request to delete Objectif : {}", id);
        objectifRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
