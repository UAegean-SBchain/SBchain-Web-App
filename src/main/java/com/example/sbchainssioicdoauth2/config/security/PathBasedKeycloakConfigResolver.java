package com.example.sbchainssioicdoauth2.config.security;

import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.KeycloakDeploymentBuilder;
import org.keycloak.adapters.OIDCHttpFacade;

@Slf4j
public class PathBasedKeycloakConfigResolver implements KeycloakConfigResolver {

    private Map<String, KeycloakDeployment> cache = new ConcurrentHashMap<String, KeycloakDeployment>();

    public final static String BASE_URL = "http://localhost:8080/";

    @Override
    public KeycloakDeployment resolve(OIDCHttpFacade.Request request) {

        if (request.getRelativePath().equals("/") || request.getRelativePath().equals("/error")) {
            return KeycloakDeploymentBuilder.build(getClass().getResourceAsStream("/personalinfo-keycloak.json"));
        }
        String path = request.getURI();
        if (request.getHeader("referer") != null && !(path.contains("sso") || path.contains("multi"))) {
            path = request.getHeader("referer");
            if (request.getHeader("referer").equals(BASE_URL) || request.getHeader("referer").endsWith("error")) {
                return KeycloakDeploymentBuilder.build(getClass().getResourceAsStream("/personalinfo-keycloak.json"));
            }
        }

        int multitenantIndex = path.indexOf("multi/");

        String realm = "";
        if (multitenantIndex != -1) {
            realm = path.substring(path.indexOf("multi/")).split("/")[1];
        } else {
            realm = (request.getRelativePath().split("/"))[1];
        }

        if (realm.contains("?")) {
            realm = realm.split("\\?")[0];
        }

        KeycloakDeployment deployment = cache.get(realm);

        if (null == deployment) {
            // not found on the simple cache, try to load it from the file system
            InputStream is = getClass().getResourceAsStream("/" + realm + "-keycloak.json");
            if (is == null) {
                throw new IllegalStateException("Not able to find the file /" + realm + "-keycloak.json");
            }
            deployment = KeycloakDeploymentBuilder.build(is);
            cache.put(realm, deployment);

        }
        cache.put("sso", deployment);
        return deployment;
    }

}
