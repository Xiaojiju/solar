package com.dire.guard;

import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

public interface Permission {

    Set<GrantedAuthority> getAuthorities();
}
