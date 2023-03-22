package com.example.mymgstudyapp.localnet.sdk.mgtvnew.provider;

import android.content.Context;

import java.util.List;

public class DeviceProviderManager {

    public static DeviceProviderManager sInstance;

    private List<IDiscoveryProvider> mDiscoveryProviders;
    private DiscoveryProviderListener mDiscoveryProviderListener;

    public static DeviceProviderManager getInstance() {
        return sInstance;
    }

    public void init(Context context) {

    }

    public void start() {

    }

    public void cancel() {

    }

    public void setDiscoveryProviderListener(DiscoveryProviderListener listener) {
        this.mDiscoveryProviderListener = listener;
    }
}
