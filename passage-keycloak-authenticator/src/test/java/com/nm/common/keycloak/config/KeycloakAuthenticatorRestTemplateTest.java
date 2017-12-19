package com.nm.common.keycloak.config;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * Created by kre5335 on 4/25/2017.
 */
@RunWith(SpringRunner.class)
@DirtiesContext
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class KeycloakAuthenticatorRestTemplateTest {

    private static final String TEST_TOKEN = "xyz";

    @Autowired
    RestTemplate restTemplate;

    @LocalServerPort
    int serverPort;

    @Test
    public void verifyRestTemplate() {
        Assert.assertNotNull(restTemplate);
        restTemplate.getForObject("http://localhost:"+serverPort+"/", Void.class);
    }



    @SpringBootApplication
    @Configuration
    @RestController
    @EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class})
    static class App {

        @Bean
        RestTemplate keycloakRestTemplate() {
            return new RestTemplate(factory());
        }

        @Bean
        @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
        HttpComponentsClientHttpRequestFactory factory() {
            return new KeycloakAuthenticatorClientRequestFactory("test", () -> TEST_TOKEN);
        }


        @GetMapping("/")
        @ResponseStatus(HttpStatus.NO_CONTENT)
        public void testHeader(@RequestHeader  HttpHeaders headers) {
            Assert.assertThat("Bearer "+TEST_TOKEN, CoreMatchers.is(headers.getFirst(HttpHeaders.AUTHORIZATION)));
        }

    }

}