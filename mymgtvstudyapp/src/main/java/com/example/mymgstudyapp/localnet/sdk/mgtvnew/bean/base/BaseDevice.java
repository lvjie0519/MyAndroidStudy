package com.example.mymgstudyapp.localnet.sdk.mgtvnew.bean.base;

import com.example.mymgstudyapp.localnet.sdk.mgtvnew.bean.DeviceType;

public class BaseDevice {
    protected DeviceType mDeviceType;
    protected String mDeviceIp;
    protected String mDeviceMac;

    public String getDeviceIp() {
        return mDeviceIp;
    }

    public void setDeviceIp(String deviceIp) {
        this.mDeviceIp = deviceIp;
    }

    public String getDeviceMac() {
        return mDeviceMac;
    }

    public void setDeviceMac(String deviceMac) {
        this.mDeviceMac = deviceMac;
    }

    public DeviceType getDeviceType() {
        return mDeviceType;
    }

    public void setDeviceType(DeviceType deviceType) {
        this.mDeviceType = deviceType;
    }
}
