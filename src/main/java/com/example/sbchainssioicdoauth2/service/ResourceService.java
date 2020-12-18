package com.example.sbchainssioicdoauth2.service;

import java.util.Map;

import org.keycloak.KeycloakSecurityContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ResourceService {

    public String getSomething(KeycloakSecurityContext context) {
        final String uri = "http://localhost:8081/resources/something";

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setBearerAuth(context.getTokenString());

        HttpEntity<?> entity = new HttpEntity<Object>(requestHeaders);
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class).getBody();
        return result;
    }

    public Map<String, Object> getClaims(KeycloakSecurityContext context) {
        final String uri = "http://localhost:8081/resources/claims";

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setBearerAuth(context.getTokenString());

        HttpEntity<?> entity = new HttpEntity<Object>(requestHeaders);
        RestTemplate restTemplate = new RestTemplate(); 

        ParameterizedTypeReference<Map<String, Object>> responseType = new ParameterizedTypeReference<Map<String, Object>>(){};

        ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, entity, responseType);
        
        return responseEntity.getBody();
    }

    public Map<String, Object> getPersonalInfo(KeycloakSecurityContext context) {
        final String uri = "http://localhost:8081/resources/personalInfo";

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setBearerAuth(context.getTokenString());

        HttpEntity<?> entity = new HttpEntity<Object>(requestHeaders);
        RestTemplate restTemplate = new RestTemplate(); 

        ParameterizedTypeReference<Map<String, Object>> responseType = new ParameterizedTypeReference<Map<String, Object>>(){};

        ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, entity, responseType);
        
        return responseEntity.getBody();
    }

    
}