package com.sportstream.myapp.repository;

import com.sportstream.myapp.domain.Fight;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Fight entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FightRepository extends JpaRepository<Fight, Long>, JpaSpecificationExecutor<Fight> {

}
