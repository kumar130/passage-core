package com.nm.common;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.springframework.util.ObjectUtils;
import com.nm.logging.util.SafeStringUtils;

@ControllerAdvice
public class WebServiceControllerAdvice {

    private final ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
            false);

    private static final Logger logger = LoggerFactory.getLogger(WebServiceControllerAdvice.class);

    private WebServiceError buildWebServiceError(HttpStatus httpStatus, Exception ex, String message,
            String detailedMessage) {
        logger.error(String.format("Request resulted with exception. Details: [http status=%s, message=%s, details=%s]", SafeStringUtils.sanitize(ObjectUtils.nullSafeToString(httpStatus)), SafeStringUtils.sanitize(message), SafeStringUtils.sanitize(detailedMessage)),SafeStringUtils.sanitize(ObjectUtils.nullSafeToString(ex)));
        WebServiceError webServiceError = new WebServiceError();
        webServiceError.setStatus(httpStatus.value());
        webServiceError.setMessage(message);
        webServiceError.setDetailedMessage(detailedMessage);
        webServiceError.setException(logger.isDebugEnabled() ? ex : null);
        return webServiceError;
    }

    @ExceptionHandler({ RuntimeException.class })
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody WebServiceError handleServerException(RuntimeException ex) {
        return buildWebServiceError(HttpStatus.INTERNAL_SERVER_ERROR, ex,
                "Sorry, internal server error has occurred.", ex.getMessage());
    }

    @ExceptionHandler({ HttpMessageNotReadableException.class })
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody WebServiceError handleHttpMessageNotReadableException(HttpMessageNotReadableException ex)
            throws IOException {
        String detailed = ex.getMostSpecificCause() == null ? ex.getMessage() : ex.getMostSpecificCause().getMessage();
        return buildWebServiceError(HttpStatus.BAD_REQUEST, ex, ex.getMessage(), detailed);
    }

    @ExceptionHandler({ MissingServletRequestParameterException.class })
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody WebServiceError handleMissingServletRequestParameterException(
            MissingServletRequestParameterException ex) {
        return buildWebServiceError(HttpStatus.BAD_REQUEST, ex,
                "The requested resource url is missing some parameters.", ex.getMessage());
    }

    @ExceptionHandler({ TypeMismatchException.class })
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody WebServiceError handleTypeMismatchException(TypeMismatchException ex) {
        return buildWebServiceError(HttpStatus.BAD_REQUEST, ex,
                "The requested resource url parameter is wrong type.", ex.getMessage());
    }

    @ExceptionHandler(value = {NoSuchRequestHandlingMethodException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ResponseBody
    public WebServiceError handleNoSuchRequestHandlingMethodException(NoSuchRequestHandlingMethodException ex,
            HttpServletRequest request) {
        String detailed = String.format("The handler for this %s url does not exist", request.getRequestURL());
        return buildWebServiceError(HttpStatus.NOT_FOUND, ex, "Oops. Incorrect url.", detailed);
    }

    @ExceptionHandler({ HttpRequestMethodNotSupportedException.class })
    @ResponseStatus(value = HttpStatus.METHOD_NOT_ALLOWED)
    public @ResponseBody WebServiceError handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException ex) {
        String detailed = String.format("Incorrect http method %s supplied. Allowed: %s.", ex.getMethod(),
                Arrays.toString(ex.getSupportedMethods()));
        return buildWebServiceError(HttpStatus.METHOD_NOT_ALLOWED, ex, "Oops. Invalid http method.", detailed);
    }

    @ExceptionHandler({ HttpMediaTypeNotAcceptableException.class })
    @ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
    public @ResponseBody WebServiceError handleHttpMediaTypeNotAcceptableException(
            HttpMediaTypeNotAcceptableException ex) {
        String detailed = String.format("%s. Allowed: %s", ex.getLocalizedMessage(), ex.getSupportedMediaTypes());
        return buildWebServiceError(HttpStatus.NOT_ACCEPTABLE, ex, "Oops. Media type is not accepted.", detailed);
    }

    @ExceptionHandler({ HttpMediaTypeNotSupportedException.class })
    @ResponseStatus(value = HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public @ResponseBody WebServiceError handleHttpMediaTypeNotSupportedException(
            HttpMediaTypeNotSupportedException ex) {
        String detailed = String.format("%s. Supported: %s", ex.getLocalizedMessage(), ex.getSupportedMediaTypes());
        return buildWebServiceError(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ex, "Oops. Media type is not supported.",
                detailed);
    }

    @ExceptionHandler({ HttpClientErrorException.class })
    public @ResponseBody WebServiceError handleHttpMediaTypeNotSupportedException(HttpClientErrorException ex,
            HttpServletResponse response) {
        response.setStatus(ex.getStatusCode().value());
        try {
            return mapper.readValue(ex.getResponseBodyAsByteArray(), WebServiceError.class);
        } catch (IOException e) {
            logger.error("Error reading the error message body");
            return buildWebServiceError(ex.getStatusCode(), ex, ex.getMessage(), ex.getMessage());
        }
    }

    @ExceptionHandler({ ConstraintViolationException.class })
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody WebServiceError handleConstraintViolationException(ConstraintViolationException ex) {
        String detailed = ex.getConstraintViolations().stream()
                .map(e -> String.format("%s: %s", e.getPropertyPath(), e.getMessage()))
                .collect(Collectors.joining("\n"));
        return buildWebServiceError(HttpStatus.BAD_REQUEST, ex, "Oops. Invalid request argument(s) supplied.",
                detailed);
    }

    @ExceptionHandler({ NotFoundException.class })
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public @ResponseBody WebServiceError handleNotFoundException(NotFoundException ex) {
        return buildWebServiceError(HttpStatus.NOT_FOUND, ex, "Requested resource is not found.", ex.getMessage());
    }

    @ExceptionHandler({ ForbiddenException.class, AccessDeniedException.class })
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public @ResponseBody WebServiceError handleForbiddenException(RuntimeException ex) {
        return buildWebServiceError(HttpStatus.FORBIDDEN, ex, "Sorry, you have insufficient privileges to access this resource.", ex.getMessage());
    }

    @ExceptionHandler({ ServiceUnavailableException.class })
    @ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE)
    public @ResponseBody WebServiceError handleServiceUnavailableException(ServiceUnavailableException ex) {
        return buildWebServiceError(HttpStatus.SERVICE_UNAVAILABLE, ex, "Requested service is temporarily unavailable.", ex.getMessage());
    }

    @ExceptionHandler({ Exception.class })
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody WebServiceError handleGenericException(Exception ex) {
        return buildWebServiceError(HttpStatus.INTERNAL_SERVER_ERROR, ex, "Requested service was unable to process your request due to internal error.", ex.getMessage());
    }
}
