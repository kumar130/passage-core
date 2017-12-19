package com.nm.common.keycloak.auth;

import com.nm.common.keycloak.config.KeycloakAuthTokenProperties;
import com.nm.common.keycloak.model.KeycloakUser;
import org.keycloak.KeycloakPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.List;

/**
 * Created by kre5335 on 4/17/2017.
 */

public class KeycloakAuthenticationFacade implements IAuthenticationFacade {

    @Autowired
    private KeycloakAuthTokenProperties properties;

    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public KeycloakUser getUser() {
        Object principal = getAuthentication().getPrincipal();
        if(principal instanceof KeycloakPrincipal){
            final KeycloakPrincipal keycloakPrincipal = (KeycloakPrincipal) principal;
            final List<String> groups = (List<String>) keycloakPrincipal.getKeycloakSecurityContext().getToken().getOtherClaims().getOrDefault(properties.getGroupName(), Collections.emptyList());
            final List<String> roles = (List<String>) keycloakPrincipal.getKeycloakSecurityContext().getToken().getOtherClaims().getOrDefault(properties.getRoleName(), Collections.emptyList());
            final String fieldId = (String) keycloakPrincipal.getKeycloakSecurityContext().getToken().getOtherClaims().getOrDefault(properties.getFieldId(),null);
            final List<String> workForGroups = (List<String>) keycloakPrincipal.getKeycloakSecurityContext().getToken().getOtherClaims().getOrDefault(properties.getWorkForGroup(),Collections.emptyList());
            return new KeycloakUser(keycloakPrincipal, groups, roles,fieldId, workForGroups );
        } else {
            return new KeycloakUser(new KeycloakPrincipal(principal.toString(), null), Collections.emptyList(), Collections.emptyList(), null,Collections.emptyList());
        }
    }
}
