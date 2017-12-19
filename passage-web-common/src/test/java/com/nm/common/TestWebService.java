package com.nm.common;

import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

@RestController
@RequestMapping("/test")
public class TestWebService {

    @Inject
    private TestService service;

    @RequestMapping(value = "/advice", method = RequestMethod.GET)
    public String testControllerAdvice(@RequestParam Integer param) {
        return service.testControllerAdvice(param);
    }

    @RequestMapping(value = "/advice", method = RequestMethod.POST)
    public TestDto testControllerAdvice(@RequestBody TestDto dto) {
        return dto;
    }

    @RequestMapping(value = "/ex", method = RequestMethod.GET)
    public void testException() throws Exception {
        throw new Exception("test");
    }

}
