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

import com.sportstream.myapp.domain.Round;
import com.sportstream.myapp.domain.*; // for static metamodels
import com.sportstream.myapp.repository.RoundRepository;
import com.sportstream.myapp.service.dto.RoundCriteria;
import com.sportstream.myapp.service.dto.RoundDTO;
import com.sportstream.myapp.service.mapper.RoundMapper;

/**
 * Service for executing complex queries for Round entities in the database.
 * The main input is a {@link RoundCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RoundDTO} or a {@link Page} of {@link RoundDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RoundQueryService extends QueryService<Round> {

    private final Logger log = LoggerFactory.getLogger(RoundQueryService.class);

    private final RoundRepository roundRepository;

    private final RoundMapper roundMapper;

    public RoundQueryService(RoundRepository roundRepository, RoundMapper roundMapper) {
        this.roundRepository = roundRepository;
        this.roundMapper = roundMapper;
    }

    /**
     * Return a {@link List} of {@link RoundDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RoundDTO> findByCriteria(RoundCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Round> specification = createSpecification(criteria);
        return roundMapper.toDto(roundRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link RoundDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RoundDTO> findByCriteria(RoundCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Round> specification = createSpecification(criteria);
        return roundRepository.findAll(specification, page)
            .map(roundMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RoundCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Round> specification = createSpecification(criteria);
        return roundRepository.count(specification);
    }

    /**
     * Function to convert RoundCriteria to a {@link Specification}
     */
    private Specification<Round> createSpecification(RoundCriteria criteria) {
        Specification<Round> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Round_.id));
            }
            if (criteria.getRedTotalScore() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRedTotalScore(), Round_.redTotalScore));
            }
            if (criteria.getBlueTotalScore() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBlueTotalScore(), Round_.blueTotalScore));
            }
            if (criteria.getFightId() != null) {
                specification = specification.and(buildSpecification(criteria.getFightId(),
                    root -> root.join(Round_.fight, JoinType.LEFT).get(Fight_.id)));
            }
        }
        return specification;
    }
}
