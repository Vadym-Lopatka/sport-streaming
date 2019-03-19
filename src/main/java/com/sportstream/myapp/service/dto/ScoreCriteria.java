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
 * Criteria class for the Score entity. This class is used in ScoreResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /scores?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ScoreCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter fightId;

    private LongFilter roundId;

    private LongFilter judgeId;

    private DoubleFilter value;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getFightId() {
        return fightId;
    }

    public void setFightId(LongFilter fightId) {
        this.fightId = fightId;
    }

    public LongFilter getRoundId() {
        return roundId;
    }

    public void setRoundId(LongFilter roundId) {
        this.roundId = roundId;
    }

    public LongFilter getJudgeId() {
        return judgeId;
    }

    public void setJudgeId(LongFilter judgeId) {
        this.judgeId = judgeId;
    }

    public DoubleFilter getValue() {
        return value;
    }

    public void setValue(DoubleFilter value) {
        this.value = value;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ScoreCriteria that = (ScoreCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(fightId, that.fightId) &&
            Objects.equals(roundId, that.roundId) &&
            Objects.equals(judgeId, that.judgeId) &&
            Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        fightId,
        roundId,
        judgeId,
        value
        );
    }

    @Override
    public String toString() {
        return "ScoreCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (fightId != null ? "fightId=" + fightId + ", " : "") +
                (roundId != null ? "roundId=" + roundId + ", " : "") +
                (judgeId != null ? "judgeId=" + judgeId + ", " : "") +
                (value != null ? "value=" + value + ", " : "") +
            "}";
    }

}
