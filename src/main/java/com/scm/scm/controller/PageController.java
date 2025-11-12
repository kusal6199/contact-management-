package com.scm.scm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {

    @RequestMapping("/home")
    public String home(Model model) {
        model.addAttribute("name", "Kushal");
        model.addAttribute("email", "kushal@gmail.com");
        model.addAttribute("github", "https://github.com/kusal6199/JournalApp-Backend-SprinngBoot");
        return "home";
    }

    // about page

    @RequestMapping("/about")
    public String aboutPage() {
        return "about";
    }

    // services page

    @RequestMapping("/services")
    public String servicePage() {
        return "services";
    }

    @GetMapping("/contact")
    public String contactPage() {
        return "contact";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String signupPage() {
        return "register";
    }
}
