package com.scm.scm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.scm.scm.services.impl.SecurityCustomUserDetailService;

@Configuration
public class SecurityConfig {

    // UserDetails user =
    // User.withDefaultPasswordEncoder().username("admin123").password("admin123").build();

    // @Bean
    // public UserDetailsService userDetailsService() {
    // return new InMemoryUserDetailsManager(user);
    // }
    @Autowired
    SecurityCustomUserDetailService service;

    @Autowired
    OAuthAuthenticationSuccessHandler oAuthAuthenticationSuccessHandler;

    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(service);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.authorizeHttpRequests(authorize -> {
            // authorize.requestMatchers("/home", "/register", "/about").permitAll();
            authorize.requestMatchers("/user/**").authenticated();
            authorize.anyRequest().permitAll();
        });

        httpSecurity.formLogin(loginForm -> {
            loginForm.loginPage("/login")
                    .loginProcessingUrl("/authenticate")
                    // .successForwardUrl("/user/dashboard")
                    .defaultSuccessUrl("/user/profile", true)
                    // .failureForwardUrl("/login?error=true")
                    .usernameParameter("email")
                    .passwordParameter("password")
                    .permitAll();
        });

        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        httpSecurity.oauth2Login(oauth -> {
            oauth.loginPage("/login");
            oauth.successHandler(oAuthAuthenticationSuccessHandler);
        });

        httpSecurity.logout(formLogout -> {
            formLogout.logoutUrl("/do-logout");
            formLogout.logoutSuccessUrl("/login?logout=true");
        });

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
