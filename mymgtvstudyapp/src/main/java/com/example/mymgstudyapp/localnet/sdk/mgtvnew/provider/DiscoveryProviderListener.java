package com.example.mymgstudyapp.localnet.sdk.mgtvnew.provider;

import com.example.mymgstudyapp.localnet.sdk.mgtvnew.bean.base.BaseDevice;

public interface DiscoveryProviderListener {
    void onDeviceDiscovery(BaseDevice baseDevice);
}
