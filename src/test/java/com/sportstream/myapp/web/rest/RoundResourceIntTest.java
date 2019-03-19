package com.sportstream.myapp.web.rest;

import com.sportstream.myapp.SportstreamApp;

import com.sportstream.myapp.domain.Round;
import com.sportstream.myapp.domain.Fight;
import com.sportstream.myapp.repository.RoundRepository;
import com.sportstream.myapp.service.RoundService;
import com.sportstream.myapp.service.dto.RoundDTO;
import com.sportstream.myapp.service.mapper.RoundMapper;
import com.sportstream.myapp.web.rest.errors.ExceptionTranslator;
import com.sportstream.myapp.service.dto.RoundCriteria;
import com.sportstream.myapp.service.RoundQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;


import static com.sportstream.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the RoundResource REST controller.
 *
 * @see RoundResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SportstreamApp.class)
public class RoundResourceIntTest {

    private static final Double DEFAULT_RED_TOTAL_SCORE = 1D;
    private static final Double UPDATED_RED_TOTAL_SCORE = 2D;

    private static final Double DEFAULT_BLUE_TOTAL_SCORE = 1D;
    private static final Double UPDATED_BLUE_TOTAL_SCORE = 2D;

    @Autowired
    private RoundRepository roundRepository;

    @Autowired
    private RoundMapper roundMapper;

    @Autowired
    private RoundService roundService;

    @Autowired
    private RoundQueryService roundQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restRoundMockMvc;

    private Round round;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RoundResource roundResource = new RoundResource(roundService, roundQueryService);
        this.restRoundMockMvc = MockMvcBuilders.standaloneSetup(roundResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Round createEntity(EntityManager em) {
        Round round = new Round()
            .redTotalScore(DEFAULT_RED_TOTAL_SCORE)
            .blueTotalScore(DEFAULT_BLUE_TOTAL_SCORE);
        // Add required entity
        Fight fight = FightResourceIntTest.createEntity(em);
        em.persist(fight);
        em.flush();
        round.setFight(fight);
        return round;
    }

    @Before
    public void initTest() {
        round = createEntity(em);
    }

    @Test
    @Transactional
    public void createRound() throws Exception {
        int databaseSizeBeforeCreate = roundRepository.findAll().size();

        // Create the Round
        RoundDTO roundDTO = roundMapper.toDto(round);
        restRoundMockMvc.perform(post("/api/rounds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roundDTO)))
            .andExpect(status().isCreated());

        // Validate the Round in the database
        List<Round> roundList = roundRepository.findAll();
        assertThat(roundList).hasSize(databaseSizeBeforeCreate + 1);
        Round testRound = roundList.get(roundList.size() - 1);
        assertThat(testRound.getRedTotalScore()).isEqualTo(DEFAULT_RED_TOTAL_SCORE);
        assertThat(testRound.getBlueTotalScore()).isEqualTo(DEFAULT_BLUE_TOTAL_SCORE);
    }

    @Test
    @Transactional
    public void createRoundWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = roundRepository.findAll().size();

        // Create the Round with an existing ID
        round.setId(1L);
        RoundDTO roundDTO = roundMapper.toDto(round);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRoundMockMvc.perform(post("/api/rounds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roundDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Round in the database
        List<Round> roundList = roundRepository.findAll();
        assertThat(roundList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllRounds() throws Exception {
        // Initialize the database
        roundRepository.saveAndFlush(round);

        // Get all the roundList
        restRoundMockMvc.perform(get("/api/rounds?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(round.getId().intValue())))
            .andExpect(jsonPath("$.[*].redTotalScore").value(hasItem(DEFAULT_RED_TOTAL_SCORE.doubleValue())))
            .andExpect(jsonPath("$.[*].blueTotalScore").value(hasItem(DEFAULT_BLUE_TOTAL_SCORE.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getRound() throws Exception {
        // Initialize the database
        roundRepository.saveAndFlush(round);

        // Get the round
        restRoundMockMvc.perform(get("/api/rounds/{id}", round.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(round.getId().intValue()))
            .andExpect(jsonPath("$.redTotalScore").value(DEFAULT_RED_TOTAL_SCORE.doubleValue()))
            .andExpect(jsonPath("$.blueTotalScore").value(DEFAULT_BLUE_TOTAL_SCORE.doubleValue()));
    }

    @Test
    @Transactional
    public void getAllRoundsByRedTotalScoreIsEqualToSomething() throws Exception {
        // Initialize the database
        roundRepository.saveAndFlush(round);

        // Get all the roundList where redTotalScore equals to DEFAULT_RED_TOTAL_SCORE
        defaultRoundShouldBeFound("redTotalScore.equals=" + DEFAULT_RED_TOTAL_SCORE);

        // Get all the roundList where redTotalScore equals to UPDATED_RED_TOTAL_SCORE
        defaultRoundShouldNotBeFound("redTotalScore.equals=" + UPDATED_RED_TOTAL_SCORE);
    }

    @Test
    @Transactional
    public void getAllRoundsByRedTotalScoreIsInShouldWork() throws Exception {
        // Initialize the database
        roundRepository.saveAndFlush(round);

        // Get all the roundList where redTotalScore in DEFAULT_RED_TOTAL_SCORE or UPDATED_RED_TOTAL_SCORE
        defaultRoundShouldBeFound("redTotalScore.in=" + DEFAULT_RED_TOTAL_SCORE + "," + UPDATED_RED_TOTAL_SCORE);

        // Get all the roundList where redTotalScore equals to UPDATED_RED_TOTAL_SCORE
        defaultRoundShouldNotBeFound("redTotalScore.in=" + UPDATED_RED_TOTAL_SCORE);
    }

    @Test
    @Transactional
    public void getAllRoundsByRedTotalScoreIsNullOrNotNull() throws Exception {
        // Initialize the database
        roundRepository.saveAndFlush(round);

        // Get all the roundList where redTotalScore is not null
        defaultRoundShouldBeFound("redTotalScore.specified=true");

        // Get all the roundList where redTotalScore is null
        defaultRoundShouldNotBeFound("redTotalScore.specified=false");
    }

    @Test
    @Transactional
    public void getAllRoundsByBlueTotalScoreIsEqualToSomething() throws Exception {
        // Initialize the database
        roundRepository.saveAndFlush(round);

        // Get all the roundList where blueTotalScore equals to DEFAULT_BLUE_TOTAL_SCORE
        defaultRoundShouldBeFound("blueTotalScore.equals=" + DEFAULT_BLUE_TOTAL_SCORE);

        // Get all the roundList where blueTotalScore equals to UPDATED_BLUE_TOTAL_SCORE
        defaultRoundShouldNotBeFound("blueTotalScore.equals=" + UPDATED_BLUE_TOTAL_SCORE);
    }

    @Test
    @Transactional
    public void getAllRoundsByBlueTotalScoreIsInShouldWork() throws Exception {
        // Initialize the database
        roundRepository.saveAndFlush(round);

        // Get all the roundList where blueTotalScore in DEFAULT_BLUE_TOTAL_SCORE or UPDATED_BLUE_TOTAL_SCORE
        defaultRoundShouldBeFound("blueTotalScore.in=" + DEFAULT_BLUE_TOTAL_SCORE + "," + UPDATED_BLUE_TOTAL_SCORE);

        // Get all the roundList where blueTotalScore equals to UPDATED_BLUE_TOTAL_SCORE
        defaultRoundShouldNotBeFound("blueTotalScore.in=" + UPDATED_BLUE_TOTAL_SCORE);
    }

    @Test
    @Transactional
    public void getAllRoundsByBlueTotalScoreIsNullOrNotNull() throws Exception {
        // Initialize the database
        roundRepository.saveAndFlush(round);

        // Get all the roundList where blueTotalScore is not null
        defaultRoundShouldBeFound("blueTotalScore.specified=true");

        // Get all the roundList where blueTotalScore is null
        defaultRoundShouldNotBeFound("blueTotalScore.specified=false");
    }

    @Test
    @Transactional
    public void getAllRoundsByFightIsEqualToSomething() throws Exception {
        // Initialize the database
        Fight fight = FightResourceIntTest.createEntity(em);
        em.persist(fight);
        em.flush();
        round.setFight(fight);
        roundRepository.saveAndFlush(round);
        Long fightId = fight.getId();

        // Get all the roundList where fight equals to fightId
        defaultRoundShouldBeFound("fightId.equals=" + fightId);

        // Get all the roundList where fight equals to fightId + 1
        defaultRoundShouldNotBeFound("fightId.equals=" + (fightId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultRoundShouldBeFound(String filter) throws Exception {
        restRoundMockMvc.perform(get("/api/rounds?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(round.getId().intValue())))
            .andExpect(jsonPath("$.[*].redTotalScore").value(hasItem(DEFAULT_RED_TOTAL_SCORE.doubleValue())))
            .andExpect(jsonPath("$.[*].blueTotalScore").value(hasItem(DEFAULT_BLUE_TOTAL_SCORE.doubleValue())));

        // Check, that the count call also returns 1
        restRoundMockMvc.perform(get("/api/rounds/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultRoundShouldNotBeFound(String filter) throws Exception {
        restRoundMockMvc.perform(get("/api/rounds?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRoundMockMvc.perform(get("/api/rounds/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingRound() throws Exception {
        // Get the round
        restRoundMockMvc.perform(get("/api/rounds/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRound() throws Exception {
        // Initialize the database
        roundRepository.saveAndFlush(round);

        int databaseSizeBeforeUpdate = roundRepository.findAll().size();

        // Update the round
        Round updatedRound = roundRepository.findById(round.getId()).get();
        // Disconnect from session so that the updates on updatedRound are not directly saved in db
        em.detach(updatedRound);
        updatedRound
            .redTotalScore(UPDATED_RED_TOTAL_SCORE)
            .blueTotalScore(UPDATED_BLUE_TOTAL_SCORE);
        RoundDTO roundDTO = roundMapper.toDto(updatedRound);

        restRoundMockMvc.perform(put("/api/rounds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roundDTO)))
            .andExpect(status().isOk());

        // Validate the Round in the database
        List<Round> roundList = roundRepository.findAll();
        assertThat(roundList).hasSize(databaseSizeBeforeUpdate);
        Round testRound = roundList.get(roundList.size() - 1);
        assertThat(testRound.getRedTotalScore()).isEqualTo(UPDATED_RED_TOTAL_SCORE);
        assertThat(testRound.getBlueTotalScore()).isEqualTo(UPDATED_BLUE_TOTAL_SCORE);
    }

    @Test
    @Transactional
    public void updateNonExistingRound() throws Exception {
        int databaseSizeBeforeUpdate = roundRepository.findAll().size();

        // Create the Round
        RoundDTO roundDTO = roundMapper.toDto(round);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoundMockMvc.perform(put("/api/rounds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roundDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Round in the database
        List<Round> roundList = roundRepository.findAll();
        assertThat(roundList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRound() throws Exception {
        // Initialize the database
        roundRepository.saveAndFlush(round);

        int databaseSizeBeforeDelete = roundRepository.findAll().size();

        // Delete the round
        restRoundMockMvc.perform(delete("/api/rounds/{id}", round.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Round> roundList = roundRepository.findAll();
        assertThat(roundList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Round.class);
        Round round1 = new Round();
        round1.setId(1L);
        Round round2 = new Round();
        round2.setId(round1.getId());
        assertThat(round1).isEqualTo(round2);
        round2.setId(2L);
        assertThat(round1).isNotEqualTo(round2);
        round1.setId(null);
        assertThat(round1).isNotEqualTo(round2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RoundDTO.class);
        RoundDTO roundDTO1 = new RoundDTO();
        roundDTO1.setId(1L);
        RoundDTO roundDTO2 = new RoundDTO();
        assertThat(roundDTO1).isNotEqualTo(roundDTO2);
        roundDTO2.setId(roundDTO1.getId());
        assertThat(roundDTO1).isEqualTo(roundDTO2);
        roundDTO2.setId(2L);
        assertThat(roundDTO1).isNotEqualTo(roundDTO2);
        roundDTO1.setId(null);
        assertThat(roundDTO1).isNotEqualTo(roundDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(roundMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(roundMapper.fromId(null)).isNull();
    }
}
