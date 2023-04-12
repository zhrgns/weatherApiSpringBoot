package com.weatherapp.app01.controller.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {CityParameterValidator.class})
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface CityNameConstraint {
    // generated from @NotBlank
    String message() default "Invalid city name ! ";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}

