package com.sportstream.myapp.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.sportstream.myapp.domain.Fight;
import com.sportstream.myapp.domain.*; // for static metamodels
import com.sportstream.myapp.repository.FightRepository;
import com.sportstream.myapp.service.dto.FightCriteria;
import com.sportstream.myapp.service.dto.FightDTO;
import com.sportstream.myapp.service.mapper.FightMapper;

/**
 * Service for executing complex queries for Fight entities in the database.
 * The main input is a {@link FightCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FightDTO} or a {@link Page} of {@link FightDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FightQueryService extends QueryService<Fight> {

    private final Logger log = LoggerFactory.getLogger(FightQueryService.class);

    private final FightRepository fightRepository;

    private final FightMapper fightMapper;

    public FightQueryService(FightRepository fightRepository, FightMapper fightMapper) {
        this.fightRepository = fightRepository;
        this.fightMapper = fightMapper;
    }

    /**
     * Return a {@link List} of {@link FightDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FightDTO> findByCriteria(FightCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Fight> specification = createSpecification(criteria);
        return fightMapper.toDto(fightRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FightDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FightDTO> findByCriteria(FightCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Fight> specification = createSpecification(criteria);
        return fightRepository.findAll(specification, page)
            .map(fightMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FightCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Fight> specification = createSpecification(criteria);
        return fightRepository.count(specification);
    }

    /**
     * Function to convert FightCriteria to a {@link Specification}
     */
    private Specification<Fight> createSpecification(FightCriteria criteria) {
        Specification<Fight> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Fight_.id));
            }
            if (criteria.getRedCorner() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRedCorner(), Fight_.redCorner));
            }
            if (criteria.getBlueCorner() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBlueCorner(), Fight_.blueCorner));
            }
            if (criteria.getResult() != null) {
                specification = specification.and(buildSpecification(criteria.getResult(), Fight_.result));
            }
            if (criteria.getResultType() != null) {
                specification = specification.and(buildSpecification(criteria.getResultType(), Fight_.resultType));
            }
            if (criteria.getComment() != null) {
                specification = specification.and(buildStringSpecification(criteria.getComment(), Fight_.comment));
            }
            if (criteria.getRoundId() != null) {
                specification = specification.and(buildSpecification(criteria.getRoundId(),
                    root -> root.join(Fight_.rounds, JoinType.LEFT).get(Round_.id)));
            }
        }
        return specification;
    }
}
