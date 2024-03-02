package com.example.serverapi.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<PasswordValidatorPattern, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value.length() > 50) return false;

        if (!value.matches(".*[^\\da-zA-Z@]+.*")) return false;

        int countLetters = 0, countCharacter = 0, countDigits = 0;
        for (char c : value.toCharArray()) {
            if (c == '@') countCharacter++;
            else if (Character.isDigit(c)) countDigits++;
            else if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')) countLetters++;
        }
        return countCharacter == 1 || countLetters >= 4 || countDigits >= 1;
    }
}
