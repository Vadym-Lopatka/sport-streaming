package com.sportstream.myapp.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Round entity.
 */
public class RoundDTO implements Serializable {

    private Long id;

    private Double redTotalScore;

    private Double blueTotalScore;


    private Long fightId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getRedTotalScore() {
        return redTotalScore;
    }

    public void setRedTotalScore(Double redTotalScore) {
        this.redTotalScore = redTotalScore;
    }

    public Double getBlueTotalScore() {
        return blueTotalScore;
    }

    public void setBlueTotalScore(Double blueTotalScore) {
        this.blueTotalScore = blueTotalScore;
    }

    public Long getFightId() {
        return fightId;
    }

    public void setFightId(Long fightId) {
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

        RoundDTO roundDTO = (RoundDTO) o;
        if (roundDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), roundDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RoundDTO{" +
            "id=" + getId() +
            ", redTotalScore=" + getRedTotalScore() +
            ", blueTotalScore=" + getBlueTotalScore() +
            ", fight=" + getFightId() +
            "}";
    }
}
