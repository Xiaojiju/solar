package com.dire.guard.authentication;

import com.dire.guard.UserTemplate;
import com.dire.guard.context.SolarMessageSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.util.Assert;


public class UserTemplatePreAuthenticationChecks implements UserDetailsChecker, MessageSourceAware, InitializingBean {

    protected final Log logger = LogFactory.getLog(getClass());
    private MessageSourceAccessor messages;

    public UserTemplatePreAuthenticationChecks() {
        this.messages = SolarMessageSource.getAccessor();
    }

    public UserTemplatePreAuthenticationChecks(MessageSource messages) {
        setMessageSource(messages);
    }

    @Override
    public void check(UserDetails user) {
        Assert.isInstanceOf(UserTemplate.class, user, "only supposed UserTemplate.class");
        UserTemplate userTemplate = (UserTemplate) user;
        AccountCheck(userTemplate);
        currentMethodChecks(userTemplate);
    }

    private void AccountCheck(UserTemplate user) {
        if (!user.isAccountNonLocked()) {
            UserTemplatePreAuthenticationChecks.this.logger
                    .debug("Failed to authenticate since user account is locked");
            throw new LockedException(UserTemplatePreAuthenticationChecks.this.messages
                    .getMessage("UserTemplatePreAuthenticationChecks.locked", "User account is locked"));
        }
        if (!user.isEnabled()) {
            UserTemplatePreAuthenticationChecks.this.logger
                    .debug("Failed to authenticate since user account is disabled");
            throw new DisabledException(UserTemplatePreAuthenticationChecks.this.messages
                    .getMessage("UserTemplatePreAuthenticationChecks.disabled", "User is disabled"));
        }
        if (!user.isAccountNonExpired()) {
            UserTemplatePreAuthenticationChecks.this.logger
                    .debug("Failed to authenticate since user account has expired");
            throw new AccountExpiredException(UserTemplatePreAuthenticationChecks.this.messages
                    .getMessage("UserTemplatePreAuthenticationChecks.expired", "User account has expired"));
        }
    }

    private void currentMethodChecks(UserTemplate user) {
        if (!user.isCurrentMethodNonLocked()) {
            UserTemplatePreAuthenticationChecks.this.logger.debug("Failed to authenticate since user current method locked");
            throw new AccountExpiredException(UserTemplatePreAuthenticationChecks.this.messages
                    .getMessage("UserTemplatePreAuthenticationChecks.currentMethod.locked", "User current method has locked"));
        }
        if (!user.isCurrentMethodNonExpired()) {
            UserTemplatePreAuthenticationChecks.this.logger.debug("Failed to authenticate since user current method expired");
            throw new AccountExpiredException(UserTemplatePreAuthenticationChecks.this.messages
                    .getMessage("UserTemplatePreAuthenticationChecks.currentMethod.expired", "User current method has expired"));
        }
    }

    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.messages = new MessageSourceAccessor(messageSource);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.messages, "MessageSource could not be null");
    }
}
