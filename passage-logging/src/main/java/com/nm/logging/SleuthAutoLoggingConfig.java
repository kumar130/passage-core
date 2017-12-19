package com.nm.logging;

import com.nm.logging.util.SafeStringUtils;
import com.nm.logging.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.SpanTextMap;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.cloud.sleuth.log.Slf4jSpanLogger;
import org.springframework.cloud.sleuth.log.SpanLogger;
import org.springframework.cloud.sleuth.util.TextMapUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ObjectUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;


@Configuration
@ConditionalOnWebApplication
@ConditionalOnBean(type = {"org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter"})
@EnableConfigurationProperties(SleuthLoggingProperties.class)
public class SleuthAutoLoggingConfig {

    private static final Logger logger = LoggerFactory.getLogger(SleuthAutoLoggingConfig.class);
    
    public static final String HTTP_HEADER_AUTHORIZATION = "authorization";
    public static final String SCHEME_BEARER = "bearer ";

    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private Tracer tracer;

    @Autowired
    private SleuthLoggingProperties properties;

    @Bean
    public FilterRegistrationBean registerFilter() {
        FilterRegistrationBean bean  = new FilterRegistrationBean(filter());
        bean.setOrder(3);
        return bean;
    }

    @Bean
    public Filter filter() {
        return new Filter() {
            @Override
            public void init(FilterConfig filterConfig) throws ServletException {
            }
            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
                addAuthBag(request, tracer);
                chain.doFilter(request, response);
            }
            @Override
            public void destroy() {
            }
        };
    }

    @Bean
    public SpanLogger spanLogger() {
        return new CustomSpanLogger();
    }

    private void addAuthBag(ServletRequest request, Tracer tracer) {

        Span span = tracer.getCurrentSpan();
        if(span == null) {
            span = tracer.createSpan("span");
        }

        if (request instanceof HttpServletRequest) {
            HttpServletRequest r = (HttpServletRequest) request;
            Map<String, String> map = TextMapUtil.asMap(new HttpServletRequestTextMap(r));
            String auth = map.get(HTTP_HEADER_AUTHORIZATION);
            if(auth != null && auth.toLowerCase().startsWith(SCHEME_BEARER)) {
                String fid = extractFid(auth);
                span.setBaggageItem(properties.getMdcName(), fid);
                tracer.addTag(properties.getMdcName(), fid);
                tracer.continueSpan(span);
            }
        }
    }

    private String extractFid(String token){

        String rtn = "";

        String[] parts = token.split("\\.");
        if(parts.length < 2 || parts.length > 3 ) {
            if (logger.isDebugEnabled()) {
                logger.debug("Incorrect formatted token detected: {}", SafeStringUtils.sanitize(token));
            }
            throw new IllegalArgumentException("Parsing Error. Token format does not have 3 parts: header, payload and signature.");
        }

        final byte[] payload = Base64.getDecoder().decode(parts[1]);
        try {
            rtn = Util.findValue(payload, properties.getUserJwsClaimName());
        } catch (IOException e) {
            // it means that payload is not correct json... log it
            logger.error("Invalid json format. json input: {}", SafeStringUtils.sanitize(ObjectUtils.nullSafeToString(payload)), e);
        }

        return rtn == null ? "" : rtn;
    }

    class CustomSpanLogger extends Slf4jSpanLogger {

        public CustomSpanLogger() {
            super("");
        }

        @Override
        public void logContinuedSpan(Span span) {
            super.logContinuedSpan(span);
            if(span != null) {
                Iterable<Map.Entry<String,String>> bag = span.baggageItems();
                for(Map.Entry<String, String> e : bag) {
                    MDC.put(e.getKey(), e.getValue());
                }
            }
        }
    }

    private static class HttpServletRequestTextMap implements SpanTextMap {

        private final HttpServletRequest delegate;

        HttpServletRequestTextMap(HttpServletRequest delegate) {
            this.delegate = delegate;
        }

        @Override
        public Iterator<Map.Entry<String, String>> iterator() {
            Map<String, String> map = new HashMap<>();
            Enumeration<String> headerNames = this.delegate.getHeaderNames();
            while (headerNames != null && headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                map.put(name, this.delegate.getHeader(name));
            }
            return map.entrySet().iterator();
        }
        @Override
        public void put(String key, String value) {
        }
    }

}
