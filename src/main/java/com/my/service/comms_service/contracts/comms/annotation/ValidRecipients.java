package com.my.service.comms_service.contracts.comms.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RecipientsValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidRecipients {

    String message() default "Invalid recipients for selected channel";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
