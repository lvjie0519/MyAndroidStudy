package com.example.mymgstudyapp.localnet.sdk;

import android.text.TextUtils;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.UnknownHostException;

public class InetAddressUtils {
    public static boolean isIPv4Address(String ip) {
        if(TextUtils.isEmpty(ip)){
            return false;
        }
        return ip.split(".").length == 4;
    }

    public static boolean isValidIp4Address(final String hostName) {
        try {
            return Inet4Address.getByName(hostName) != null;
        } catch (UnknownHostException ex) {
            return false;
        }
    }

    public static boolean isValidIp6Address(final String hostName) {
        try {
            return Inet6Address.getByName(hostName) != null;
        } catch (UnknownHostException ex) {
            return false;
        }
    }
}
