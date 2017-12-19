package com.nm.common.keycloak.auth;

import org.springframework.security.core.Authentication;

/**
 * Created by kre5335 on 4/19/2017.
 */
public interface AuthorizationChecker {
    boolean check(Authentication auth);
}
