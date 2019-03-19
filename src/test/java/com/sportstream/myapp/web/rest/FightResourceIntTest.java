package com.sportstream.myapp.web.rest;

import com.sportstream.myapp.SportstreamApp;

import com.sportstream.myapp.domain.Fight;
import com.sportstream.myapp.domain.Round;
import com.sportstream.myapp.repository.FightRepository;
import com.sportstream.myapp.service.FightService;
import com.sportstream.myapp.service.dto.FightDTO;
import com.sportstream.myapp.service.mapper.FightMapper;
import com.sportstream.myapp.web.rest.errors.ExceptionTranslator;
import com.sportstream.myapp.service.dto.FightCriteria;
import com.sportstream.myapp.service.FightQueryService;

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

import com.sportstream.myapp.domain.enumeration.ResultEnum;
import com.sportstream.myapp.domain.enumeration.TypeEnum;
/**
 * Test class for the FightResource REST controller.
 *
 * @see FightResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SportstreamApp.class)
public class FightResourceIntTest {

    private static final String DEFAULT_RED_CORNER = "AAAAAAAAAA";
    private static final String UPDATED_RED_CORNER = "BBBBBBBBBB";

    private static final String DEFAULT_BLUE_CORNER = "AAAAAAAAAA";
    private static final String UPDATED_BLUE_CORNER = "BBBBBBBBBB";

    private static final ResultEnum DEFAULT_RESULT = ResultEnum.PENDING;
    private static final ResultEnum UPDATED_RESULT = ResultEnum.IN_PROGRESS;

    private static final TypeEnum DEFAULT_RESULT_TYPE = TypeEnum.RED_WIN;
    private static final TypeEnum UPDATED_RESULT_TYPE = TypeEnum.BLUE_WIN;

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    @Autowired
    private FightRepository fightRepository;

    @Autowired
    private FightMapper fightMapper;

    @Autowired
    private FightService fightService;

    @Autowired
    private FightQueryService fightQueryService;

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

    private MockMvc restFightMockMvc;

    private Fight fight;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FightResource fightResource = new FightResource(fightService, fightQueryService);
        this.restFightMockMvc = MockMvcBuilders.standaloneSetup(fightResource)
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
    public static Fight createEntity(EntityManager em) {
        Fight fight = new Fight()
            .redCorner(DEFAULT_RED_CORNER)
            .blueCorner(DEFAULT_BLUE_CORNER)
            .result(DEFAULT_RESULT)
            .resultType(DEFAULT_RESULT_TYPE)
            .comment(DEFAULT_COMMENT);
        return fight;
    }

    @Before
    public void initTest() {
        fight = createEntity(em);
    }

    @Test
    @Transactional
    public void createFight() throws Exception {
        int databaseSizeBeforeCreate = fightRepository.findAll().size();

        // Create the Fight
        FightDTO fightDTO = fightMapper.toDto(fight);
        restFightMockMvc.perform(post("/api/fights")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fightDTO)))
            .andExpect(status().isCreated());

        // Validate the Fight in the database
        List<Fight> fightList = fightRepository.findAll();
        assertThat(fightList).hasSize(databaseSizeBeforeCreate + 1);
        Fight testFight = fightList.get(fightList.size() - 1);
        assertThat(testFight.getRedCorner()).isEqualTo(DEFAULT_RED_CORNER);
        assertThat(testFight.getBlueCorner()).isEqualTo(DEFAULT_BLUE_CORNER);
        assertThat(testFight.getResult()).isEqualTo(DEFAULT_RESULT);
        assertThat(testFight.getResultType()).isEqualTo(DEFAULT_RESULT_TYPE);
        assertThat(testFight.getComment()).isEqualTo(DEFAULT_COMMENT);
    }

    @Test
    @Transactional
    public void createFightWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = fightRepository.findAll().size();

        // Create the Fight with an existing ID
        fight.setId(1L);
        FightDTO fightDTO = fightMapper.toDto(fight);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFightMockMvc.perform(post("/api/fights")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fightDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Fight in the database
        List<Fight> fightList = fightRepository.findAll();
        assertThat(fightList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkRedCornerIsRequired() throws Exception {
        int databaseSizeBeforeTest = fightRepository.findAll().size();
        // set the field null
        fight.setRedCorner(null);

        // Create the Fight, which fails.
        FightDTO fightDTO = fightMapper.toDto(fight);

        restFightMockMvc.perform(post("/api/fights")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fightDTO)))
            .andExpect(status().isBadRequest());

        List<Fight> fightList = fightRepository.findAll();
        assertThat(fightList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBlueCornerIsRequired() throws Exception {
        int databaseSizeBeforeTest = fightRepository.findAll().size();
        // set the field null
        fight.setBlueCorner(null);

        // Create the Fight, which fails.
        FightDTO fightDTO = fightMapper.toDto(fight);

        restFightMockMvc.perform(post("/api/fights")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fightDTO)))
            .andExpect(status().isBadRequest());

        List<Fight> fightList = fightRepository.findAll();
        assertThat(fightList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkResultIsRequired() throws Exception {
        int databaseSizeBeforeTest = fightRepository.findAll().size();
        // set the field null
        fight.setResult(null);

        // Create the Fight, which fails.
        FightDTO fightDTO = fightMapper.toDto(fight);

        restFightMockMvc.perform(post("/api/fights")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fightDTO)))
            .andExpect(status().isBadRequest());

        List<Fight> fightList = fightRepository.findAll();
        assertThat(fightList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkResultTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = fightRepository.findAll().size();
        // set the field null
        fight.setResultType(null);

        // Create the Fight, which fails.
        FightDTO fightDTO = fightMapper.toDto(fight);

        restFightMockMvc.perform(post("/api/fights")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fightDTO)))
            .andExpect(status().isBadRequest());

        List<Fight> fightList = fightRepository.findAll();
        assertThat(fightList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFights() throws Exception {
        // Initialize the database
        fightRepository.saveAndFlush(fight);

        // Get all the fightList
        restFightMockMvc.perform(get("/api/fights?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fight.getId().intValue())))
            .andExpect(jsonPath("$.[*].redCorner").value(hasItem(DEFAULT_RED_CORNER.toString())))
            .andExpect(jsonPath("$.[*].blueCorner").value(hasItem(DEFAULT_BLUE_CORNER.toString())))
            .andExpect(jsonPath("$.[*].result").value(hasItem(DEFAULT_RESULT.toString())))
            .andExpect(jsonPath("$.[*].resultType").value(hasItem(DEFAULT_RESULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())));
    }
    
    @Test
    @Transactional
    public void getFight() throws Exception {
        // Initialize the database
        fightRepository.saveAndFlush(fight);

        // Get the fight
        restFightMockMvc.perform(get("/api/fights/{id}", fight.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(fight.getId().intValue()))
            .andExpect(jsonPath("$.redCorner").value(DEFAULT_RED_CORNER.toString()))
            .andExpect(jsonPath("$.blueCorner").value(DEFAULT_BLUE_CORNER.toString()))
            .andExpect(jsonPath("$.result").value(DEFAULT_RESULT.toString()))
            .andExpect(jsonPath("$.resultType").value(DEFAULT_RESULT_TYPE.toString()))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT.toString()));
    }

    @Test
    @Transactional
    public void getAllFightsByRedCornerIsEqualToSomething() throws Exception {
        // Initialize the database
        fightRepository.saveAndFlush(fight);

        // Get all the fightList where redCorner equals to DEFAULT_RED_CORNER
        defaultFightShouldBeFound("redCorner.equals=" + DEFAULT_RED_CORNER);

        // Get all the fightList where redCorner equals to UPDATED_RED_CORNER
        defaultFightShouldNotBeFound("redCorner.equals=" + UPDATED_RED_CORNER);
    }

    @Test
    @Transactional
    public void getAllFightsByRedCornerIsInShouldWork() throws Exception {
        // Initialize the database
        fightRepository.saveAndFlush(fight);

        // Get all the fightList where redCorner in DEFAULT_RED_CORNER or UPDATED_RED_CORNER
        defaultFightShouldBeFound("redCorner.in=" + DEFAULT_RED_CORNER + "," + UPDATED_RED_CORNER);

        // Get all the fightList where redCorner equals to UPDATED_RED_CORNER
        defaultFightShouldNotBeFound("redCorner.in=" + UPDATED_RED_CORNER);
    }

    @Test
    @Transactional
    public void getAllFightsByRedCornerIsNullOrNotNull() throws Exception {
        // Initialize the database
        fightRepository.saveAndFlush(fight);

        // Get all the fightList where redCorner is not null
        defaultFightShouldBeFound("redCorner.specified=true");

        // Get all the fightList where redCorner is null
        defaultFightShouldNotBeFound("redCorner.specified=false");
    }

    @Test
    @Transactional
    public void getAllFightsByBlueCornerIsEqualToSomething() throws Exception {
        // Initialize the database
        fightRepository.saveAndFlush(fight);

        // Get all the fightList where blueCorner equals to DEFAULT_BLUE_CORNER
        defaultFightShouldBeFound("blueCorner.equals=" + DEFAULT_BLUE_CORNER);

        // Get all the fightList where blueCorner equals to UPDATED_BLUE_CORNER
        defaultFightShouldNotBeFound("blueCorner.equals=" + UPDATED_BLUE_CORNER);
    }

    @Test
    @Transactional
    public void getAllFightsByBlueCornerIsInShouldWork() throws Exception {
        // Initialize the database
        fightRepository.saveAndFlush(fight);

        // Get all the fightList where blueCorner in DEFAULT_BLUE_CORNER or UPDATED_BLUE_CORNER
        defaultFightShouldBeFound("blueCorner.in=" + DEFAULT_BLUE_CORNER + "," + UPDATED_BLUE_CORNER);

        // Get all the fightList where blueCorner equals to UPDATED_BLUE_CORNER
        defaultFightShouldNotBeFound("blueCorner.in=" + UPDATED_BLUE_CORNER);
    }

    @Test
    @Transactional
    public void getAllFightsByBlueCornerIsNullOrNotNull() throws Exception {
        // Initialize the database
        fightRepository.saveAndFlush(fight);

        // Get all the fightList where blueCorner is not null
        defaultFightShouldBeFound("blueCorner.specified=true");

        // Get all the fightList where blueCorner is null
        defaultFightShouldNotBeFound("blueCorner.specified=false");
    }

    @Test
    @Transactional
    public void getAllFightsByResultIsEqualToSomething() throws Exception {
        // Initialize the database
        fightRepository.saveAndFlush(fight);

        // Get all the fightList where result equals to DEFAULT_RESULT
        defaultFightShouldBeFound("result.equals=" + DEFAULT_RESULT);

        // Get all the fightList where result equals to UPDATED_RESULT
        defaultFightShouldNotBeFound("result.equals=" + UPDATED_RESULT);
    }

    @Test
    @Transactional
    public void getAllFightsByResultIsInShouldWork() throws Exception {
        // Initialize the database
        fightRepository.saveAndFlush(fight);

        // Get all the fightList where result in DEFAULT_RESULT or UPDATED_RESULT
        defaultFightShouldBeFound("result.in=" + DEFAULT_RESULT + "," + UPDATED_RESULT);

        // Get all the fightList where result equals to UPDATED_RESULT
        defaultFightShouldNotBeFound("result.in=" + UPDATED_RESULT);
    }

    @Test
    @Transactional
    public void getAllFightsByResultIsNullOrNotNull() throws Exception {
        // Initialize the database
        fightRepository.saveAndFlush(fight);

        // Get all the fightList where result is not null
        defaultFightShouldBeFound("result.specified=true");

        // Get all the fightList where result is null
        defaultFightShouldNotBeFound("result.specified=false");
    }

    @Test
    @Transactional
    public void getAllFightsByResultTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        fightRepository.saveAndFlush(fight);

        // Get all the fightList where resultType equals to DEFAULT_RESULT_TYPE
        defaultFightShouldBeFound("resultType.equals=" + DEFAULT_RESULT_TYPE);

        // Get all the fightList where resultType equals to UPDATED_RESULT_TYPE
        defaultFightShouldNotBeFound("resultType.equals=" + UPDATED_RESULT_TYPE);
    }

    @Test
    @Transactional
    public void getAllFightsByResultTypeIsInShouldWork() throws Exception {
        // Initialize the database
        fightRepository.saveAndFlush(fight);

        // Get all the fightList where resultType in DEFAULT_RESULT_TYPE or UPDATED_RESULT_TYPE
        defaultFightShouldBeFound("resultType.in=" + DEFAULT_RESULT_TYPE + "," + UPDATED_RESULT_TYPE);

        // Get all the fightList where resultType equals to UPDATED_RESULT_TYPE
        defaultFightShouldNotBeFound("resultType.in=" + UPDATED_RESULT_TYPE);
    }

    @Test
    @Transactional
    public void getAllFightsByResultTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        fightRepository.saveAndFlush(fight);

        // Get all the fightList where resultType is not null
        defaultFightShouldBeFound("resultType.specified=true");

        // Get all the fightList where resultType is null
        defaultFightShouldNotBeFound("resultType.specified=false");
    }

    @Test
    @Transactional
    public void getAllFightsByCommentIsEqualToSomething() throws Exception {
        // Initialize the database
        fightRepository.saveAndFlush(fight);

        // Get all the fightList where comment equals to DEFAULT_COMMENT
        defaultFightShouldBeFound("comment.equals=" + DEFAULT_COMMENT);

        // Get all the fightList where comment equals to UPDATED_COMMENT
        defaultFightShouldNotBeFound("comment.equals=" + UPDATED_COMMENT);
    }

    @Test
    @Transactional
    public void getAllFightsByCommentIsInShouldWork() throws Exception {
        // Initialize the database
        fightRepository.saveAndFlush(fight);

        // Get all the fightList where comment in DEFAULT_COMMENT or UPDATED_COMMENT
        defaultFightShouldBeFound("comment.in=" + DEFAULT_COMMENT + "," + UPDATED_COMMENT);

        // Get all the fightList where comment equals to UPDATED_COMMENT
        defaultFightShouldNotBeFound("comment.in=" + UPDATED_COMMENT);
    }

    @Test
    @Transactional
    public void getAllFightsByCommentIsNullOrNotNull() throws Exception {
        // Initialize the database
        fightRepository.saveAndFlush(fight);

        // Get all the fightList where comment is not null
        defaultFightShouldBeFound("comment.specified=true");

        // Get all the fightList where comment is null
        defaultFightShouldNotBeFound("comment.specified=false");
    }

    @Test
    @Transactional
    public void getAllFightsByRoundIsEqualToSomething() throws Exception {
        // Initialize the database
        Round round = RoundResourceIntTest.createEntity(em);
        em.persist(round);
        em.flush();
        fight.addRound(round);
        fightRepository.saveAndFlush(fight);
        Long roundId = round.getId();

        // Get all the fightList where round equals to roundId
        defaultFightShouldBeFound("roundId.equals=" + roundId);

        // Get all the fightList where round equals to roundId + 1
        defaultFightShouldNotBeFound("roundId.equals=" + (roundId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultFightShouldBeFound(String filter) throws Exception {
        restFightMockMvc.perform(get("/api/fights?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fight.getId().intValue())))
            .andExpect(jsonPath("$.[*].redCorner").value(hasItem(DEFAULT_RED_CORNER)))
            .andExpect(jsonPath("$.[*].blueCorner").value(hasItem(DEFAULT_BLUE_CORNER)))
            .andExpect(jsonPath("$.[*].result").value(hasItem(DEFAULT_RESULT.toString())))
            .andExpect(jsonPath("$.[*].resultType").value(hasItem(DEFAULT_RESULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)));

        // Check, that the count call also returns 1
        restFightMockMvc.perform(get("/api/fights/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultFightShouldNotBeFound(String filter) throws Exception {
        restFightMockMvc.perform(get("/api/fights?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFightMockMvc.perform(get("/api/fights/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingFight() throws Exception {
        // Get the fight
        restFightMockMvc.perform(get("/api/fights/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFight() throws Exception {
        // Initialize the database
        fightRepository.saveAndFlush(fight);

        int databaseSizeBeforeUpdate = fightRepository.findAll().size();

        // Update the fight
        Fight updatedFight = fightRepository.findById(fight.getId()).get();
        // Disconnect from session so that the updates on updatedFight are not directly saved in db
        em.detach(updatedFight);
        updatedFight
            .redCorner(UPDATED_RED_CORNER)
            .blueCorner(UPDATED_BLUE_CORNER)
            .result(UPDATED_RESULT)
            .resultType(UPDATED_RESULT_TYPE)
            .comment(UPDATED_COMMENT);
        FightDTO fightDTO = fightMapper.toDto(updatedFight);

        restFightMockMvc.perform(put("/api/fights")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fightDTO)))
            .andExpect(status().isOk());

        // Validate the Fight in the database
        List<Fight> fightList = fightRepository.findAll();
        assertThat(fightList).hasSize(databaseSizeBeforeUpdate);
        Fight testFight = fightList.get(fightList.size() - 1);
        assertThat(testFight.getRedCorner()).isEqualTo(UPDATED_RED_CORNER);
        assertThat(testFight.getBlueCorner()).isEqualTo(UPDATED_BLUE_CORNER);
        assertThat(testFight.getResult()).isEqualTo(UPDATED_RESULT);
        assertThat(testFight.getResultType()).isEqualTo(UPDATED_RESULT_TYPE);
        assertThat(testFight.getComment()).isEqualTo(UPDATED_COMMENT);
    }

    @Test
    @Transactional
    public void updateNonExistingFight() throws Exception {
        int databaseSizeBeforeUpdate = fightRepository.findAll().size();

        // Create the Fight
        FightDTO fightDTO = fightMapper.toDto(fight);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFightMockMvc.perform(put("/api/fights")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fightDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Fight in the database
        List<Fight> fightList = fightRepository.findAll();
        assertThat(fightList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFight() throws Exception {
        // Initialize the database
        fightRepository.saveAndFlush(fight);

        int databaseSizeBeforeDelete = fightRepository.findAll().size();

        // Delete the fight
        restFightMockMvc.perform(delete("/api/fights/{id}", fight.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Fight> fightList = fightRepository.findAll();
        assertThat(fightList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Fight.class);
        Fight fight1 = new Fight();
        fight1.setId(1L);
        Fight fight2 = new Fight();
        fight2.setId(fight1.getId());
        assertThat(fight1).isEqualTo(fight2);
        fight2.setId(2L);
        assertThat(fight1).isNotEqualTo(fight2);
        fight1.setId(null);
        assertThat(fight1).isNotEqualTo(fight2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FightDTO.class);
        FightDTO fightDTO1 = new FightDTO();
        fightDTO1.setId(1L);
        FightDTO fightDTO2 = new FightDTO();
        assertThat(fightDTO1).isNotEqualTo(fightDTO2);
        fightDTO2.setId(fightDTO1.getId());
        assertThat(fightDTO1).isEqualTo(fightDTO2);
        fightDTO2.setId(2L);
        assertThat(fightDTO1).isNotEqualTo(fightDTO2);
        fightDTO1.setId(null);
        assertThat(fightDTO1).isNotEqualTo(fightDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(fightMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(fightMapper.fromId(null)).isNull();
    }
}
