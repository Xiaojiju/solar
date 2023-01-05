package com.dire.core;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

public class SolarLocaleResolver implements LocaleResolver {

    private static final String DEFAULT_HEADER_LOCALE = "Locale-Type";
    private String localeHeader;

    public SolarLocaleResolver() {
        this.localeHeader = DEFAULT_HEADER_LOCALE;
    }

    public SolarLocaleResolver(String localeHeader) {
        this.localeHeader = localeHeader;
    }

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        String localeType = request.getHeader(this.localeHeader);
        Locale locale;
        if (!StringUtils.hasText(localeType)) {
            locale = request.getLocale();
        } else {
            locale = new Locale(localeType);
        }
        if (locale == null) {
            locale = Locale.getDefault();
        }
        return locale;
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {

    }

    public static String getDefaultHeaderLocale() {
        return DEFAULT_HEADER_LOCALE;
    }

    public String getLocaleHeader() {
        return localeHeader;
    }

    public void setLocaleHeader(String localeHeader) {
        this.localeHeader = localeHeader;
    }
}
