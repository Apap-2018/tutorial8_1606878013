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
    public String confirmPassword (String oldPassword, String newPassword, String confirmedNewPassword) {
        String message = "";
        boolean passwordContainsDigit = false;
        boolean passwordContainsLetter = false;

        for (char c : newPassword.toCharArray()) {
            if (Character.isDigit(c)){
                passwordContainsDigit = true;
            }
        }

        if (!(newPassword.equals(confirmedNewPassword))) {
            message = "Password baru dan konfirmasi password baru tidak sama";
        }

        if (!passwordContainsDigit) {
            message = "Password harus terdiri atas minimal satu buah angka";
        }

        passwordContainsLetter = newPassword.matches(".*[a-zA-Z]+.*");

        if (!passwordContainsLetter) {
            message = "Password harus terdiri atas minimal satu buah huruf";
        }

        if (!(newPassword.length() >= 8)){
            message = "Password harus terdiri atas minimal 8 buah karakter";
        }

        UserRoleModel user = getUserCurrentLoggedIn();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!(passwordEncoder.matches(oldPassword, user.getPassword()))){
            message = "Password lama tidak sama";
        }

        return message;
    }

    @Override
    public String confirmPassword(String password){
        String message = "";
        boolean passwordContainsDigit = false;
        boolean passwordContainsLetter = false;

        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                passwordContainsDigit = true;
            }
        }

        if (!passwordContainsDigit){
            message = "Password harus terdiri atas minimal satu buah angka";
        }

        passwordContainsLetter = password.matches(".*[a-zA-Z]+.*");

        if (!passwordContainsLetter) {
            message = "Password harus terdiri atas minimal satu buah huruf";
        }

        if (!(password.length() >= 8)){
            message = "Password harus terdiri atas minimal 8 buah karakter";
        }

        return message;
    }
}
