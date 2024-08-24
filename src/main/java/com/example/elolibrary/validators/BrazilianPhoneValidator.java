package com.example.elolibrary.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class BrazilianPhoneValidator implements ConstraintValidator<ValidBrazilianPhone, String> {

    @Override
    //REFERENCIA: https://medium.com/@igorrozani/criando-uma-express%C3%A3o-regular-para-telefone-fef7a8f98828
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        final String TELEFONE_PATTERN = "(\\(?\\d{2}\\)?\\s)?(\\d{4,5}\\-\\d{4})";
        Pattern patternPhone = Pattern.compile(TELEFONE_PATTERN);
        return patternPhone.matcher(s).matches();
    }
}
