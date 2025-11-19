package com.scm.scm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.scm.scm.entities.User;
import com.scm.scm.forms.UserForm;
import com.scm.scm.helper.Message;
import com.scm.scm.helper.MessageType;
import com.scm.scm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class PageController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String inddex() {
        return "redirect:/home";
    }

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
    public String processRegistration(@Valid @ModelAttribute UserForm userForm, BindingResult rBindingResult,
            HttpSession session) {
        // User user = User.builder()
        // .name(userForm.getName())
        // .email(userForm.getEmail())
        // .about(userForm.getAbout())
        // .password(userForm.getPassword())
        // .phoneNumber(userForm.getPhoneNumber())
        // .profilePic(
        // "https://thumbs.dreamstime.com/b/default-user-profile-icon-social-media-picture-flat-symbol-round-grey-shape-person-set-gender-neutral-sign-simple-circular-387560956.jpg")

        // .build();

        if (rBindingResult.hasErrors()) {
            return "register";
        }

        User user = new User();
        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setPassword(userForm.getPassword());
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setAbout(userForm.getAbout());
        user.setProfilePic(
                "https://thumbs.dreamstime.com/b/default-user-profile-icon-social-media-picture-flat-symbol-round-grey-shape-person-set-gender-neutral-sign-simple-circular-387560956.jpg");

        User saveUser = userService.saveUser(user);

        Message message = Message.builder().content("Registration Success").messageType(MessageType.green).build();

        session.setAttribute("message", message);

        return "redirect:/register";
    }
}
