package com.example.mymgstudyapp.localnet.sdk.wukongtv.deviceprovider;

public class DiscoveryFilter {
    private String serviceFilter;
    private String serviceId;

    public DiscoveryFilter(String serviceFilter, String serviceId) {
        this.serviceFilter = serviceFilter;
        this.serviceId = serviceId;
    }

    public String getServiceFilter() {
        return serviceFilter;
    }

    public void setServiceFilter(String serviceFilter) {
        this.serviceFilter = serviceFilter;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
}
