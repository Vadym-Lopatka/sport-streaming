package com.sportstream.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the Round entity. This class is used in RoundResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /rounds?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class RoundCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private DoubleFilter redTotalScore;

    private DoubleFilter blueTotalScore;

    private LongFilter fightId;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public DoubleFilter getRedTotalScore() {
        return redTotalScore;
    }

    public void setRedTotalScore(DoubleFilter redTotalScore) {
        this.redTotalScore = redTotalScore;
    }

    public DoubleFilter getBlueTotalScore() {
        return blueTotalScore;
    }

    public void setBlueTotalScore(DoubleFilter blueTotalScore) {
        this.blueTotalScore = blueTotalScore;
    }

    public LongFilter getFightId() {
        return fightId;
    }

    public void setFightId(LongFilter fightId) {
        this.fightId = fightId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final RoundCriteria that = (RoundCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(redTotalScore, that.redTotalScore) &&
            Objects.equals(blueTotalScore, that.blueTotalScore) &&
            Objects.equals(fightId, that.fightId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        redTotalScore,
        blueTotalScore,
        fightId
        );
    }

    @Override
    public String toString() {
        return "RoundCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (redTotalScore != null ? "redTotalScore=" + redTotalScore + ", " : "") +
                (blueTotalScore != null ? "blueTotalScore=" + blueTotalScore + ", " : "") +
                (fightId != null ? "fightId=" + fightId + ", " : "") +
            "}";
    }

}
