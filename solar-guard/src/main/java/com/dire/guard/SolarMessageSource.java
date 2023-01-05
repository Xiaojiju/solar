package com.dire.guard;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ResourceBundleMessageSource;

public class SolarMessageSource extends ResourceBundleMessageSource {

    public SolarMessageSource() {
        setBasename("i18n/solar_messages");
        setDefaultEncoding("UTF-8");
    }

    public static MessageSourceAccessor getAccessor() {
        return new MessageSourceAccessor(new SolarMessageSource());
    }
}
