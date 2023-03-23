package com.example.mymgstudyapp.localnet.sdk.mgtv.utils;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.nfc.NfcAdapter;
import android.text.TextUtils;

import com.example.mymgstudyapp.localnet.sdk.mgtv.WifiInfo;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteOrder;

public class NetworkUtil {

    public static WifiInfo getCurrentWifiInfo(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager == null) {
            return null;
        }

        if (wifiManager.getWifiState() != WifiManager.WIFI_STATE_ENABLED) {
            // 设备wifi没有开启， 返回null
            return null;
        }

        DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();

        return new WifiInfo(wifiManager.getConnectionInfo().getMacAddress(),
                dhcpInfo.ipAddress,
                wifiManager.getDhcpInfo().netmask);
    }

    /**
     * 整形ip 转换位 字符串ip 格式
     * @param ip 系统返回的整形数据
     * @return
     */
    public static String ipIntToString(int ip) {
        if (ip <= 0) {
            return "";
        }

        if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
            ip = Integer.valueOf(Integer.reverseBytes(ip));
        }
        try {
            return InetAddress.getByAddress(BigInteger.valueOf(ip).toByteArray()).getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return "";
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

    public static int[] convertIpToIntArray(String ip) {
        if(TextUtils.isEmpty(ip)){
            return null;
        }

        String[] array = ip.split(".");
        if(array.length != 4){
            return null;
        }

        try {
            int[] result = new int[array.length];
            for (int i=0; i< array.length; i++) {
                result[i] = Integer.parseInt(array[i]);
            }
            return result;
        }catch (Exception e){

        }

        return null;
    }



    public static int[] getLocalNetworkRange(Context context) {
        int[] range = new int[2];

        WifiInfo wifiInfo = getCurrentWifiInfo(context);
        if (wifiInfo == null) {
            return range;
        }

        int network = wifiInfo.getIp() & wifiInfo.getNetmask();
        int broadcast = network | (~wifiInfo.getNetmask());

        range[0] = network + 1;
        range[1] = broadcast - 1;

        return range;
    }
}
