package com.scm.scm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.scm.scm.entities.User;
import com.scm.scm.helper.Helper;
import com.scm.scm.services.UserService;

@ControllerAdvice
public class RootController {

    @Autowired
    UserService userService;

    @ModelAttribute
    public void addLoggedInUserInformation(Model model, Authentication authentication) {

        if (authentication == null) {
            return;
        }

        String username = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(username);

        // System.out.println("username" + user.getUsername());
        model.addAttribute("loggedInUser", user);
    }

}
