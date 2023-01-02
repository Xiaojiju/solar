package com.dire.guard.filter;

import java.io.Serializable;

public interface AuthenticationResponse extends Serializable {

    String getUniqueId();

    String getToken();

    long expiredTime();

    long signTime();

}
