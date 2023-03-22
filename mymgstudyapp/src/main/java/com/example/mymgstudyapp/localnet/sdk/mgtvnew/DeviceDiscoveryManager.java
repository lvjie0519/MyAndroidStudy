package com.example.mymgstudyapp.localnet.sdk.mgtvnew;

import android.content.Context;

import com.example.mymgstudyapp.localnet.sdk.mgtvnew.bean.base.BaseDevice;
import com.example.mymgstudyapp.localnet.sdk.mgtvnew.provider.DeviceProviderManager;

import java.util.List;
import java.util.Map;

public class DeviceDiscoveryManager {

    public static final int STATE_START = 1;
    public static final int STATE_STOP = 2;


    private static DeviceDiscoveryManager sInstance;

    private IDeviceDiscoveryListener mIDeviceDiscoveryListener;
    private int mCurrentDiscoveryState = STATE_STOP;
    private Map<String, BaseDevice> mDeviceMap;

    public static DeviceDiscoveryManager getInstance() {
        return sInstance;
    }

    public void init(Context context){
        DeviceProviderManager.getInstance().init(context);
    }

    public void setDeviceDiscoveryListener(IDeviceDiscoveryListener listener) {
        this.mIDeviceDiscoveryListener = listener;
    }

    public void startDiscovery() {
    }

    public void cancelDiscovery() {
    }

    public int getState() {
        return mCurrentDiscoveryState;
    }

    public boolean isDiscovering() {
        return true;
    }

}
