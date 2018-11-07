package com.apap.tutorial8.service;

import com.apap.tutorial8.model.UserRoleModel;

public interface UserRoleService {
    UserRoleModel addUser(UserRoleModel user);
    public String encrypt(String password);
    void updateUser(UserRoleModel user, String newPassword);
    UserRoleModel getUserCurrentLoggedIn();
    boolean confirmPassword (String oldPassword, String newPassword, String confirmedNewPassword);
    boolean confirmPassword (String password);
}
