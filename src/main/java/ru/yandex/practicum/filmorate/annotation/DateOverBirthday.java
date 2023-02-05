package ru.yandex.practicum.filmorate.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DateOverBirthdayValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DateOverBirthday {
    String message() default "{DateOverBirthday}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
