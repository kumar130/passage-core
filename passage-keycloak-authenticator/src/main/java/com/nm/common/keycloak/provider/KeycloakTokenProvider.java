package com.nm.common.keycloak.provider;

import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.Configuration;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.util.JsonSerialization;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * Created by kre5335 on 4/25/2017.
 */
public class KeycloakTokenProvider implements AuthTokenProvider {

    private final String clientId;

    public KeycloakTokenProvider(String clientId) {
        this.clientId = clientId;
    }

    public String getToken() {
        final AuthzClient client = getAuthzClient();
        final AccessTokenResponse response = client.obtainAccessToken();
        return response.getToken();
    }

    private AuthzClient getAuthzClient() {
        try {
            final Resource resource = new ClassPathResource(String.format("%s.json", this.clientId));
            return AuthzClient.create(JsonSerialization.readValue(resource.getInputStream(), Configuration.class, true));
        } catch (Exception e) {
            throw new RuntimeException(String.format("Could not create authorization client for %s", clientId), e);
        }
    }


}
