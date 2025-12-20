package com.scm.scm.config;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.scm.scm.entities.Providers;
import com.scm.scm.entities.User;
import com.scm.scm.helper.AppConstant;
import com.scm.scm.repository.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    Logger logger = LoggerFactory.getLogger(OAuthAuthenticationSuccessHandler.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        // logger.info("OAuthAuthenticationSuccessHandeler");

        // before login save data into the DB

        /**
         * DefaultOAuth2User user = (DefaultOAuth2User) authentication.getPrincipal();
         * 
         * String email = user.getAttribute("email").toString();
         * String picture = user.getAttribute("picture").toString();
         * String name = user.getAttribute("name").toString();
         * 
         * // create usaer and save in Database
         * 
         * User user2 = new User();
         * user2.setEmail(email);
         * user2.setName(name);
         * user2.setProfilePic(picture);
         * user2.setProvider(Providers.GOOGLE);
         * user2.setPassword("password");
         * user2.setUserId(UUID.randomUUID().toString());
         * user2.setEnable(true);
         * user2.setEmailVerified(true);
         * user2.setProviderUserId(user.getName());
         * user2.setRoleList(List.of(AppConstant.ROLE_USER));
         * 
         * user2.setAbout("this account is created using google");
         * 
         * User userInDb = userRepository.findByEmail(email).orElse(null);
         * if (userInDb == null) {
         * userRepository.save(user2);
         * logger.info("user is saved: " + email);
         * }
         * 
         */

        // first check if this is google or github login

        var oauthUser = (DefaultOAuth2User) authentication.getPrincipal();

        User user = new User();

        user.setEmailVerified(true);
        user.setRoleList(List.of(AppConstant.ROLE_USER));
        user.setEnable(true);
        user.setUserId(UUID.randomUUID().toString());

        var OAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;

        String authorizedClientRegistrationId = OAuth2AuthenticationToken.getAuthorizedClientRegistrationId();
        if (authorizedClientRegistrationId.equalsIgnoreCase("google")) {

            user.setEmail(oauthUser.getAttribute("email").toString());
            user.setName(oauthUser.getAttribute("name").toString());
            user.setProfilePic(oauthUser.getAttribute("picture").toString());
            user.setProviderUserId(oauthUser.getName());
            user.setProvider(Providers.GOOGLE);

        } else if (authorizedClientRegistrationId.equalsIgnoreCase("github")) {

            String email = oauthUser.getAttribute("email") != null ? oauthUser.getAttribute("email")
                    : oauthUser.getAttribute("login").toString() + "@gmail.com";

            String name = oauthUser.getAttribute("name") != null
                    ? oauthUser.getAttribute("name").toString()
                    : oauthUser.getAttribute("login").toString();

            user.setProfilePic(oauthUser.getAttribute("avatar_url").toString());
            user.setName(name);
            user.setProviderUserId(oauthUser.getName());
            user.setEmail(email);
            user.setProvider(Providers.GITHUB);

        } else {
            logger.info("Unknown Provider");
        }

        User userInDb = userRepository.findByEmail(user.getEmail()).orElse(null);
        if (userInDb == null) {
            userRepository.save(user);
            logger.info("user is saved: " + user.getEmail());

        }
        new DefaultRedirectStrategy().sendRedirect(request, response, "/user/profile");
    }
}
