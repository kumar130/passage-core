package com.nm.discovery;

import com.nm.cloud.openshift.ServiceBinding;
import com.nm.cloud.openshift.SpringCloudRestClient;
import com.nm.discovery.core.ServiceDiscoveryProvider;
import com.nm.discovery.core.ServiceInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.service.UriBasedServiceInfo;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class ServiceDiscoveryProviderImpl implements ServiceDiscoveryProvider {
    @Autowired
    private ServiceBinding serviceBinding;

    private SpringCloudRestClient getClientInstance(DiscoveryServices serviceId) {
        return serviceBinding.getServiceInstance(serviceId.getServiceId());
    }

    @Override
    public <T> T invokeExternalService(DiscoveryServices serviceId, String uri, Optional<Class> entityClass) {
        return invokeExternalService(serviceId, uri, entityClass, HttpMethod.GET, null, null);
    }

    @Override
    public <T> T invokeExternalService(DiscoveryServices serviceId, String uri, Optional<Class> entityClass, Map<String, String> headers) {
        return invokeExternalService(serviceId, uri, entityClass, HttpMethod.GET, null, null, headers);
    }

    @Override
    public <T> T invokeExternalService(DiscoveryServices serviceId, String uri, Optional<Class> entityClass,
                                       HttpMethod verb, Map<String, Object> requestParams, Object entity) {
        return invokeExternalService(serviceId, uri, entityClass, verb, requestParams, entity, null);
    }

    @Override
    public <T> T invokeExternalService(DiscoveryServices serviceId, String uri, Optional<Class> entityClass,
                                        HttpMethod verb, Map<String, Object> requestParams, Object entity,
                                        Map<String, String> headers) {
        return getClientInstance(serviceId).execute(uri, entityClass, verb, requestParams, entity, headers);
    }


    @Override
    public <T> T uploadFile(DiscoveryServices serviceId, String uri, byte[] bytes, HttpMethod method) {
        return getClientInstance(serviceId).uploadFile(uri, bytes, method);
    }

    @Override
    public byte[] downloadFile(DiscoveryServices serviceId, String uri) {
        return getClientInstance(serviceId).downloadFile(uri);
    }

    @Override
    public byte[] downloadFile(DiscoveryServices serviceId, String uri, Map<String, String> headers) {
        return getClientInstance(serviceId).downloadFile(uri, headers);
    }

    @Override
    public ServiceInfo discoverService(DiscoveryServices serviceId) {
        UriBasedServiceInfo serviceData = serviceBinding.getServiceInfo(serviceId.getServiceId());
        return new ServiceInfo(serviceId.getServiceId(), serviceData.getHost(), serviceData.getPort());
    }
}
