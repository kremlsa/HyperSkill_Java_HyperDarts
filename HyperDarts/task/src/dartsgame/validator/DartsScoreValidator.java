package dartsgame.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DartsScoreValidator implements
        ConstraintValidator<DartsScoreConstraint, Integer> {

    @Override
    public void initialize(DartsScoreConstraint data) {
    }

    @Override
    public boolean isValid(Integer field,
                           ConstraintValidatorContext cxt) {
        return field == 101 || field == 301 || field == 501;
    }

}