package com.dire.core.convert;

import com.dire.core.CommonConstants;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class LocalDateConverter implements Converter<String, LocalDate> {

    @Override
    public LocalDate convert(String source) {
        if (!StringUtils.hasText(source)) {
            return null;
        }
        return LocalDate.parse(source, DateTimeFormatter.ofPattern(CommonConstants.Y_M_D));
    }

}
