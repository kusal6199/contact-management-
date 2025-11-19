package com.scm.scm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    // to access the dashboard of user

    @GetMapping("/dashboard")
    public String getUserDashBoard() {
        return "user/dashboard";
    }

    // to access the profile page of user

    @GetMapping("/profile")
    public String getUserProfilePage() {
        return "user/profile";
    }
}
