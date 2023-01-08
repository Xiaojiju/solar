package com.dire.guard.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "solar.security")
public class WebSecurityProperties {

    private boolean enablePermissions;
    private boolean enableSignature;

    public boolean isEnablePermissions() {
        return enablePermissions;
    }

    public void setEnablePermissions(boolean enablePermissions) {
        this.enablePermissions = enablePermissions;
    }

    public boolean isEnableSignature() {
        return enableSignature;
    }

    public void setEnableSignature(boolean enableSignature) {
        this.enableSignature = enableSignature;
    }
}
