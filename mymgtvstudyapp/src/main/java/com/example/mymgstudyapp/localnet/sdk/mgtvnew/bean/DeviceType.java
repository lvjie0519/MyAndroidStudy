package com.example.mymgstudyapp.localnet.sdk.mgtvnew.bean;

public enum DeviceType {

    LocalNetDevice("LocalNetDevice"),
    BleDevice("BleDevice");

    private String mDeviceType;
    DeviceType(String deviceType){
        this.mDeviceType = deviceType;
    }

    @Override
    public String toString() {
        return "DeviceType{" +
                "mDeviceType='" + mDeviceType + '\'' +
                '}';
    }
}
