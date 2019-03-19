package com.sportstream.myapp.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.sportstream.myapp.domain.enumeration.ResultEnum;

import com.sportstream.myapp.domain.enumeration.TypeEnum;

/**
 * A Fight.
 */
@Entity
@Table(name = "fight")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Fight extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "red_corner", nullable = false)
    private String redCorner;

    @NotNull
    @Column(name = "blue_corner", nullable = false)
    private String blueCorner;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "result", nullable = false)
    private ResultEnum result;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "result_type", nullable = false)
    private TypeEnum resultType;

    @Column(name = "jhi_comment")
    private String comment;

    @OneToMany(mappedBy = "fight")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Round> rounds = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRedCorner() {
        return redCorner;
    }

    public Fight redCorner(String redCorner) {
        this.redCorner = redCorner;
        return this;
    }

    public void setRedCorner(String redCorner) {
        this.redCorner = redCorner;
    }

    public String getBlueCorner() {
        return blueCorner;
    }

    public Fight blueCorner(String blueCorner) {
        this.blueCorner = blueCorner;
        return this;
    }

    public void setBlueCorner(String blueCorner) {
        this.blueCorner = blueCorner;
    }

    public ResultEnum getResult() {
        return result;
    }

    public Fight result(ResultEnum result) {
        this.result = result;
        return this;
    }

    public void setResult(ResultEnum result) {
        this.result = result;
    }

    public TypeEnum getResultType() {
        return resultType;
    }

    public Fight resultType(TypeEnum resultType) {
        this.resultType = resultType;
        return this;
    }

    public void setResultType(TypeEnum resultType) {
        this.resultType = resultType;
    }

    public String getComment() {
        return comment;
    }

    public Fight comment(String comment) {
        this.comment = comment;
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Set<Round> getRounds() {
        return rounds;
    }

    public Fight rounds(Set<Round> rounds) {
        this.rounds = rounds;
        return this;
    }

    public Fight addRound(Round round) {
        this.rounds.add(round);
        round.setFight(this);
        return this;
    }

    public Fight removeRound(Round round) {
        this.rounds.remove(round);
        round.setFight(null);
        return this;
    }

    public void setRounds(Set<Round> rounds) {
        this.rounds = rounds;
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
        Fight fight = (Fight) o;
        if (fight.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), fight.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Fight{" +
            "id=" + getId() +
            ", redCorner='" + getRedCorner() + "'" +
            ", blueCorner='" + getBlueCorner() + "'" +
            ", result='" + getResult() + "'" +
            ", resultType='" + getResultType() + "'" +
            ", comment='" + getComment() + "'" +
            "}";
    }
}
