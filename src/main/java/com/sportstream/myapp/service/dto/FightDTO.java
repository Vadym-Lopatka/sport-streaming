package com.sportstream.myapp.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import com.sportstream.myapp.domain.enumeration.ResultEnum;
import com.sportstream.myapp.domain.enumeration.TypeEnum;

/**
 * A DTO for the Fight entity.
 */
public class FightDTO implements Serializable {

    private Long id;

    @NotNull
    private String redCorner;

    @NotNull
    private String blueCorner;

    @NotNull
    private ResultEnum result;

    @NotNull
    private TypeEnum resultType;

    private String comment;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRedCorner() {
        return redCorner;
    }

    public void setRedCorner(String redCorner) {
        this.redCorner = redCorner;
    }

    public String getBlueCorner() {
        return blueCorner;
    }

    public void setBlueCorner(String blueCorner) {
        this.blueCorner = blueCorner;
    }

    public ResultEnum getResult() {
        return result;
    }

    public void setResult(ResultEnum result) {
        this.result = result;
    }

    public TypeEnum getResultType() {
        return resultType;
    }

    public void setResultType(TypeEnum resultType) {
        this.resultType = resultType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FightDTO fightDTO = (FightDTO) o;
        if (fightDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), fightDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FightDTO{" +
            "id=" + getId() +
            ", redCorner='" + getRedCorner() + "'" +
            ", blueCorner='" + getBlueCorner() + "'" +
            ", result='" + getResult() + "'" +
            ", resultType='" + getResultType() + "'" +
            ", comment='" + getComment() + "'" +
            "}";
    }
}
