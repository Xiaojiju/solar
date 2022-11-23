package com.dire.core.utils.validator;

import com.dire.core.annotations.FieldPattern;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexValidator extends AbstractValidator<FieldPattern> {

    private String regex;
    private Class<? extends Validator>[] validators;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (super.isValid(s, constraintValidatorContext)) {
            return true;
        }
        boolean flag = true;
        if (StringUtils.hasText(regex)) {
            flag = regexMatch(s);
        }
        return flag && validate(s);
    }

    @Override
    public void initialize(FieldPattern constraintAnnotation) {
        regex = constraintAnnotation.regex();
        super.setRequired(constraintAnnotation.required());
        validators = constraintAnnotation.importClass();
    }

    private boolean regexMatch(String source) {
        if (StringUtils.hasText(regex)) {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(source);
            return matcher.matches();
        }
        return false;
    }

    private boolean validate(String source) {
        if (validators.length <= 0) {
            return true;
        }
        try {
            for (Class<? extends Validator> clazz : validators) {
                Constructor<? extends Validator> constructor = clazz.getDeclaredConstructor();
                constructor.setAccessible(true);
                Validator validator = constructor.newInstance();
                if (!validator.validate(source)) {
                    return false;
                }
            }
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

}
