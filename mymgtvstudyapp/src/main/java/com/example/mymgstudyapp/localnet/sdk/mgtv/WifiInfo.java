package com.example.mymgstudyapp.localnet.sdk.mgtv;

public class WifiInfo {
    private int ip;
    private String mac;
    private int netmask;

    public WifiInfo() {
    }

    public WifiInfo(String mac, int ip, int netmask) {
        this.mac = mac;
        this.ip = ip;
        this.netmask = netmask;
    }

    public int getIp() {
        return ip;
    }

    public void setIp(int ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public int getNetmask() {
        return netmask;
    }

    public void setNetmask(int netmask) {
        this.netmask = netmask;
    }

    @Override
    public String toString() {
        return "WifiInfo{" +
                "ip=" + ip +
                ", mac='" + mac + '\'' +
                ", netmask=" + netmask +
                '}';
    }
}
