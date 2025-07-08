package antran.project.Validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class DobValidator implements ConstraintValidator<DobConstrain, LocalDate> {
    private int minAge;

    @Override
    public void initialize(DobConstrain constraintAnnotation) {
        this.minAge = constraintAnnotation.min();
    }

    @Override
    public boolean isValid(LocalDate dob, ConstraintValidatorContext context) {
        if (Objects.isNull(dob)) {
            return true; // If dob is null, we assume it's valid (can be handled by @NotNull if needed)
        }

        long years = ChronoUnit.YEARS.between(dob, LocalDate.now());

        return years >= minAge; // Check if the age is greater than or equal to the minimum age
    }
}
