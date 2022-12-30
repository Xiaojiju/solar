package com.dire.guard;

public interface AccountDetails extends BasicAccount {

    String getUniqueId();

    String getCurrentReferenceKey();

    String getSecretKey();

    String getCurrentMethod();

}
