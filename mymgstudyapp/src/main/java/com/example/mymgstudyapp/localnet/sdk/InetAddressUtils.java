package com.example.mymgstudyapp.localnet.sdk;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.UnknownHostException;

public class InetAddressUtils {
    public static boolean isIPv4Address(String ip) {
        return true;
    }

    public boolean isValidIp4Address(final String hostName) {
        try {
            return Inet4Address.getByName(hostName) != null;
        } catch (UnknownHostException ex) {
            return false;
        }
    }

    public boolean isValidIp6Address(final String hostName) {
        try {
            return Inet6Address.getByName(hostName) != null;
        } catch (UnknownHostException ex) {
            return false;
        }
    }
}
