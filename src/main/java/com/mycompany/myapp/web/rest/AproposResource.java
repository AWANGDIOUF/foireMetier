package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Apropos;
import com.mycompany.myapp.repository.AproposRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Apropos}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AproposResource {

    private final Logger log = LoggerFactory.getLogger(AproposResource.class);

    private static final String ENTITY_NAME = "apropos";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AproposRepository aproposRepository;

    public AproposResource(AproposRepository aproposRepository) {
        this.aproposRepository = aproposRepository;
    }

    /**
     * {@code POST  /apropos} : Create a new apropos.
     *
     * @param apropos the apropos to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new apropos, or with status {@code 400 (Bad Request)} if the apropos has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/apropos")
    public ResponseEntity<Apropos> createApropos(@RequestBody Apropos apropos) throws URISyntaxException {
        log.debug("REST request to save Apropos : {}", apropos);
        if (apropos.getId() != null) {
            throw new BadRequestAlertException("A new apropos cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Apropos result = aproposRepository.save(apropos);
        return ResponseEntity
            .created(new URI("/api/apropos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /apropos/:id} : Updates an existing apropos.
     *
     * @param id the id of the apropos to save.
     * @param apropos the apropos to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated apropos,
     * or with status {@code 400 (Bad Request)} if the apropos is not valid,
     * or with status {@code 500 (Internal Server Error)} if the apropos couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/apropos/{id}")
    public ResponseEntity<Apropos> updateApropos(@PathVariable(value = "id", required = false) final Long id, @RequestBody Apropos apropos)
        throws URISyntaxException {
        log.debug("REST request to update Apropos : {}, {}", id, apropos);
        if (apropos.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, apropos.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aproposRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Apropos result = aproposRepository.save(apropos);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, apropos.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /apropos/:id} : Partial updates given fields of an existing apropos, field will ignore if it is null
     *
     * @param id the id of the apropos to save.
     * @param apropos the apropos to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated apropos,
     * or with status {@code 400 (Bad Request)} if the apropos is not valid,
     * or with status {@code 404 (Not Found)} if the apropos is not found,
     * or with status {@code 500 (Internal Server Error)} if the apropos couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/apropos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Apropos> partialUpdateApropos(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Apropos apropos
    ) throws URISyntaxException {
        log.debug("REST request to partial update Apropos partially : {}, {}", id, apropos);
        if (apropos.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, apropos.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aproposRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Apropos> result = aproposRepository
            .findById(apropos.getId())
            .map(existingApropos -> {
                if (apropos.getTexte() != null) {
                    existingApropos.setTexte(apropos.getTexte());
                }

                return existingApropos;
            })
            .map(aproposRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, apropos.getId().toString())
        );
    }

    /**
     * {@code GET  /apropos} : get all the apropos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of apropos in body.
     */
    @GetMapping("/apropos")
    public List<Apropos> getAllApropos() {
        log.debug("REST request to get all Apropos");
        return aproposRepository.findAll();
    }

    /**
     * {@code GET  /apropos/:id} : get the "id" apropos.
     *
     * @param id the id of the apropos to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the apropos, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/apropos/{id}")
    public ResponseEntity<Apropos> getApropos(@PathVariable Long id) {
        log.debug("REST request to get Apropos : {}", id);
        Optional<Apropos> apropos = aproposRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(apropos);
    }

    /**
     * {@code DELETE  /apropos/:id} : delete the "id" apropos.
     *
     * @param id the id of the apropos to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/apropos/{id}")
    public ResponseEntity<Void> deleteApropos(@PathVariable Long id) {
        log.debug("REST request to delete Apropos : {}", id);
        aproposRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
