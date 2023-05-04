package com.example.mymgstudyapp.localnet.sdk.mgtvnew.provider;

import com.connectsdk.discovery.DiscoveryProviderListener;

public interface IDiscoveryProvider {
    public void start();
    public void cancel();
    public void addListener(DiscoveryProviderListener listener);
    public void removeListener(DiscoveryProviderListener listener);
}
