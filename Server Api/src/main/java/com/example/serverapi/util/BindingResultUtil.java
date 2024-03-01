package com.example.serverapi.util;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.stream.Collectors;

public class BindingResultUtil {
    public static String extractMessages(BindingResult bindingResult) {
        return bindingResult.getFieldErrors()
                .stream().collect(Collectors.toMap(FieldError::getField,
                        DefaultMessageSourceResolvable::getDefaultMessage))
                .toString();
    }
}
