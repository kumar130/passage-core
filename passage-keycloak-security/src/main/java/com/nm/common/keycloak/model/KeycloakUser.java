package com.nm.common.keycloak.model;

import org.keycloak.KeycloakPrincipal;

import java.security.Principal;
import java.util.List;

/**
 * Created by kre5335 on 4/17/2017.
 */
public class KeycloakUser {

    private Principal principal;
    private List<String> groups;
    private List<String> roles;
    private String fieldId;
    private List<String> workForGroups;

 

    public KeycloakUser(Principal principal, List<String> groups, List<String> roles, String fieldId,
            List<String> workForGroups) {
        super();
        this.principal = principal;
        this.groups = groups;
        this.roles = roles;
        this.fieldId = fieldId;
        this.workForGroups = workForGroups;
    }

    public String getFieldId() {
        return fieldId;
    }

    public List<String> getWorkForGroups() {
        return workForGroups;
    }

    public Principal getPrincipal(){
        return principal;
    }

    public List<String> getGroups() {
        return groups;
    }

    public List<String> getRoles() {
        return roles;
    }
}
