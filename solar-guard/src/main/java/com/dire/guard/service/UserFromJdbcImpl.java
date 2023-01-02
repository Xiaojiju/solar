package com.dire.guard.service;

import com.dire.guard.UserTemplate;
import com.dire.guard.exceptions.NoAuthoritiesException;
import com.dire.guard.mapper.UserServiceMapper;
import com.dire.tools.enums.Judge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class UserFromJdbcImpl implements UserDetailsService, MessageSourceAware {

    private static final Logger logger = LoggerFactory.getLogger(UserFromJdbcImpl.class);
    private GrantAuthorityService grantAuthorityService = new NullGrantAuthorityServiceImpl();
    private UserServiceMapper userServiceMapper;
    private MessageSourceAccessor messageSource = SpringSecurityMessageSource.getAccessor();
    private GrantAuthorityHandler permissionHandler = new EmptyPermissionHandler();
    private final boolean enablePermissions;

    public UserFromJdbcImpl(UserServiceMapper userServiceMapper) {
        this.userServiceMapper = userServiceMapper;
        this.enablePermissions = true;
    }

    public UserFromJdbcImpl(UserServiceMapper userServiceMapper, boolean enablePermissions) {
        this.userServiceMapper = userServiceMapper;
        this.enablePermissions = enablePermissions;
    }

    @Override
    public void setMessageSource(MessageSource messageSource) {
        Assert.notNull(messageSource, "messageSource cannot be null");
        this.messageSource = new MessageSourceAccessor(messageSource);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDto userDto = this.userServiceMapper.selectUserByReferenceKey(username);
        UserTemplate userDetails = this.createUserTemplate(username, userDto);
        userDto.eraseCredentials();
        List<GrantedAuthority> permissions;
        if (this.enablePermissions) {
            permissions = permissionHandler.handle(loadAuthorities(userDetails.getUsername()));
        } else {
            permissions = new ArrayList<>();
        }
        return createUserTemplate(username, userDetails, permissions);
    }

    protected List<GrantedAuthority> loadAuthorities(String username) {
        return grantAuthorityService.loadPermissions(username);
    }

    private UserDetails createUserTemplate(String username, UserDetails userDetails,
                                            List<GrantedAuthority> permissions) {
        Assert.isInstanceOf(UserTemplate.class, userDetails,
                () -> this.messageSource.getMessage("UserFromJdbcImpl.onlySupports",
                        "Only UserTemplate is supported"));
        if (this.enablePermissions) {
            if (CollectionUtils.isEmpty(permissions)) {
                logger.debug("Username {} authorities could not be null", username);
                throw new NoAuthoritiesException(this.messageSource.getMessage("JdbcDaoImpl.nonPermissions",
                        new Object[] { username }, "Username {0} has no permission"));
            }
        }
        UserTemplate userTemplate = (UserTemplate) userDetails;
        return UserTemplate.builder().username(userTemplate.getUsername())
                .password(userTemplate.getPassword())
                .methodExpired(!userTemplate.isCurrentMethodExpired())
                .credentialsExpired(!userTemplate.isCredentialsNonExpired())
                .disabled(!userTemplate.isEnabled())
                .currentMethodLocked(!userTemplate.isCurrentMethodNonLocked())
                .currentMethod(userTemplate.getCurrentMethod())
                .authorities(permissions)
                .build();
    }

    public UserTemplate createUserTemplate(String username, UserDto userDto) {
        if (userDto == null) {
            logger.debug("username {} not found", username);
            throw new UsernameNotFoundException(this.messageSource.getMessage("JdbcDaoImpl.notFound",
                    new Object[] { username }, "Username {0} wrong with username or password"));
        }
        LocalDateTime now = LocalDateTime.now();
        boolean credentialsExpired = false, methodExpired = false;
        LocalDateTime credentialsExpiredTime = userDto.getCredentialsExpired();
        if (credentialsExpiredTime != null) {
            credentialsExpired = credentialsExpiredTime.isAfter(now);
        }
        LocalDateTime methodExpiredTime = userDto.getMethodExpired();
        if (methodExpiredTime != null) {
            methodExpired = methodExpiredTime.isAfter(now);
        }
        return new UserTemplate(userDto.getUniqueId(), userDto.getSecretKey(), Collections.emptySet(),
                !methodExpired, !credentialsExpired, userDto.getEnabled() == Judge.YES,
                !(userDto.getCurrentMethodLocked() == Judge.YES), userDto.getCurrentMethod());
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

    public UserServiceMapper getUserServiceMapper() {
        return userServiceMapper;
    }

    public void setUserServiceMapper(UserServiceMapper userServiceMapper) {
        this.userServiceMapper = userServiceMapper;
    }

    public GrantAuthorityHandler getPermissionHandler() {
        return permissionHandler;
    }

    public void setPermissionHandler(GrantAuthorityHandler permissionHandler) {
        this.permissionHandler = permissionHandler;
    }

    public boolean isEnablePermissions() {
        return enablePermissions;
    }

    public static class UserDto implements CredentialsContainer {

        private String uniqueId;
        private String currentReferenceKey;
        private String secretKey;
        private LocalDateTime methodExpired;
        private LocalDateTime credentialsExpired;
        private Judge enabled;
        private Judge currentMethodLocked;
        private String currentMethod;

        @Override
        public void eraseCredentials() {
            this.secretKey = null;
        }

        public String getUniqueId() {
            return uniqueId;
        }

        public void setUniqueId(String uniqueId) {
            this.uniqueId = uniqueId;
        }

        public String getCurrentReferenceKey() {
            return currentReferenceKey;
        }

        public void setCurrentReferenceKey(String currentReferenceKey) {
            this.currentReferenceKey = currentReferenceKey;
        }

        public String getSecretKey() {
            return secretKey;
        }

        public void setSecretKey(String secretKey) {
            this.secretKey = secretKey;
        }

        public LocalDateTime getMethodExpired() {
            return methodExpired;
        }

        public void setMethodExpired(LocalDateTime methodExpired) {
            this.methodExpired = methodExpired;
        }

        public LocalDateTime getCredentialsExpired() {
            return credentialsExpired;
        }

        public void setCredentialsExpired(LocalDateTime credentialsExpired) {
            this.credentialsExpired = credentialsExpired;
        }

        public Judge getEnabled() {
            return enabled;
        }

        public void setEnabled(Judge enabled) {
            this.enabled = enabled;
        }

        public Judge getCurrentMethodLocked() {
            return currentMethodLocked;
        }

        public void setCurrentMethodLocked(Judge currentMethodLocked) {
            this.currentMethodLocked = currentMethodLocked;
        }

        public String getCurrentMethod() {
            return currentMethod;
        }

        public void setCurrentMethod(String currentMethod) {
            this.currentMethod = currentMethod;
        }
    }
}
