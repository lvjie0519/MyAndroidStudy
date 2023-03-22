package com.example.mymgstudyapp.localnet.sdk.mgtv.utils;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.nfc.NfcAdapter;

import com.example.mymgstudyapp.localnet.sdk.mgtv.WifiInfo;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteOrder;

public class IPUtil {

    public static WifiInfo getCurrentWifiInfo(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        if (wifiManager == null) {
            return null;
        }



        return new WifiInfo(wifiManager.getConnectionInfo().getIpAddress(),
                wifiManager.getConnectionInfo().getMacAddress(),
                wifiManager.getDhcpInfo().netmask);
    }

    public static String ipIntToString(int ip) {
        if (ip <= 0) {
            return "";
        }

        return ((ip >> 24) & 0xFF) + "."
                + ((ip >> 16) & 0xFF) + "."
                + ((ip >> 8) & 0xFF) + "."
                + (ip & 0xFF);
    }

    private String convertToIpString(Integer num) {
        if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
            num = Integer.valueOf(Integer.reverseBytes(num.intValue()));
        }
        try {
            return InetAddress.getByAddress(BigInteger.valueOf(num.intValue()).toByteArray()).getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int[] getLocalNetworkRange(Context context){
        int[] range = new int[2];

        WifiInfo wifiInfo = getCurrentWifiInfo(context);
        if(wifiInfo == null){
            return range;
        }

        int network = wifiInfo.getIp() & wifiInfo.getNetmask();
        int broadcast = network | (~wifiInfo.getNetmask());

        range[0] = network+1;
        range[1] = broadcast-1;

        return range;
    }
}
