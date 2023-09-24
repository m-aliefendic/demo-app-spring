package com.ba.demo.configuration;

import com.ba.demo.core.service.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter;


import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfig.class);

    @Autowired
    private TokenService tokenService;

    @Autowired
    ObjectMapper objectMapper;


    @Override
    public void configure(final WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                //Registration
                "/public/**/*",
                "/public/*",


                "/swagger-resources",
                "/swagger-resources/*",
                "/swagger-resources/**/*",
                "/swagger-ui.html",
                "/swagger-ui.html/**/*",
                "/swagger-ui.html/*",
                "/api/v2/api-docs",
                "/api/swagger-resources/**")


                .antMatchers(HttpMethod.POST,  "/**/*", "/*")
                .antMatchers(HttpMethod.GET,  "/**/*", "/*");
    }
    @Bean("authenticationManager")
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    @Bean
    AuthenticationEntryPoint unauthorizedEntryPoint() {
        return (request, response, authException) -> {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
        };
    }

//    @Override
//    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
//        auth.authenticationProvider(tokenAuthenticationProvider());
//    }

//    @Bean
//    AuthenticationProvider tokenAuthenticationProvider() {
//        return new TokenAuthenticationProvider(tokenService);
//    }

    @Bean
    public SecurityContextHolderAwareRequestFilter securityContextHolderAwareRequestFilter() {
        return new SecurityContextHolderAwareRequestFilter();
    }

}