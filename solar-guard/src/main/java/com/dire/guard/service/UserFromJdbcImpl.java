package com.dire.guard.service;

import com.dire.guard.AccountDetails;
import com.dire.guard.UserTemplate;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.util.Assert;


public class UserFromJdbcImpl implements UserTemplateService, MessageSourceAware {

    private GrantAuthorityService grantAuthorityService = new NullGrantAuthorityServiceImpl();
    private UserServiceMapper userServiceMapper;
    private MessageSourceAccessor messageSource = SpringSecurityMessageSource.getAccessor();

    public UserFromJdbcImpl(UserServiceMapper userServiceMapper) {
        this.userServiceMapper = userServiceMapper;
    }

    @Override
    public void setMessageSource(MessageSource messageSource) {
        Assert.notNull(messageSource, "messageSource cannot be null");
        this.messageSource = new MessageSourceAccessor(messageSource);
    }

    @Override
    public AccountDetails loadUserByReferenceKey(String referenceKey) {
        return userServiceMapper.selectUserByReferenceKey(referenceKey);
    }

    public MessageSourceAccessor getMessageSource() {
        return this.messageSource;
    }

    public GrantAuthorityService getGrantAuthorityService() {
        return this.grantAuthorityService;
    }

    public void setGrantAuthorityService(GrantAuthorityService grantAuthorityService) {
        this.grantAuthorityService = grantAuthorityService;
    }
}
