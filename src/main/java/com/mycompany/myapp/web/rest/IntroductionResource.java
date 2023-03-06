package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Introduction;
import com.mycompany.myapp.repository.IntroductionRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Introduction}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class IntroductionResource {

    private final Logger log = LoggerFactory.getLogger(IntroductionResource.class);

    private static final String ENTITY_NAME = "introduction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IntroductionRepository introductionRepository;

    public IntroductionResource(IntroductionRepository introductionRepository) {
        this.introductionRepository = introductionRepository;
    }

    /**
     * {@code POST  /introductions} : Create a new introduction.
     *
     * @param introduction the introduction to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new introduction, or with status {@code 400 (Bad Request)} if the introduction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/introductions")
    public ResponseEntity<Introduction> createIntroduction(@RequestBody Introduction introduction) throws URISyntaxException {
        log.debug("REST request to save Introduction : {}", introduction);
        if (introduction.getId() != null) {
            throw new BadRequestAlertException("A new introduction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Introduction result = introductionRepository.save(introduction);
        return ResponseEntity
            .created(new URI("/api/introductions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /introductions/:id} : Updates an existing introduction.
     *
     * @param id the id of the introduction to save.
     * @param introduction the introduction to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated introduction,
     * or with status {@code 400 (Bad Request)} if the introduction is not valid,
     * or with status {@code 500 (Internal Server Error)} if the introduction couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/introductions/{id}")
    public ResponseEntity<Introduction> updateIntroduction(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Introduction introduction
    ) throws URISyntaxException {
        log.debug("REST request to update Introduction : {}, {}", id, introduction);
        if (introduction.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, introduction.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!introductionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Introduction result = introductionRepository.save(introduction);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, introduction.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /introductions/:id} : Partial updates given fields of an existing introduction, field will ignore if it is null
     *
     * @param id the id of the introduction to save.
     * @param introduction the introduction to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated introduction,
     * or with status {@code 400 (Bad Request)} if the introduction is not valid,
     * or with status {@code 404 (Not Found)} if the introduction is not found,
     * or with status {@code 500 (Internal Server Error)} if the introduction couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/introductions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Introduction> partialUpdateIntroduction(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Introduction introduction
    ) throws URISyntaxException {
        log.debug("REST request to partial update Introduction partially : {}, {}", id, introduction);
        if (introduction.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, introduction.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!introductionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Introduction> result = introductionRepository
            .findById(introduction.getId())
            .map(existingIntroduction -> {
                if (introduction.getIntroduction() != null) {
                    existingIntroduction.setIntroduction(introduction.getIntroduction());
                }

                return existingIntroduction;
            })
            .map(introductionRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, introduction.getId().toString())
        );
    }

    /**
     * {@code GET  /introductions} : get all the introductions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of introductions in body.
     */
    @GetMapping("/introductions")
    public List<Introduction> getAllIntroductions() {
        log.debug("REST request to get all Introductions");
        return introductionRepository.findAll();
    }

    /**
     * {@code GET  /introductions/:id} : get the "id" introduction.
     *
     * @param id the id of the introduction to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the introduction, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/introductions/{id}")
    public ResponseEntity<Introduction> getIntroduction(@PathVariable Long id) {
        log.debug("REST request to get Introduction : {}", id);
        Optional<Introduction> introduction = introductionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(introduction);
    }

    /**
     * {@code DELETE  /introductions/:id} : delete the "id" introduction.
     *
     * @param id the id of the introduction to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/introductions/{id}")
    public ResponseEntity<Void> deleteIntroduction(@PathVariable Long id) {
        log.debug("REST request to delete Introduction : {}", id);
        introductionRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
