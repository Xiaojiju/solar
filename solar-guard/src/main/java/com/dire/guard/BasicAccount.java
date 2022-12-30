package com.dire.guard;

import java.io.Serializable;

public interface BasicAccount extends Serializable {

    boolean isMethodExpired();

    boolean isCredentialsExpired();

    boolean isEnabled();

    boolean isCurrentMethodLocked();

}
