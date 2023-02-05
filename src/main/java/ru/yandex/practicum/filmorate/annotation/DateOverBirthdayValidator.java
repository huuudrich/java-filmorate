package ru.yandex.practicum.filmorate.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;


public class DateOverBirthdayValidator implements ConstraintValidator<DateOverBirthday, LocalDate> {

    @Override
    public void initialize(DateOverBirthday constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        return !LocalDate.of(1895, 12, 28).isAfter(localDate);
    }
}
