package com.sportstream.myapp.service;

import com.sportstream.myapp.domain.Fight;
import com.sportstream.myapp.repository.FightRepository;
import com.sportstream.myapp.service.dto.FightDTO;
import com.sportstream.myapp.service.mapper.FightMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing Fight.
 */
@Service
@Transactional
public class FightService {

    private final Logger log = LoggerFactory.getLogger(FightService.class);

    private final FightRepository fightRepository;

    private final FightMapper fightMapper;

    public FightService(FightRepository fightRepository, FightMapper fightMapper) {
        this.fightRepository = fightRepository;
        this.fightMapper = fightMapper;
    }

    /**
     * Save a fight.
     *
     * @param fightDTO the entity to save
     * @return the persisted entity
     */
    public FightDTO save(FightDTO fightDTO) {
        log.debug("Request to save Fight : {}", fightDTO);
        Fight fight = fightMapper.toEntity(fightDTO);
        fight = fightRepository.save(fight);
        return fightMapper.toDto(fight);
    }

    /**
     * Get all the fights.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<FightDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Fights");
        return fightRepository.findAll(pageable)
            .map(fightMapper::toDto);
    }


    /**
     * Get one fight by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<FightDTO> findOne(Long id) {
        log.debug("Request to get Fight : {}", id);
        return fightRepository.findById(id)
            .map(fightMapper::toDto);
    }

    /**
     * Delete the fight by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Fight : {}", id);
        fightRepository.deleteById(id);
    }
}
