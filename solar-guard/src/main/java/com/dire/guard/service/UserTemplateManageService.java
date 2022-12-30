package com.dire.guard.service;

import com.dire.guard.AccountDetails;

import java.util.Collection;

public interface UserTemplateManageService extends UserTemplateService {

    boolean createUser(AccountDetails accountDetails);

    boolean updateUser(AccountDetails accountDetails);

    boolean deleteUser(String uniqueId);

    boolean deleteUsers(Collection<String> uniqueIds);

    boolean changePassword(String oldPassword, String newPassword);

    boolean referenceKeyExists(String referenceKey);
}
