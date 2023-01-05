package com.dire.guard.web.api;

import com.dire.core.context.response.RestResult;
import com.dire.guard.UserTemplate;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/solar")
public class UserManageController {

    private UserDetailsManager userDetailsManager;

    public UserManageController(UserDetailsManager userDetailsManager) {
        this.userDetailsManager = userDetailsManager;
    }

    @PostMapping("/user")
    public RestResult<UserTemplate> createUser() {
        UserTemplate userTemplate = (UserTemplate) userDetailsManager.loadUserByUsername("17790271060");
        userTemplate.eraseCredentials();
        return RestResult.success(userTemplate);
    }

    public UserDetailsManager getUserDetailsManager() {
        return userDetailsManager;
    }

    public void setUserDetailsManager(UserDetailsManager userDetailsManager) {
        this.userDetailsManager = userDetailsManager;
    }
}
