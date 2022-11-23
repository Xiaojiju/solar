package com.dire.core.annotations;

import com.dire.core.utils.validator.Validator;
import com.dire.core.utils.validator.RegexValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 验证
 * @author 一块小饼干
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = RegexValidator.class)
public @interface FieldPattern {

    boolean required() default true;

    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String regex() default "";

    Class<? extends Validator>[] importClass() default {};

}
