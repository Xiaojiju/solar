package com.dire.guard;

import org.springframework.security.core.userdetails.UserDetails;

public interface MultiAuthenticationUserDetails extends UserDetails {

    String getCurrentMethod();

    boolean isCurrentMethodNonExpired();

    boolean isCurrentMethodNonLocked();

}
