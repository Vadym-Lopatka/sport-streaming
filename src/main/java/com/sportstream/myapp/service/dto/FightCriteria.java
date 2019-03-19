package com.sportstream.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import com.sportstream.myapp.domain.enumeration.ResultEnum;
import com.sportstream.myapp.domain.enumeration.TypeEnum;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the Fight entity. This class is used in FightResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /fights?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FightCriteria implements Serializable {
    /**
     * Class for filtering ResultEnum
     */
    public static class ResultEnumFilter extends Filter<ResultEnum> {
    }
    /**
     * Class for filtering TypeEnum
     */
    public static class TypeEnumFilter extends Filter<TypeEnum> {
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter redCorner;

    private StringFilter blueCorner;

    private ResultEnumFilter result;

    private TypeEnumFilter resultType;

    private StringFilter comment;

    private LongFilter roundId;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getRedCorner() {
        return redCorner;
    }

    public void setRedCorner(StringFilter redCorner) {
        this.redCorner = redCorner;
    }

    public StringFilter getBlueCorner() {
        return blueCorner;
    }

    public void setBlueCorner(StringFilter blueCorner) {
        this.blueCorner = blueCorner;
    }

    public ResultEnumFilter getResult() {
        return result;
    }

    public void setResult(ResultEnumFilter result) {
        this.result = result;
    }

    public TypeEnumFilter getResultType() {
        return resultType;
    }

    public void setResultType(TypeEnumFilter resultType) {
        this.resultType = resultType;
    }

    public StringFilter getComment() {
        return comment;
    }

    public void setComment(StringFilter comment) {
        this.comment = comment;
    }

    public LongFilter getRoundId() {
        return roundId;
    }

    public void setRoundId(LongFilter roundId) {
        this.roundId = roundId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final FightCriteria that = (FightCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(redCorner, that.redCorner) &&
            Objects.equals(blueCorner, that.blueCorner) &&
            Objects.equals(result, that.result) &&
            Objects.equals(resultType, that.resultType) &&
            Objects.equals(comment, that.comment) &&
            Objects.equals(roundId, that.roundId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        redCorner,
        blueCorner,
        result,
        resultType,
        comment,
        roundId
        );
    }

    @Override
    public String toString() {
        return "FightCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (redCorner != null ? "redCorner=" + redCorner + ", " : "") +
                (blueCorner != null ? "blueCorner=" + blueCorner + ", " : "") +
                (result != null ? "result=" + result + ", " : "") +
                (resultType != null ? "resultType=" + resultType + ", " : "") +
                (comment != null ? "comment=" + comment + ", " : "") +
                (roundId != null ? "roundId=" + roundId + ", " : "") +
            "}";
    }

}
