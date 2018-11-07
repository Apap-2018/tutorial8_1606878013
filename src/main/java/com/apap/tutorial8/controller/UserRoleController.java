package com.apap.tutorial8.controller;

import com.apap.tutorial8.model.UserRoleModel;
import com.apap.tutorial8.repository.UserRoleDb;
import com.apap.tutorial8.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserRoleController {
    @Autowired
    private UserRoleService userService;

    @Autowired
    private UserRoleDb userRoleDb;

    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    private String addUserSubmit(@ModelAttribute UserRoleModel user, @RequestParam("password") String password) {
        if (userService.confirmPassword(password)){
            userService.addUser(user);
            return "home";
        }
        else {
            return "password-salah";
        }
    }

    @GetMapping(value = "/updateUser")
    private String updateUser(){
        return "update-user";
    }

    @PostMapping(value = "/updateUser")
    private String updateUserSubmit(@RequestParam("oldPassword") String oldPassword,
                                    @RequestParam("newPassword") String newPassword,
                                    @RequestParam("confirmedNewPassword") String confirmedNewPassword) {
        if (userService.confirmPassword(oldPassword, newPassword, confirmedNewPassword)) {
            UserRoleModel currentLoggedIn = userService.getUserCurrentLoggedIn();
            userService.updateUser(currentLoggedIn, newPassword);
            return "home";
        }
        else {
            return "password-salah";
        }
    }
}
