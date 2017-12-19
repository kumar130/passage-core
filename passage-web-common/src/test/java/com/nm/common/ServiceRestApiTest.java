package com.nm.common;

import com.nm.test.RestApi;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.snippet.Snippet;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import javax.validation.metadata.ConstraintDescriptor;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class ServiceRestApiTest extends RestApi {

    private static final Logger logger = LoggerFactory.getLogger(ServiceRestApiTest.class);
    private static final String REQUEST_MAPPING_PATH = "/test/advice";

    @InjectMocks
    private TestWebService webService;

    @Mock
    private TestService service;

    private MockMvc mockMvc;

    public ServiceRestApiTest() {
        super(TestWebService.class);
    }

    @Before
    public void setUp() {
        this.document = document("{method-name}", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()));
        this.mockMvc = MockMvcBuilders.standaloneSetup(webService)
                .apply(documentationConfiguration(this.restDocumentation)).alwaysDo(this.document)
                .setControllerAdvice(new WebServiceControllerAdvice()).build();
    }

    private Snippet webServiceErrorDocumentation() {
        return responseFields(fieldWithPath("status").description("Response Status Code"),
                fieldWithPath("message").description("Exception message"),
                fieldWithPath("detailedMessage").description("Detailed exception message"),
                fieldWithPath("exception").description("Exception class"));
    }

    @Test
    public void testControllerAdvice() throws Exception {
        String message = "Detailed Message";
        when(service.testControllerAdvice(1)).thenThrow(new RuntimeException(message));

        this.document.snippets(webServiceErrorDocumentation());

        this.mockMvc
                .perform(get(REQUEST_MAPPING_PATH).param("param", "1").accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.status", is(HttpStatus.INTERNAL_SERVER_ERROR.value())))
                .andExpect(jsonPath("$.message", is("Sorry, internal server error has occurred.")))
                .andExpect(jsonPath("$.detailedMessage", is(message)));

        verify(service, times(1)).testControllerAdvice(1);
        verifyNoMoreInteractions(service);
    }

    private void testException(MockHttpServletRequestBuilder builder, ResultMatcher result, HttpStatus status,
            String message, String detailedMessage) throws Exception {
        this.mockMvc.perform(builder.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(result).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.status", is(status.value()))).andExpect(jsonPath("$.message", is(message)))
                .andExpect(jsonPath("$.detailedMessage", is(detailedMessage)));
    }

    @Test
    public void testMissingServletRequestParameterException() throws Exception {
        testException(get(REQUEST_MAPPING_PATH), status().isBadRequest(), HttpStatus.BAD_REQUEST,
                "The requested resource url is missing some parameters.",
                "Required Integer parameter 'param' is not present");
    }

    @Test
    public void testHttpRequestMethodNotSupportedException() throws Exception {
        this.mockMvc
                .perform(put(REQUEST_MAPPING_PATH).accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.status", is(HttpStatus.METHOD_NOT_ALLOWED.value())))
                .andExpect(jsonPath("$.message", is("Oops. Invalid http method.")))
                .andExpect(jsonPath("$.detailedMessage", startsWith("Incorrect http method PUT supplied. Allowed: ")));
    }

    @Test
    public void testConstraintViolationExceptionHandler() throws Exception {
        when(service.testControllerAdvice(1))
                .thenThrow(new ConstraintViolationException(Collections.singleton(new TestConstraintViolation())));

        this.mockMvc
                .perform(get(REQUEST_MAPPING_PATH).param("param", "1").accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())));

        verify(service, times(1)).testControllerAdvice(1);
        verifyNoMoreInteractions(service);
    }

    @Test
    public void testForbiddenExceptionHandler() throws Exception {
        when(service.testControllerAdvice(1)).thenThrow(new ForbiddenException(), new AccessDeniedException("Access Denied"));
        this.mockMvc
                .perform(get(REQUEST_MAPPING_PATH).param("param", "1").accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.status", is(HttpStatus.FORBIDDEN.value())));

        verify(service, times(1)).testControllerAdvice(1);

    }

    @Test
    public void testServiceUnavailableExceptionHandler() throws Exception {
        when(service.testControllerAdvice(1)).thenThrow(new ServiceUnavailableException());
        this.mockMvc
                .perform(get(REQUEST_MAPPING_PATH).param("param", "1").accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isServiceUnavailable()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.status", is(HttpStatus.SERVICE_UNAVAILABLE.value())));

        verify(service, times(1)).testControllerAdvice(1);

    }

    @Test
    public void testNotFoundExcpetionHandler() throws Exception {
        when(service.testControllerAdvice(1)).thenThrow(new NotFoundException());
        this.mockMvc
                .perform(get(REQUEST_MAPPING_PATH).param("param", "1").accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.status", is(HttpStatus.NOT_FOUND.value())));

        verify(service, times(1)).testControllerAdvice(1);

    }

    @Test
    public void testException() throws Exception {
        this.mockMvc
                .perform(get("/test/ex").accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.status", is(HttpStatus.INTERNAL_SERVER_ERROR.value())))
                .andExpect(jsonPath("$.message", is("Requested service was unable to process your request due to internal error.")))
                .andExpect(jsonPath("$.detailedMessage", is("test")));
    }

    private static class TestConstraintViolation implements ConstraintViolation<String> {

        @Override
        public String getMessage() {
            return "Test message";
        }

        @Override
        public String getMessageTemplate() {
            return null;
        }

        @Override
        public String getRootBean() {
            return null;
        }

        @Override
        public Class<String> getRootBeanClass() {
            return null;
        }

        @Override
        public Object getLeafBean() {
            return null;
        }

        @Override
        public Object[] getExecutableParameters() {
            return new Object[0];
        }

        @Override
        public Object getExecutableReturnValue() {
            return null;
        }

        @Override
        public Path getPropertyPath() {
            return null;
        }

        @Override
        public Object getInvalidValue() {
            return null;
        }

        @Override
        public ConstraintDescriptor<?> getConstraintDescriptor() {
            return null;
        }

        @Override
        public <U> U unwrap(Class<U> type) {
            return null;
        }
    }

}
