package com.sportstream.myapp.service.mapper;

import com.sportstream.myapp.domain.*;
import com.sportstream.myapp.service.dto.ScoreDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Score and its DTO ScoreDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ScoreMapper extends EntityMapper<ScoreDTO, Score> {



    default Score fromId(Long id) {
        if (id == null) {
            return null;
        }
        Score score = new Score();
        score.setId(id);
        return score;
    }
}
