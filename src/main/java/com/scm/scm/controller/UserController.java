package com.scm.scm.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    // private Logger logger = LoggerFactory.getLogger(UserController.class);

    // @Autowired
    // private UserService userService;

    // @ModelAttribute
    // public void addLoggedInUserInformation(Model model, Authentication
    // authentication) {
    // String username = Helper.getEmailOfLoggedInUser(authentication);
    // User user = userService.getUserByEmail(username);
    // model.addAttribute("loggedInUser", user);
    // }

    // to access the dashboard of user

    @GetMapping("/dashboard")
    public String getUserDashBoard() {
        return "user/dashboard";
    }

    // to access the profile page of user

    @GetMapping("/profile")
    public String getUserProfilePage(Model model, Authentication authentication) {
        // String username = Helper.getEmailOfLoggedInUser(authentication);

        // User user = userService.getUserByEmail(username);

        // model.addAttribute("loggedInUser", user);

        // logger.info("name ------------>" + user.getName());
        // logger.info("email ------------>" + user.getEmail());
        // logger.info("username: ------------>" + user.getUsername());

        // String username = Helper.getEmailOfLoggedInUser(authentication);

        return "user/profile";
    }
}
