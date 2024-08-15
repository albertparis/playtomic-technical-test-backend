package com.playtomic.tests.wallet.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreditCardValidator implements ConstraintValidator<ValidCreditCard, String> {
    private static final Logger log = LoggerFactory.getLogger(CreditCardValidator.class);
    private static final String CREDIT_CARD_REGEX = "^(?:4[0-9]{12}(?:[0-9]{3})?" + // Visa
            "|5[1-5][0-9]{14}" + // MasterCard
            "|3[47][0-9]{13}" + // American Express
            "|3(?:0[0-5]|[68][0-9])[0-9]{11}" + // Diners Club
            "|6(?:011|5[0-9]{2})[0-9]{12}" + // Discover
            "|(?:2131|1800|35\\d{3})\\d{11})$"; // JCB

    @Override
    public void initialize(ValidCreditCard constraintAnnotation) {
    }

    @Override
    public boolean isValid(String creditCard, ConstraintValidatorContext context) {
//        return creditCard != null && creditCard.matches(CREDIT_CARD_REGEX);
        boolean isValid = creditCard != null && creditCard.matches(CREDIT_CARD_REGEX);
        log.info("Validating credit card: {} - Valid: {}", creditCard, isValid);
        return isValid;
    }
}