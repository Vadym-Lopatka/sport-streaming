package com.sportstream.myapp.service;

import com.sportstream.myapp.domain.Round;
import com.sportstream.myapp.repository.RoundRepository;
import com.sportstream.myapp.service.dto.RoundDTO;
import com.sportstream.myapp.service.mapper.RoundMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing Round.
 */
@Service
@Transactional
public class RoundService {

    private final Logger log = LoggerFactory.getLogger(RoundService.class);

    private final RoundRepository roundRepository;

    private final RoundMapper roundMapper;

    public RoundService(RoundRepository roundRepository, RoundMapper roundMapper) {
        this.roundRepository = roundRepository;
        this.roundMapper = roundMapper;
    }

    /**
     * Save a round.
     *
     * @param roundDTO the entity to save
     * @return the persisted entity
     */
    public RoundDTO save(RoundDTO roundDTO) {
        log.debug("Request to save Round : {}", roundDTO);
        Round round = roundMapper.toEntity(roundDTO);
        round = roundRepository.save(round);
        return roundMapper.toDto(round);
    }

    /**
     * Get all the rounds.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<RoundDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Rounds");
        return roundRepository.findAll(pageable)
            .map(roundMapper::toDto);
    }


    /**
     * Get one round by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<RoundDTO> findOne(Long id) {
        log.debug("Request to get Round : {}", id);
        return roundRepository.findById(id)
            .map(roundMapper::toDto);
    }

    /**
     * Delete the round by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Round : {}", id);
        roundRepository.deleteById(id);
    }
}
