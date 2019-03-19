package com.sportstream.myapp.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Score entity.
 */
public class ScoreDTO implements Serializable {

    private Long id;

    @NotNull
    private Long fightId;

    @NotNull
    private Long roundId;

    @NotNull
    private Long judgeId;

    @NotNull
    private Double value;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFightId() {
        return fightId;
    }

    public void setFightId(Long fightId) {
        this.fightId = fightId;
    }

    public Long getRoundId() {
        return roundId;
    }

    public void setRoundId(Long roundId) {
        this.roundId = roundId;
    }

    public Long getJudgeId() {
        return judgeId;
    }

    public void setJudgeId(Long judgeId) {
        this.judgeId = judgeId;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
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

        ScoreDTO scoreDTO = (ScoreDTO) o;
        if (scoreDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), scoreDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ScoreDTO{" +
            "id=" + getId() +
            ", fightId=" + getFightId() +
            ", roundId=" + getRoundId() +
            ", judgeId=" + getJudgeId() +
            ", value=" + getValue() +
            "}";
    }
}
