package com.nm.common.keycloak.config;

import org.keycloak.AuthorizationContext;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Created by kre5335 on 4/14/2017.
 */
public class KeycloakSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

    private AuthorizationContext authorizationContext;

    public KeycloakSecurityExpressionRoot(Authentication auth) {
        super(auth);
        setDefaultRolePrefix(null);
        KeycloakPrincipal principal = (KeycloakPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        KeycloakSecurityContext context = principal.getKeycloakSecurityContext();
        authorizationContext = new AuthorizationContext(context.getToken(), null);
    }

    private Object target;

    void setThis(Object target) {
        this.target = target;
    }

    @Override
    public void setFilterObject(Object filterObject) {

    }

    @Override
    public Object getFilterObject() {
        return null;
    }

    @Override
    public void setReturnObject(Object returnObject) {

    }

    @Override
    public Object getReturnObject() {
        return null;
    }

    public Object getThis() {
        return target;
    }

    public boolean hasPermission(String resourceName, String scopeName) {
        return authorizationContext.hasPermission(resourceName, scopeName);
    }

    public boolean hasResourcePermission(String resourceName) {
        return authorizationContext.hasResourcePermission(resourceName);
    }

    public boolean hasScopePermission(String permission) {
        return authorizationContext.hasScopePermission(permission);
    }
}