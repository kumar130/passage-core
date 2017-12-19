package com.nm.common.keycloak.config;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

/**
 * Created by kre5335 on 4/25/2017.
 */
@RunWith(SpringRunner.class)
@DirtiesContext
@SpringBootTest
public class KeycloakAuthenticatorAutoConfigurationConditionsTest {

    @Autowired
    ApplicationContext ctx;


    @Test(expected = NoSuchBeanDefinitionException.class)
    public void testConditions() {
        Assert.assertNull(ctx.getBean(RestTemplate.class));
        Assert.assertNull(ctx.getBean(KeycloakAuthenticatorClientRequestFactory.class));
        Assert.assertNull(ctx.getBean(HttpComponentsClientHttpRequestFactory.class));
    }



    @SpringBootApplication
    static class App {

    }

}