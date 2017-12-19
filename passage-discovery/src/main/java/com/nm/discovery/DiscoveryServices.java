package com.nm.discovery;

public enum DiscoveryServices {
    WORKFLOW_SERVICE("workflow", "Workflow Service"),
    PCC_SERVICE("pcc", "PCC Service"),
    GATEWAY_SERVICE("gateway", "Gateway Adapter");

    private String serviceId;
    private String serviceName;

    DiscoveryServices(final String serviceId, final String serviceName) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
    }

    public String getServiceId() {
        return serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }
}

