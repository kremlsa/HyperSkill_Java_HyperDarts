package dartsgame.payloads;

import dartsgame.validator.DartsScoreConstraint;

import javax.validation.constraints.Pattern;

public class CreateDTO {

    private int targetScore;

    public int getTargetScore() {
        return targetScore;
    }

    public CreateDTO setTargetScore(int targetScore) {
        this.targetScore = targetScore;
        return this;
    }
}

