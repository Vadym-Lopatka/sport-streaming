package com.sportstream.myapp.service.mapper;

import com.sportstream.myapp.domain.*;
import com.sportstream.myapp.service.dto.FightDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Fight and its DTO FightDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FightMapper extends EntityMapper<FightDTO, Fight> {


    @Mapping(target = "rounds", ignore = true)
    Fight toEntity(FightDTO fightDTO);

    default Fight fromId(Long id) {
        if (id == null) {
            return null;
        }
        Fight fight = new Fight();
        fight.setId(id);
        return fight;
    }
}
