package com.scm.scm.helper;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

// import lombok.val;

public class Helper {

    public static String getEmailOfLoggedInUser(Authentication authentication) {

        // AuthenticationPrincipal principal = (AuthenticationPrincipal)
        // authentication.getPrincipal();

        if (authentication instanceof OAuth2AuthenticationToken) {

            var aOuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
            var authorizedClientRegistrationId = aOuth2AuthenticationToken.getAuthorizedClientRegistrationId();

            String username = "";
            var oauth2User = (OAuth2User) authentication.getPrincipal();

            if (authorizedClientRegistrationId.equalsIgnoreCase("google")) {

                username = oauth2User.getAttribute("email");
                System.out.println("getting email from google");

            } else if (authorizedClientRegistrationId.equalsIgnoreCase("github")) {
                System.out.println("getting email from github ");

                username = oauth2User.getAttribute("email") != null ? oauth2User.getAttribute("email")
                        : oauth2User.getAttribute("login").toString() + "@gmail.com";

            }

            return username;
        } else {
            System.out.println("getting email from local database ");
            return authentication.getName();
        }

    }

}
