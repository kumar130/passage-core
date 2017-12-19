package com.nm.common.keycloak.config;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

/**
 * Created by kre5335 on 4/25/2017.
 */
@RunWith(SpringRunner.class)
@DirtiesContext
@SpringBootTest
public class KeycloakAuthenticatorAutoConfigurationTest {

    @Autowired
    ApplicationContext ctx;


    @Test
    public void testConditions() {
        Assert.assertNotNull(ctx.getBean(RestTemplate.class));
        Assert.assertNotNull(ctx.getBean(KeycloakAuthenticatorClientRequestFactory.class));
    }

    @Test
    public void verifyRestTemplate() {
        RestTemplate restTemplate = ctx.getBean(RestTemplate.class);
        Assert.assertFalse(restTemplate.getRequestFactory() instanceof SimpleClientHttpRequestFactory);
        Assert.assertTrue(restTemplate.getRequestFactory() instanceof KeycloakAuthenticatorClientRequestFactory);
    }



    @SpringBootApplication
    @Configuration
    static class App {

        @Autowired
        private HttpComponentsClientHttpRequestFactory factory;

        @Bean
        RestTemplate keycloakRestTemplate() {
            return new RestTemplate(factory);
        }

    }

}