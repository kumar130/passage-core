package com.nm.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.restdocs.RestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class RestApi {
    public static final String DOC_REF_MESSAGE = "\n\nPlease refer to 'http://docs.spring.io/autorepo/docs/spring-restdocs/1.0.0.RC1/reference/html5/' for more info.\n";

    private static final String TARGET_OUTPUT_DIR = "target/generated-snippets";

    @Rule
    public final RestDocumentation restDocumentation = new RestDocumentation(TARGET_OUTPUT_DIR);

    protected RestDocumentationResultHandler document;
    protected ObjectMapper mapper = new ObjectMapper();

    private Class<?>[] controllers;

    public RestApi(Class ... controllerClass){
        controllers = controllerClass;
    }

    private List<String> findAnnotatedMethods(Class clazz, Class<? extends Annotation> annotation) {
        List<String> list = new ArrayList();
        Method[] methods = ReflectionUtils.getAllDeclaredMethods(clazz);
        for(Method m : methods){
            if(m.getAnnotation(annotation) != null) {
                list.add(m.getName());
            }
        }
        return list;
    }

    @Test
    public void allEndpointsDocumented() {
        List<String> missing = new ArrayList<String>();
        for(Class c : controllers) {
            List<String> cMethods = findAnnotatedMethods(c, RequestMapping.class);
            List<String> tMethods = findAnnotatedMethods(this.getClass(), Test.class);
            for(String cM : cMethods){
                if(!tMethods.contains(cM)){
                    missing.add(cM);
                }
            }
            Assert.assertTrue("Missing REST API documentation for " + c.getName() + " and its handler methods:" + missing + " ."+DOC_REF_MESSAGE, missing.isEmpty());
        }
    }

}
