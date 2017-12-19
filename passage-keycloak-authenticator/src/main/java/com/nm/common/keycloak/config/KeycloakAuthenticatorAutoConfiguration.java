package com.nm.common.keycloak.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.client.RestTemplate;

/**
 * Created by kre5335 on 4/14/2017.
 */
@Configuration
@ConditionalOnBean({RestTemplate.class})
@ConditionalOnMissingBean(KeycloakAuthenticatorClientRequestFactory.class)
@EnableConfigurationProperties(KeycloakAuthenticatorProperties.class)
public class KeycloakAuthenticatorAutoConfiguration {

    @Autowired
    private KeycloakAuthenticatorProperties properties;

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public KeycloakAuthenticatorClientRequestFactory keycloakAuthenticatorClientRequestFactory() {
        return new KeycloakAuthenticatorClientRequestFactory(properties.getClientId());
    }

}
