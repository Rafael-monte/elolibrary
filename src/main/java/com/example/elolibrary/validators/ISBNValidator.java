package com.example.elolibrary.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class ISBNValidator implements ConstraintValidator<ValidISBN, String> {

    @Override
    /*REFERENCIA: https://howtodoinjava.com/java/regex/java-regex-validate-international-standard-book-number-isbns/*/
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        final String LIVRO_PATTERN = "^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$";
        Pattern pattern = Pattern.compile(LIVRO_PATTERN);
        return pattern.matcher(s).matches();
    }
}
