package com.nm.test;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.lang.annotation.Annotation;
import java.util.Set;

@RunWith(MockitoJUnitRunner.class)
public class RestApiTest {

    private static String ASCIIDOC_FILE = "file:src/main/asciidoc/index.adoc";
    private static String MARKER_FILE = "file:src/main/resources/com.nm.test.marker";

    @Test
    public void findRestApiImplementation() throws ClassNotFoundException, FileNotFoundException {

        Assume.assumeFalse(ResourceUtils.getFile(MARKER_FILE).getAbsoluteFile().exists());

        int count = countRestApiImplementations();
        Assert.assertTrue("Missing REST API documentation. You must implement "
                + RestApi.class.getName()
                + " and create tests for each endpoint."
                + RestApi.DOC_REF_MESSAGE, count == 1);
    }

    @Test
    public void asciidocCheck() throws ClassNotFoundException, FileNotFoundException {
        Assume.assumeTrue(countRestApiImplementations() == 1);

        Assert.assertTrue(
                "Could not find asciidoc files. Please create 'src/main/asciidoc/index.adoc'.",
                ResourceUtils.getFile(ASCIIDOC_FILE).getAbsoluteFile().exists());
    }

    private int countRestApiImplementations() throws ClassNotFoundException {
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AssignableTypeFilter(RestApi.class));
        Set<BeanDefinition> components = provider.findCandidateComponents("com.nm");
        int count = 0;
        for (BeanDefinition component : components)
        {
            if(!component.getBeanClassName().equals(RestApi.class.getName())){
                Class clazz = Class.forName(component.getBeanClassName());
                Annotation ann = clazz.getAnnotation(RunWith.class);
                count += (ann == null) ? 0 : 1;
            }
        }
        return count;
    }


}