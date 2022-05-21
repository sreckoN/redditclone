package com.srecko.reddit.exception;

import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.List;

public class RegistrationRequestException extends RuntimeException {

    public RegistrationRequestException(List<ObjectError> errors) {
        super(createMessage(errors));
    }

    private static String createMessage(List<ObjectError> errors) {
        String message = "Registration request validation failed for the following fields: ";
        for (int i = 0; i < errors.size(); i++) {
            String fieldError = ((FieldError) errors.get(i)).getField();
            if (i == errors.size() - 1) {
                message += fieldError + ".";
            } else {
                message += fieldError + ", ";
            }
        }
        return message;
    }
}
