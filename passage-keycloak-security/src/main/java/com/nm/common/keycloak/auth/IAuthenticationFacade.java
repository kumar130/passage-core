package com.nm.common.keycloak.auth;

import com.nm.common.keycloak.model.KeycloakUser;
import org.springframework.security.core.Authentication;

/**
 * Created by kre5335 on 4/17/2017.
 */
public interface IAuthenticationFacade {
    Authentication getAuthentication();
    KeycloakUser getUser();
}
