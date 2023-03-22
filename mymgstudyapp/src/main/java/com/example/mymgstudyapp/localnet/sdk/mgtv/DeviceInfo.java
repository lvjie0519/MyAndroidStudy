package com.example.mymgstudyapp.localnet.sdk.mgtv;

public class DeviceInfo {

    private String ip;
    private String mac;

    public DeviceInfo() {
    }

    public DeviceInfo(String ip, String mac) {
        this.ip = ip;
        this.mac = mac;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    @Override
    public String toString() {
        return "DeviceInfo{" +
                "ip='" + ip + '\'' +
                ", mac='" + mac + '\'' +
                '}';
    }
}
