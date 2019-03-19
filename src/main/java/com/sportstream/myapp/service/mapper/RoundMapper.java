package com.sportstream.myapp.service.mapper;

import com.sportstream.myapp.domain.*;
import com.sportstream.myapp.service.dto.RoundDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Round and its DTO RoundDTO.
 */
@Mapper(componentModel = "spring", uses = {FightMapper.class})
public interface RoundMapper extends EntityMapper<RoundDTO, Round> {

    @Mapping(source = "fight.id", target = "fightId")
    RoundDTO toDto(Round round);

    @Mapping(source = "fightId", target = "fight")
    Round toEntity(RoundDTO roundDTO);

    default Round fromId(Long id) {
        if (id == null) {
            return null;
        }
        Round round = new Round();
        round.setId(id);
        return round;
    }
}
