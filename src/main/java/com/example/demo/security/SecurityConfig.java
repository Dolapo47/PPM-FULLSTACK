package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()
                .and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler)//handles why exception is thrown on wrong authentication
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)//server doesnt have to hold sessions
                .and()
                .headers().frameOptions().sameOrigin()//to enable h2 database
                .and()
                .authorizeRequests()
                .antMatchers(
                    "/",
                        "/favicon.ico",
                        "/**/*/png",
                        "/**/*/gif",
                        "/**/*/svg",
                        "/**/*/jpg",
                        "/**/*/html",
                        "/**/*/css",
                        "/**/*/js"
                ).permitAll()
                .antMatchers("/api/users/**")
                .permitAll()
                .anyRequest()
                .authenticated();
    }
}
