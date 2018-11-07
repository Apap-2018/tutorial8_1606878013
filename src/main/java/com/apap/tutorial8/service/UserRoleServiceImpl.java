package com.apap.tutorial8.service;

import com.apap.tutorial8.model.UserRoleModel;
import com.apap.tutorial8.repository.UserRoleDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserRoleServiceImpl implements UserRoleService{
    @Autowired
    private UserRoleDb userDb;

    @Override
    public UserRoleModel addUser (UserRoleModel user) {
        String pass = encrypt(user.getPassword());
        user.setPassword(pass);
        return userDb.save(user);
    }

    @Override
    public void updateUser (UserRoleModel user, String newPassword) {
        String hashedNewPassword = encrypt(newPassword);
        user.setPassword(hashedNewPassword);
        userDb.save(user);
    }

    @Override
    public String encrypt(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);
        return hashedPassword;
    }

    @Override
    public UserRoleModel getUserCurrentLoggedIn(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = "";

        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        }
        else {
            username = principal.toString();
        }

        return userDb.findByUsername(username);
    }

    @Override
    public boolean confirmPassword (String oldPassword, String newPassword, String confirmedNewPassword) {
        boolean flagOldPassword = false;
        boolean flagNewPassword = false;
        boolean flagContainNumber = false;
        boolean flagContainLetter = false;

        boolean flag = false;

        //Password lama yang di form sama dengan password lama sebenarnya
        if (!encrypt(oldPassword).equals(getUserCurrentLoggedIn().getPassword())){
            flagOldPassword = true ;
        }

        //Password baru yang di form sama dengan password baru yang akan dikonfirmasi di form
        if ((newPassword.equals(confirmedNewPassword)) && confirmedNewPassword.length() >= 8) {
            flagNewPassword = true;
        }

        //Password mengandung angka
        for (char c : newPassword.toCharArray()) {
            if (Character.isDigit(c)) {
                flagContainNumber = true;
            }
        }

        //Password mengandung huruf
        flagContainLetter = newPassword.matches(".*[a-zA-Z]+.*");

        flag = flagNewPassword && flagOldPassword && flagContainNumber && flagContainLetter;
        return flag;
    }

    @Override
    public boolean confirmPassword(String password){
        boolean passwordLength = false;
        boolean passwordContainsDigit = false;
        boolean passwordContainsLetter = false;

        if (password.length() >= 8){
            passwordLength = true;
        }

        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                passwordContainsDigit = true;
            }
        }

        passwordContainsLetter = password.matches(".*[a-zA-Z]+.*");

        return passwordLength && passwordContainsDigit && passwordContainsLetter;
    }
}
