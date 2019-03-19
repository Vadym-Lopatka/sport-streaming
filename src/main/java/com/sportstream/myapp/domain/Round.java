package com.sportstream.myapp.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Round.
 */
@Entity
@Table(name = "round")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Round implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "red_total_score")
    private Double redTotalScore;

    @Column(name = "blue_total_score")
    private Double blueTotalScore;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("rounds")
    private Fight fight;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getRedTotalScore() {
        return redTotalScore;
    }

    public Round redTotalScore(Double redTotalScore) {
        this.redTotalScore = redTotalScore;
        return this;
    }

    public void setRedTotalScore(Double redTotalScore) {
        this.redTotalScore = redTotalScore;
    }

    public Double getBlueTotalScore() {
        return blueTotalScore;
    }

    public Round blueTotalScore(Double blueTotalScore) {
        this.blueTotalScore = blueTotalScore;
        return this;
    }

    public void setBlueTotalScore(Double blueTotalScore) {
        this.blueTotalScore = blueTotalScore;
    }

    public Fight getFight() {
        return fight;
    }

    public Round fight(Fight fight) {
        this.fight = fight;
        return this;
    }

    public void setFight(Fight fight) {
        this.fight = fight;
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
        Round round = (Round) o;
        if (round.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), round.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Round{" +
            "id=" + getId() +
            ", redTotalScore=" + getRedTotalScore() +
            ", blueTotalScore=" + getBlueTotalScore() +
            "}";
    }
}
