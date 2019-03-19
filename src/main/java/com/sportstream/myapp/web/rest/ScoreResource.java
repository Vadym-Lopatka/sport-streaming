package com.sportstream.myapp.web.rest;
import com.sportstream.myapp.service.ScoreService;
import com.sportstream.myapp.web.rest.errors.BadRequestAlertException;
import com.sportstream.myapp.web.rest.util.HeaderUtil;
import com.sportstream.myapp.web.rest.util.PaginationUtil;
import com.sportstream.myapp.service.dto.ScoreDTO;
import com.sportstream.myapp.service.dto.ScoreCriteria;
import com.sportstream.myapp.service.ScoreQueryService;
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
 * REST controller for managing Score.
 */
@RestController
@RequestMapping("/api")
public class ScoreResource {

    private final Logger log = LoggerFactory.getLogger(ScoreResource.class);

    private static final String ENTITY_NAME = "score";

    private final ScoreService scoreService;

    private final ScoreQueryService scoreQueryService;

    public ScoreResource(ScoreService scoreService, ScoreQueryService scoreQueryService) {
        this.scoreService = scoreService;
        this.scoreQueryService = scoreQueryService;
    }

    /**
     * POST  /scores : Create a new score.
     *
     * @param scoreDTO the scoreDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new scoreDTO, or with status 400 (Bad Request) if the score has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/scores")
    public ResponseEntity<ScoreDTO> createScore(@Valid @RequestBody ScoreDTO scoreDTO) throws URISyntaxException {
        log.debug("REST request to save Score : {}", scoreDTO);
        if (scoreDTO.getId() != null) {
            throw new BadRequestAlertException("A new score cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ScoreDTO result = scoreService.save(scoreDTO);
        return ResponseEntity.created(new URI("/api/scores/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /scores : Updates an existing score.
     *
     * @param scoreDTO the scoreDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated scoreDTO,
     * or with status 400 (Bad Request) if the scoreDTO is not valid,
     * or with status 500 (Internal Server Error) if the scoreDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/scores")
    public ResponseEntity<ScoreDTO> updateScore(@Valid @RequestBody ScoreDTO scoreDTO) throws URISyntaxException {
        log.debug("REST request to update Score : {}", scoreDTO);
        if (scoreDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ScoreDTO result = scoreService.save(scoreDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, scoreDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /scores : get all the scores.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of scores in body
     */
    @GetMapping("/scores")
    public ResponseEntity<List<ScoreDTO>> getAllScores(ScoreCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Scores by criteria: {}", criteria);
        Page<ScoreDTO> page = scoreQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/scores");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /scores/count : count all the scores.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/scores/count")
    public ResponseEntity<Long> countScores(ScoreCriteria criteria) {
        log.debug("REST request to count Scores by criteria: {}", criteria);
        return ResponseEntity.ok().body(scoreQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /scores/:id : get the "id" score.
     *
     * @param id the id of the scoreDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the scoreDTO, or with status 404 (Not Found)
     */
    @GetMapping("/scores/{id}")
    public ResponseEntity<ScoreDTO> getScore(@PathVariable Long id) {
        log.debug("REST request to get Score : {}", id);
        Optional<ScoreDTO> scoreDTO = scoreService.findOne(id);
        return ResponseUtil.wrapOrNotFound(scoreDTO);
    }

    /**
     * DELETE  /scores/:id : delete the "id" score.
     *
     * @param id the id of the scoreDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/scores/{id}")
    public ResponseEntity<Void> deleteScore(@PathVariable Long id) {
        log.debug("REST request to delete Score : {}", id);
        scoreService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
