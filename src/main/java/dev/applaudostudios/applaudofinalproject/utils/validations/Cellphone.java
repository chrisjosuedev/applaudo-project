package dev.applaudostudios.applaudofinalproject.utils.validations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ContactNumberValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Cellphone {
    String message() default "Invalid cellphone number";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}