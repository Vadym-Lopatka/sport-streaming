package com.sportstream.myapp.web.rest;
import com.sportstream.myapp.service.FightService;
import com.sportstream.myapp.web.rest.errors.BadRequestAlertException;
import com.sportstream.myapp.web.rest.util.HeaderUtil;
import com.sportstream.myapp.web.rest.util.PaginationUtil;
import com.sportstream.myapp.service.dto.FightDTO;
import com.sportstream.myapp.service.dto.FightCriteria;
import com.sportstream.myapp.service.FightQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Fight.
 */
@RestController
@RequestMapping("/api")
public class FightResource {

    private final Logger log = LoggerFactory.getLogger(FightResource.class);

    private static final String ENTITY_NAME = "fight";

    private final FightService fightService;

    private final FightQueryService fightQueryService;

    public FightResource(FightService fightService, FightQueryService fightQueryService) {
        this.fightService = fightService;
        this.fightQueryService = fightQueryService;
    }

    /**
     * POST  /fights : Create a new fight.
     *
     * @param fightDTO the fightDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new fightDTO, or with status 400 (Bad Request) if the fight has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/fights")
    public ResponseEntity<FightDTO> createFight(@Valid @RequestBody FightDTO fightDTO) throws URISyntaxException {
        log.debug("REST request to save Fight : {}", fightDTO);
        if (fightDTO.getId() != null) {
            throw new BadRequestAlertException("A new fight cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FightDTO result = fightService.save(fightDTO);
        return ResponseEntity.created(new URI("/api/fights/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /fights : Updates an existing fight.
     *
     * @param fightDTO the fightDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated fightDTO,
     * or with status 400 (Bad Request) if the fightDTO is not valid,
     * or with status 500 (Internal Server Error) if the fightDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/fights")
    public ResponseEntity<FightDTO> updateFight(@Valid @RequestBody FightDTO fightDTO) throws URISyntaxException {
        log.debug("REST request to update Fight : {}", fightDTO);
        if (fightDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FightDTO result = fightService.save(fightDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, fightDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /fights : get all the fights.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of fights in body
     */
    @GetMapping("/fights")
    public ResponseEntity<List<FightDTO>> getAllFights(FightCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Fights by criteria: {}", criteria);
        Page<FightDTO> page = fightQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/fights");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /fights/count : count all the fights.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/fights/count")
    public ResponseEntity<Long> countFights(FightCriteria criteria) {
        log.debug("REST request to count Fights by criteria: {}", criteria);
        return ResponseEntity.ok().body(fightQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /fights/:id : get the "id" fight.
     *
     * @param id the id of the fightDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the fightDTO, or with status 404 (Not Found)
     */
    @GetMapping("/fights/{id}")
    public ResponseEntity<FightDTO> getFight(@PathVariable Long id) {
        log.debug("REST request to get Fight : {}", id);
        Optional<FightDTO> fightDTO = fightService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fightDTO);
    }

    /**
     * DELETE  /fights/:id : delete the "id" fight.
     *
     * @param id the id of the fightDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/fights/{id}")
    public ResponseEntity<Void> deleteFight(@PathVariable Long id) {
        log.debug("REST request to delete Fight : {}", id);
        fightService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
