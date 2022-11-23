package com.dire.core.utils.validator;

import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;

public abstract class AbstractValidator<T extends Annotation> implements ConstraintValidator<T, String> {

    private boolean required = false;

    @Override
    public boolean isValid(String e, ConstraintValidatorContext constraintValidatorContext) {
        if (required && !StringUtils.hasText(e)) {
            return false;
        }
        return !required && !StringUtils.hasText(e);
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }
}
