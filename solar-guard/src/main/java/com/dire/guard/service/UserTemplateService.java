package com.dire.guard.service;

import com.dire.guard.AccountDetails;

public interface UserTemplateService {

    AccountDetails loadUserByReferenceKey(String referenceKey);

}
