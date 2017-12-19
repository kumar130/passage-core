package com.nm.common.keycloak.config;

import com.nm.common.keycloak.auth.AuthorizationChecker;
import com.nm.common.keycloak.auth.IAuthenticationFacade;
import com.nm.common.keycloak.auth.KeycloakAuthenticationFacade;
import com.nm.common.keycloak.auth.DefaultSecurityChecker;
import org.keycloak.adapters.AdapterDeploymentContext;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

/**
 * Created by kre5335 on 4/14/2017.
 */
@Configuration
@ConditionalOnClass({KeycloakWebSecurityConfigurerAdapter.class})
@EnableConfigurationProperties(KeycloakAuthTokenProperties.class)
public class KeycloakMethodSecurityAutoConfiguration {

    @Configuration
    @ConditionalOnMissingBean(MethodSecurityExpressionHandler.class)
    @ConditionalOnBean(AdapterDeploymentContext.class)
    @EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
    static class MethodExpressionHandlerConfiguration {
        @Bean
        public MethodSecurityExpressionHandler expressionHandler() {
            return new KeycloakSecurityExpressionHandler();
        }

        @Bean
        @ConditionalOnMissingBean(AuthorizationChecker.class)
        public AuthorizationChecker securityChecker() {
            return new DefaultSecurityChecker();
        }
    }


    @Bean
    @ConditionalOnMissingBean(IAuthenticationFacade.class)
    public IAuthenticationFacade authenticationFacade() {
        return new KeycloakAuthenticationFacade();
    }


}
