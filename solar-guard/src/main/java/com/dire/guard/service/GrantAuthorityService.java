package com.dire.guard.service;

import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public interface GrantAuthorityService {

    List<GrantedAuthority> loadPermissions(String uniqueId);

    List<GrantedAuthority> loadRoles(String uniqueId);

}
