package com.my.service.comms_service.contracts.comms.annotation;

import com.my.service.comms_service.contracts.comms.base.MessageContext;
import com.my.service.comms_service.contracts.comms.enums.ChannelType;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;
import java.util.regex.Pattern;

public class RecipientsValidator implements ConstraintValidator<ValidRecipients, MessageContext> {

    private static final Pattern EMAIL_REGEX =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    private static final Pattern PHONE_REGEX =
            Pattern.compile("^\\+?[1-9]\\d{7,14}$");

    private static final Pattern UUID_REGEX =
        Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");


    @Override
    public boolean isValid(MessageContext ctx, ConstraintValidatorContext context) {
        if (ctx == null) return true;

        ChannelType channel = ctx.getChannel();
        List<String> recipients = ctx.getRecipients();

        if (recipients == null || recipients.isEmpty())
            return false;

        switch (channel) {
            case ChannelType.email -> {
                return validateAll(recipients, EMAIL_REGEX, context, "must be valid email:"+EMAIL_REGEX);
            }
            case ChannelType.sms -> {
                return validateAll(recipients, PHONE_REGEX, context, "must be valid phone number:"+PHONE_REGEX);
            }
            case ChannelType.notification -> {
                return validateAll(recipients, UUID_REGEX, context, "must be valid uuid:"+UUID_REGEX);
            }
            default -> {
                return true;
            }
        }
    }

    private boolean validateAll(List<String> values, Pattern pattern,
                                ConstraintValidatorContext context, String message) {
        boolean allValid = true;

        for (String value : values) {
            if (value == null || !pattern.matcher(value).matches()) {
                allValid = false;

                context.disableDefaultConstraintViolation();
                context
                    .buildConstraintViolationWithTemplate("Invalid recipient: " + value + " — " + message)
                    .addPropertyNode("recipients")
                    .addConstraintViolation();
            }
        }
        return allValid;
    }
}
