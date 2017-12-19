package com.nm.common.keycloak.config;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.keycloak.authorization.client.Configuration;
import org.keycloak.util.JsonSerialization;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by kre5335 on 5/9/2017.
 */

@RunWith(MockitoJUnitRunner.class)
public class KeycloakJsonParserTest {

    @Before
    public void setup() {
        System.setProperty("test.me.now", "xyz");
    }



    @Test
    public void whenReadJsonFile_thenReplaceValuesWithProps() throws IOException {
        Configuration c = JsonSerialization.readValue(readFile(), Configuration.class, true);
        MatcherAssert.assertThat("xyz", Matchers.is(c.getAuthServerUrl()));
    }

    private InputStream readFile() throws IOException {
        return new ClassPathResource("example.json").getInputStream();
    }
}
