package com.dire.guard.authentication;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

import java.util.Set;

public class EachUserGrantAuthority implements GrantedAuthority {

    private final String roleId;
    private final String role;
    private final Set<String> permissions;

    public EachUserGrantAuthority(String roleId, String role, Set<String> permissions) {
        Assert.hasText(roleId, "roleId could not be null");
        Assert.isTrue(permissions != null, "permissions could not be null");
        this.roleId = roleId;
        this.role = role;
        this.permissions = permissions;
    }

    @Override
    public String getAuthority() {
        return this.roleId;
    }

    public String getName() {
        return this.role;
    }

    public Set<String> getPermissions() {
        return this.permissions;
    }

}
