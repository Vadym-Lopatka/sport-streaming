package com.sportstream.myapp.domain;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Score.
 */
@Entity
@Table(name = "score")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Score implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "fight_id", nullable = false)
    private Long fightId;

    @NotNull
    @Column(name = "round_id", nullable = false)
    private Long roundId;

    @NotNull
    @Column(name = "judge_id", nullable = false)
    private Long judgeId;

    @NotNull
    @Column(name = "jhi_value", nullable = false)
    private Double value;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFightId() {
        return fightId;
    }

    public Score fightId(Long fightId) {
        this.fightId = fightId;
        return this;
    }

    public void setFightId(Long fightId) {
        this.fightId = fightId;
    }

    public Long getRoundId() {
        return roundId;
    }

    public Score roundId(Long roundId) {
        this.roundId = roundId;
        return this;
    }

    public void setRoundId(Long roundId) {
        this.roundId = roundId;
    }

    public Long getJudgeId() {
        return judgeId;
    }

    public Score judgeId(Long judgeId) {
        this.judgeId = judgeId;
        return this;
    }

    public void setJudgeId(Long judgeId) {
        this.judgeId = judgeId;
    }

    public Double getValue() {
        return value;
    }

    public Score value(Double value) {
        this.value = value;
        return this;
    }

    public void setValue(Double value) {
        this.value = value;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Score score = (Score) o;
        if (score.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), score.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Score{" +
            "id=" + getId() +
            ", fightId=" + getFightId() +
            ", roundId=" + getRoundId() +
            ", judgeId=" + getJudgeId() +
            ", value=" + getValue() +
            "}";
    }
}
