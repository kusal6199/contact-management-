package com.scm.scm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.scm.scm.entities.User;
import com.scm.scm.forms.UserForm;
import com.scm.scm.services.UserService;

@Controller
public class PageController {

    @Autowired
    private UserService userService;

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
    public String signupPage(Model model) {
        UserForm userForm = new UserForm();
        model.addAttribute("userForm", userForm);

        return "register";
    }

    // signup data processing
    @RequestMapping(value = "/do-register", method = RequestMethod.POST)
    public String processRegistration(@ModelAttribute UserForm userForm) {
        User user = User.builder()
                .name(userForm.getName())
                .email(userForm.getEmail())
                .about(userForm.getAbout())
                .password(userForm.getPassword())
                .phoneNumber(userForm.getPhoneNumber())
                .profilePic(
                        "https://thumbs.dreamstime.com/b/default-user-profile-icon-social-media-picture-flat-symbol-round-grey-shape-person-set-gender-neutral-sign-simple-circular-387560956.jpg")

                .build();
        User saveUser = userService.saveUser(user);
        return "redirect:/register";
    }
}
