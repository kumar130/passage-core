package com.nm.common.keycloak.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by kre5335 on 4/17/2017.
 */
@ConfigurationProperties(prefix = "keycloak.authenticator")
public class KeycloakAuthenticatorProperties {

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    private String clientId = "dummy";

}
