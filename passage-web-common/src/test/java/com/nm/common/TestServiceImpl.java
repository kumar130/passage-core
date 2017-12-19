package com.nm.common;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("test")
public class TestServiceImpl implements TestService {

    @Override
    public String testControllerAdvice(Integer param) {
        return "";
    }
}
