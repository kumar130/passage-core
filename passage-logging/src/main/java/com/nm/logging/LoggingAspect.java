package com.nm.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.stream.Collectors;

@Aspect
@Component
public class LoggingAspect {

    @Around("execution(* com.nm..*(..)) && @annotation(org.springframework.web.bind.annotation.RequestMapping) && @annotation(requestMapping)")
    public Object logWebRequest(ProceedingJoinPoint pjp, RequestMapping requestMapping) throws Throwable {
        Logger logger = LoggerFactory.getLogger(pjp.getTarget().getClass());
        long startTime = System.currentTimeMillis();

        if (logger.isInfoEnabled()) {
            logger.info("Enter: {}, URL: {}, HTTP: {}, args=[{}]", pjp.getSignature().toShortString(),
                    Arrays.toString(requestMapping.value()), Arrays.toString(requestMapping.method()), logArguments(pjp));
        }

        Object result = null;
        try {
            result = pjp.proceed();
            return result;
        } finally {
            if (logger.isInfoEnabled()) {
                logger.info("Exit: {}, Response Time: {} ms, Return: {}", pjp.getSignature().toShortString(),
                        System.currentTimeMillis() - startTime, result != null ? result.toString() : null);
            }
        }
    }

    private String logArguments(ProceedingJoinPoint pjp) {
        return Arrays.stream(pjp.getArgs()).map(i -> i == null ? "<null>" : i.toString()).collect(Collectors.joining(","));
    }
}
