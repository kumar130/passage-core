package com.nm.discovery.core;

public class ServiceInfo {
    private String serviceId;
    private String scheme = "http";
    private String host;
    private Integer port;

    public ServiceInfo() {}

    public ServiceInfo(String serviceId, String host, Integer port) {
        this.serviceId = serviceId;
        this.host = host;
        this.port = port;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}
