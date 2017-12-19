package com.nm.common.keycloak.config;

import com.nm.common.keycloak.provider.AuthTokenProvider;
import com.nm.common.keycloak.provider.KeycloakTokenProvider;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

/**
 * Created by kre5335 on 4/25/2017.
 */

public class KeycloakAuthenticatorClientRequestFactory extends HttpComponentsClientHttpRequestFactory implements ClientHttpRequestFactory {

    private AuthTokenProvider tokenProvider;

    public KeycloakAuthenticatorClientRequestFactory(String clientId) {
        this(clientId, null);
    }

    protected KeycloakAuthenticatorClientRequestFactory(String clientId, AuthTokenProvider provider) {
        super(HttpClients.custom().disableCookieManagement().build());
        tokenProvider = (provider == null) ? new KeycloakTokenProvider(clientId) : provider;
    }

    @Override
    protected void postProcessHttpRequest(HttpUriRequest request) {
        request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + tokenProvider.getToken());
    }


}
