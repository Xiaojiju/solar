package com.dire.core.convert;

import com.dire.core.CommonConstants;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class LocalDateTimeConverter implements Converter<String, LocalDateTime> {

    @Override
    public LocalDateTime convert(String source) {
        if (!StringUtils.hasText(source)) {
            return null;
        }
        return LocalDateTime.parse(source, DateTimeFormatter.ofPattern(CommonConstants.Y_M_D_H_M_S));
    }
}
