package com.nm.discovery.core;

import com.netflix.client.http.HttpRequest;
import com.nm.discovery.DiscoveryServices;
import org.springframework.http.HttpMethod;

import java.util.Map;
import java.util.Optional;

public interface ServiceDiscoveryProvider {
    <T> T invokeExternalService(DiscoveryServices serviceId, String uri, Optional<Class> entityClass);

    <T> T invokeExternalService(DiscoveryServices serviceId, String uri, Optional<Class> entityClass, Map<String, String> headers);

    <T> T invokeExternalService(DiscoveryServices serviceId, String uri, Optional<Class> entityClass, HttpMethod verb, Map<String, Object> requestParams, Object entity);

    <T> T invokeExternalService(DiscoveryServices serviceId, String uri, Optional<Class> entityClass, HttpMethod verb, Map<String, Object> requestParams, Object entity, Map<String, String> headers);

    <T> T uploadFile(DiscoveryServices serviceId, String uri, byte[] bytes, HttpMethod method);

    byte[] downloadFile(DiscoveryServices serviceId, String uri);

    byte[] downloadFile(DiscoveryServices serviceId, String uri, Map<String, String> headers);

    ServiceInfo discoverService(DiscoveryServices serviceId);
}
