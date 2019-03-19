package com.sportstream.myapp.web.rest;

import com.sportstream.myapp.SportstreamApp;

import com.sportstream.myapp.domain.Score;
import com.sportstream.myapp.repository.ScoreRepository;
import com.sportstream.myapp.service.ScoreService;
import com.sportstream.myapp.service.dto.ScoreDTO;
import com.sportstream.myapp.service.mapper.ScoreMapper;
import com.sportstream.myapp.web.rest.errors.ExceptionTranslator;
import com.sportstream.myapp.service.dto.ScoreCriteria;
import com.sportstream.myapp.service.ScoreQueryService;

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
 * Test class for the ScoreResource REST controller.
 *
 * @see ScoreResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SportstreamApp.class)
public class ScoreResourceIntTest {

    private static final Long DEFAULT_FIGHT_ID = 1L;
    private static final Long UPDATED_FIGHT_ID = 2L;

    private static final Long DEFAULT_ROUND_ID = 1L;
    private static final Long UPDATED_ROUND_ID = 2L;

    private static final Long DEFAULT_JUDGE_ID = 1L;
    private static final Long UPDATED_JUDGE_ID = 2L;

    private static final Double DEFAULT_VALUE = 1D;
    private static final Double UPDATED_VALUE = 2D;

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private ScoreMapper scoreMapper;

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private ScoreQueryService scoreQueryService;

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

    private MockMvc restScoreMockMvc;

    private Score score;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ScoreResource scoreResource = new ScoreResource(scoreService, scoreQueryService);
        this.restScoreMockMvc = MockMvcBuilders.standaloneSetup(scoreResource)
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
    public static Score createEntity(EntityManager em) {
        Score score = new Score()
            .fightId(DEFAULT_FIGHT_ID)
            .roundId(DEFAULT_ROUND_ID)
            .judgeId(DEFAULT_JUDGE_ID)
            .value(DEFAULT_VALUE);
        return score;
    }

    @Before
    public void initTest() {
        score = createEntity(em);
    }

    @Test
    @Transactional
    public void createScore() throws Exception {
        int databaseSizeBeforeCreate = scoreRepository.findAll().size();

        // Create the Score
        ScoreDTO scoreDTO = scoreMapper.toDto(score);
        restScoreMockMvc.perform(post("/api/scores")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(scoreDTO)))
            .andExpect(status().isCreated());

        // Validate the Score in the database
        List<Score> scoreList = scoreRepository.findAll();
        assertThat(scoreList).hasSize(databaseSizeBeforeCreate + 1);
        Score testScore = scoreList.get(scoreList.size() - 1);
        assertThat(testScore.getFightId()).isEqualTo(DEFAULT_FIGHT_ID);
        assertThat(testScore.getRoundId()).isEqualTo(DEFAULT_ROUND_ID);
        assertThat(testScore.getJudgeId()).isEqualTo(DEFAULT_JUDGE_ID);
        assertThat(testScore.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void createScoreWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = scoreRepository.findAll().size();

        // Create the Score with an existing ID
        score.setId(1L);
        ScoreDTO scoreDTO = scoreMapper.toDto(score);

        // An entity with an existing ID cannot be created, so this API call must fail
        restScoreMockMvc.perform(post("/api/scores")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(scoreDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Score in the database
        List<Score> scoreList = scoreRepository.findAll();
        assertThat(scoreList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkFightIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = scoreRepository.findAll().size();
        // set the field null
        score.setFightId(null);

        // Create the Score, which fails.
        ScoreDTO scoreDTO = scoreMapper.toDto(score);

        restScoreMockMvc.perform(post("/api/scores")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(scoreDTO)))
            .andExpect(status().isBadRequest());

        List<Score> scoreList = scoreRepository.findAll();
        assertThat(scoreList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRoundIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = scoreRepository.findAll().size();
        // set the field null
        score.setRoundId(null);

        // Create the Score, which fails.
        ScoreDTO scoreDTO = scoreMapper.toDto(score);

        restScoreMockMvc.perform(post("/api/scores")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(scoreDTO)))
            .andExpect(status().isBadRequest());

        List<Score> scoreList = scoreRepository.findAll();
        assertThat(scoreList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkJudgeIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = scoreRepository.findAll().size();
        // set the field null
        score.setJudgeId(null);

        // Create the Score, which fails.
        ScoreDTO scoreDTO = scoreMapper.toDto(score);

        restScoreMockMvc.perform(post("/api/scores")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(scoreDTO)))
            .andExpect(status().isBadRequest());

        List<Score> scoreList = scoreRepository.findAll();
        assertThat(scoreList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = scoreRepository.findAll().size();
        // set the field null
        score.setValue(null);

        // Create the Score, which fails.
        ScoreDTO scoreDTO = scoreMapper.toDto(score);

        restScoreMockMvc.perform(post("/api/scores")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(scoreDTO)))
            .andExpect(status().isBadRequest());

        List<Score> scoreList = scoreRepository.findAll();
        assertThat(scoreList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllScores() throws Exception {
        // Initialize the database
        scoreRepository.saveAndFlush(score);

        // Get all the scoreList
        restScoreMockMvc.perform(get("/api/scores?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(score.getId().intValue())))
            .andExpect(jsonPath("$.[*].fightId").value(hasItem(DEFAULT_FIGHT_ID.intValue())))
            .andExpect(jsonPath("$.[*].roundId").value(hasItem(DEFAULT_ROUND_ID.intValue())))
            .andExpect(jsonPath("$.[*].judgeId").value(hasItem(DEFAULT_JUDGE_ID.intValue())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getScore() throws Exception {
        // Initialize the database
        scoreRepository.saveAndFlush(score);

        // Get the score
        restScoreMockMvc.perform(get("/api/scores/{id}", score.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(score.getId().intValue()))
            .andExpect(jsonPath("$.fightId").value(DEFAULT_FIGHT_ID.intValue()))
            .andExpect(jsonPath("$.roundId").value(DEFAULT_ROUND_ID.intValue()))
            .andExpect(jsonPath("$.judgeId").value(DEFAULT_JUDGE_ID.intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.doubleValue()));
    }

    @Test
    @Transactional
    public void getAllScoresByFightIdIsEqualToSomething() throws Exception {
        // Initialize the database
        scoreRepository.saveAndFlush(score);

        // Get all the scoreList where fightId equals to DEFAULT_FIGHT_ID
        defaultScoreShouldBeFound("fightId.equals=" + DEFAULT_FIGHT_ID);

        // Get all the scoreList where fightId equals to UPDATED_FIGHT_ID
        defaultScoreShouldNotBeFound("fightId.equals=" + UPDATED_FIGHT_ID);
    }

    @Test
    @Transactional
    public void getAllScoresByFightIdIsInShouldWork() throws Exception {
        // Initialize the database
        scoreRepository.saveAndFlush(score);

        // Get all the scoreList where fightId in DEFAULT_FIGHT_ID or UPDATED_FIGHT_ID
        defaultScoreShouldBeFound("fightId.in=" + DEFAULT_FIGHT_ID + "," + UPDATED_FIGHT_ID);

        // Get all the scoreList where fightId equals to UPDATED_FIGHT_ID
        defaultScoreShouldNotBeFound("fightId.in=" + UPDATED_FIGHT_ID);
    }

    @Test
    @Transactional
    public void getAllScoresByFightIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        scoreRepository.saveAndFlush(score);

        // Get all the scoreList where fightId is not null
        defaultScoreShouldBeFound("fightId.specified=true");

        // Get all the scoreList where fightId is null
        defaultScoreShouldNotBeFound("fightId.specified=false");
    }

    @Test
    @Transactional
    public void getAllScoresByFightIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        scoreRepository.saveAndFlush(score);

        // Get all the scoreList where fightId greater than or equals to DEFAULT_FIGHT_ID
        defaultScoreShouldBeFound("fightId.greaterOrEqualThan=" + DEFAULT_FIGHT_ID);

        // Get all the scoreList where fightId greater than or equals to UPDATED_FIGHT_ID
        defaultScoreShouldNotBeFound("fightId.greaterOrEqualThan=" + UPDATED_FIGHT_ID);
    }

    @Test
    @Transactional
    public void getAllScoresByFightIdIsLessThanSomething() throws Exception {
        // Initialize the database
        scoreRepository.saveAndFlush(score);

        // Get all the scoreList where fightId less than or equals to DEFAULT_FIGHT_ID
        defaultScoreShouldNotBeFound("fightId.lessThan=" + DEFAULT_FIGHT_ID);

        // Get all the scoreList where fightId less than or equals to UPDATED_FIGHT_ID
        defaultScoreShouldBeFound("fightId.lessThan=" + UPDATED_FIGHT_ID);
    }


    @Test
    @Transactional
    public void getAllScoresByRoundIdIsEqualToSomething() throws Exception {
        // Initialize the database
        scoreRepository.saveAndFlush(score);

        // Get all the scoreList where roundId equals to DEFAULT_ROUND_ID
        defaultScoreShouldBeFound("roundId.equals=" + DEFAULT_ROUND_ID);

        // Get all the scoreList where roundId equals to UPDATED_ROUND_ID
        defaultScoreShouldNotBeFound("roundId.equals=" + UPDATED_ROUND_ID);
    }

    @Test
    @Transactional
    public void getAllScoresByRoundIdIsInShouldWork() throws Exception {
        // Initialize the database
        scoreRepository.saveAndFlush(score);

        // Get all the scoreList where roundId in DEFAULT_ROUND_ID or UPDATED_ROUND_ID
        defaultScoreShouldBeFound("roundId.in=" + DEFAULT_ROUND_ID + "," + UPDATED_ROUND_ID);

        // Get all the scoreList where roundId equals to UPDATED_ROUND_ID
        defaultScoreShouldNotBeFound("roundId.in=" + UPDATED_ROUND_ID);
    }

    @Test
    @Transactional
    public void getAllScoresByRoundIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        scoreRepository.saveAndFlush(score);

        // Get all the scoreList where roundId is not null
        defaultScoreShouldBeFound("roundId.specified=true");

        // Get all the scoreList where roundId is null
        defaultScoreShouldNotBeFound("roundId.specified=false");
    }

    @Test
    @Transactional
    public void getAllScoresByRoundIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        scoreRepository.saveAndFlush(score);

        // Get all the scoreList where roundId greater than or equals to DEFAULT_ROUND_ID
        defaultScoreShouldBeFound("roundId.greaterOrEqualThan=" + DEFAULT_ROUND_ID);

        // Get all the scoreList where roundId greater than or equals to UPDATED_ROUND_ID
        defaultScoreShouldNotBeFound("roundId.greaterOrEqualThan=" + UPDATED_ROUND_ID);
    }

    @Test
    @Transactional
    public void getAllScoresByRoundIdIsLessThanSomething() throws Exception {
        // Initialize the database
        scoreRepository.saveAndFlush(score);

        // Get all the scoreList where roundId less than or equals to DEFAULT_ROUND_ID
        defaultScoreShouldNotBeFound("roundId.lessThan=" + DEFAULT_ROUND_ID);

        // Get all the scoreList where roundId less than or equals to UPDATED_ROUND_ID
        defaultScoreShouldBeFound("roundId.lessThan=" + UPDATED_ROUND_ID);
    }


    @Test
    @Transactional
    public void getAllScoresByJudgeIdIsEqualToSomething() throws Exception {
        // Initialize the database
        scoreRepository.saveAndFlush(score);

        // Get all the scoreList where judgeId equals to DEFAULT_JUDGE_ID
        defaultScoreShouldBeFound("judgeId.equals=" + DEFAULT_JUDGE_ID);

        // Get all the scoreList where judgeId equals to UPDATED_JUDGE_ID
        defaultScoreShouldNotBeFound("judgeId.equals=" + UPDATED_JUDGE_ID);
    }

    @Test
    @Transactional
    public void getAllScoresByJudgeIdIsInShouldWork() throws Exception {
        // Initialize the database
        scoreRepository.saveAndFlush(score);

        // Get all the scoreList where judgeId in DEFAULT_JUDGE_ID or UPDATED_JUDGE_ID
        defaultScoreShouldBeFound("judgeId.in=" + DEFAULT_JUDGE_ID + "," + UPDATED_JUDGE_ID);

        // Get all the scoreList where judgeId equals to UPDATED_JUDGE_ID
        defaultScoreShouldNotBeFound("judgeId.in=" + UPDATED_JUDGE_ID);
    }

    @Test
    @Transactional
    public void getAllScoresByJudgeIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        scoreRepository.saveAndFlush(score);

        // Get all the scoreList where judgeId is not null
        defaultScoreShouldBeFound("judgeId.specified=true");

        // Get all the scoreList where judgeId is null
        defaultScoreShouldNotBeFound("judgeId.specified=false");
    }

    @Test
    @Transactional
    public void getAllScoresByJudgeIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        scoreRepository.saveAndFlush(score);

        // Get all the scoreList where judgeId greater than or equals to DEFAULT_JUDGE_ID
        defaultScoreShouldBeFound("judgeId.greaterOrEqualThan=" + DEFAULT_JUDGE_ID);

        // Get all the scoreList where judgeId greater than or equals to UPDATED_JUDGE_ID
        defaultScoreShouldNotBeFound("judgeId.greaterOrEqualThan=" + UPDATED_JUDGE_ID);
    }

    @Test
    @Transactional
    public void getAllScoresByJudgeIdIsLessThanSomething() throws Exception {
        // Initialize the database
        scoreRepository.saveAndFlush(score);

        // Get all the scoreList where judgeId less than or equals to DEFAULT_JUDGE_ID
        defaultScoreShouldNotBeFound("judgeId.lessThan=" + DEFAULT_JUDGE_ID);

        // Get all the scoreList where judgeId less than or equals to UPDATED_JUDGE_ID
        defaultScoreShouldBeFound("judgeId.lessThan=" + UPDATED_JUDGE_ID);
    }


    @Test
    @Transactional
    public void getAllScoresByValueIsEqualToSomething() throws Exception {
        // Initialize the database
        scoreRepository.saveAndFlush(score);

        // Get all the scoreList where value equals to DEFAULT_VALUE
        defaultScoreShouldBeFound("value.equals=" + DEFAULT_VALUE);

        // Get all the scoreList where value equals to UPDATED_VALUE
        defaultScoreShouldNotBeFound("value.equals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void getAllScoresByValueIsInShouldWork() throws Exception {
        // Initialize the database
        scoreRepository.saveAndFlush(score);

        // Get all the scoreList where value in DEFAULT_VALUE or UPDATED_VALUE
        defaultScoreShouldBeFound("value.in=" + DEFAULT_VALUE + "," + UPDATED_VALUE);

        // Get all the scoreList where value equals to UPDATED_VALUE
        defaultScoreShouldNotBeFound("value.in=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void getAllScoresByValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        scoreRepository.saveAndFlush(score);

        // Get all the scoreList where value is not null
        defaultScoreShouldBeFound("value.specified=true");

        // Get all the scoreList where value is null
        defaultScoreShouldNotBeFound("value.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultScoreShouldBeFound(String filter) throws Exception {
        restScoreMockMvc.perform(get("/api/scores?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(score.getId().intValue())))
            .andExpect(jsonPath("$.[*].fightId").value(hasItem(DEFAULT_FIGHT_ID.intValue())))
            .andExpect(jsonPath("$.[*].roundId").value(hasItem(DEFAULT_ROUND_ID.intValue())))
            .andExpect(jsonPath("$.[*].judgeId").value(hasItem(DEFAULT_JUDGE_ID.intValue())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.doubleValue())));

        // Check, that the count call also returns 1
        restScoreMockMvc.perform(get("/api/scores/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultScoreShouldNotBeFound(String filter) throws Exception {
        restScoreMockMvc.perform(get("/api/scores?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restScoreMockMvc.perform(get("/api/scores/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingScore() throws Exception {
        // Get the score
        restScoreMockMvc.perform(get("/api/scores/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateScore() throws Exception {
        // Initialize the database
        scoreRepository.saveAndFlush(score);

        int databaseSizeBeforeUpdate = scoreRepository.findAll().size();

        // Update the score
        Score updatedScore = scoreRepository.findById(score.getId()).get();
        // Disconnect from session so that the updates on updatedScore are not directly saved in db
        em.detach(updatedScore);
        updatedScore
            .fightId(UPDATED_FIGHT_ID)
            .roundId(UPDATED_ROUND_ID)
            .judgeId(UPDATED_JUDGE_ID)
            .value(UPDATED_VALUE);
        ScoreDTO scoreDTO = scoreMapper.toDto(updatedScore);

        restScoreMockMvc.perform(put("/api/scores")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(scoreDTO)))
            .andExpect(status().isOk());

        // Validate the Score in the database
        List<Score> scoreList = scoreRepository.findAll();
        assertThat(scoreList).hasSize(databaseSizeBeforeUpdate);
        Score testScore = scoreList.get(scoreList.size() - 1);
        assertThat(testScore.getFightId()).isEqualTo(UPDATED_FIGHT_ID);
        assertThat(testScore.getRoundId()).isEqualTo(UPDATED_ROUND_ID);
        assertThat(testScore.getJudgeId()).isEqualTo(UPDATED_JUDGE_ID);
        assertThat(testScore.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void updateNonExistingScore() throws Exception {
        int databaseSizeBeforeUpdate = scoreRepository.findAll().size();

        // Create the Score
        ScoreDTO scoreDTO = scoreMapper.toDto(score);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restScoreMockMvc.perform(put("/api/scores")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(scoreDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Score in the database
        List<Score> scoreList = scoreRepository.findAll();
        assertThat(scoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteScore() throws Exception {
        // Initialize the database
        scoreRepository.saveAndFlush(score);

        int databaseSizeBeforeDelete = scoreRepository.findAll().size();

        // Delete the score
        restScoreMockMvc.perform(delete("/api/scores/{id}", score.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Score> scoreList = scoreRepository.findAll();
        assertThat(scoreList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Score.class);
        Score score1 = new Score();
        score1.setId(1L);
        Score score2 = new Score();
        score2.setId(score1.getId());
        assertThat(score1).isEqualTo(score2);
        score2.setId(2L);
        assertThat(score1).isNotEqualTo(score2);
        score1.setId(null);
        assertThat(score1).isNotEqualTo(score2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ScoreDTO.class);
        ScoreDTO scoreDTO1 = new ScoreDTO();
        scoreDTO1.setId(1L);
        ScoreDTO scoreDTO2 = new ScoreDTO();
        assertThat(scoreDTO1).isNotEqualTo(scoreDTO2);
        scoreDTO2.setId(scoreDTO1.getId());
        assertThat(scoreDTO1).isEqualTo(scoreDTO2);
        scoreDTO2.setId(2L);
        assertThat(scoreDTO1).isNotEqualTo(scoreDTO2);
        scoreDTO1.setId(null);
        assertThat(scoreDTO1).isNotEqualTo(scoreDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(scoreMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(scoreMapper.fromId(null)).isNull();
    }
}
