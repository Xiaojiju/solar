package com.dire.guard.authentication;

public interface PermissionMatcher {

    boolean hasPermissions(String...permission);

}
