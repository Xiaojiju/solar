package com.dire.guard.service;

import com.dire.guard.mapper.UserServiceMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;

public class UserTemplateDetailsManager extends UserFromJdbcImpl implements UserDetailsManager {

    public UserTemplateDetailsManager(UserServiceMapper userServiceMapper) {
        super(userServiceMapper);
    }

    public UserTemplateDetailsManager(UserServiceMapper userServiceMapper, boolean enablePermissions) {
        super(userServiceMapper, enablePermissions);
    }

    @Override
    public void createUser(UserDetails user) {

    }

    @Override
    public void updateUser(UserDetails user) {

    }

    @Override
    public void deleteUser(String username) {

    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }

    @Override
    public boolean userExists(String username) {
        return false;
    }

}
