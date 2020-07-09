package com.example.sbchainssioicdoauth2.config.security;

import javax.servlet.http.HttpServletRequest;

import org.keycloak.adapters.KeycloakConfigResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class KeycloakConfig {

    @Bean
    public KeycloakConfigResolver keycloakConfigResolver(HttpServletRequest request) {
        return new PathBasedKeycloakConfigResolver();
    }   
}