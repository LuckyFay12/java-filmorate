package ru.yandex.practicum.filmorate.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = MinDateValidator.class)
public @interface MinDate {
    String date();
    String message() default "Дата не должна быть раньше {date}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
