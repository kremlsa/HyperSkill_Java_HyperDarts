package dartsgame.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DartsScoreValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface DartsScoreConstraint {
    String message() default "Invalid data";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}