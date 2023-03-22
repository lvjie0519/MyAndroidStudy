package com.example.mymgstudyapp.localnet.sdk.mgtvnew;

import com.example.mymgstudyapp.localnet.sdk.mgtvnew.bean.base.BaseDevice;

import java.util.List;

public interface IDeviceDiscoveryListener {
    void onScanStart();
    void onScanning(BaseDevice device);
    void onScanFinish(List<BaseDevice> devices);
    void onScanFailed(int code, String errorMsg);
}
