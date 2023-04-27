package com.android.study.example.websocket.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

public class NetUtils {
    private final static String TAG = "NetUtils";
    public static final int INVALIED_PORT = -1;
    private static final int BEGIN_PORT = 8887;
    private static final int END_PORT = 65535;

    public static int getAvailablePort() {
        for (int port = BEGIN_PORT; port <= END_PORT; port++) {
            if (isPortAvailable(port)) {
                return port;
            }
        }
        return INVALIED_PORT;
    }

    private static boolean isPortAvailable(int port) {
        Socket s = null;
        boolean retVal = false;
        try {
            s = new Socket();
            s.bind(new InetSocketAddress(port));
            retVal = true;
        } catch (IOException e) {
            Log.e(TAG, "isPortAvailable: socket bind port fail.");
        } finally {
            if (s != null) {
                try {
                    s.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return retVal;
    }

    public static String getLocAddress() {
        String ipaddress = "";

        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            // 遍历所用的网络接口
            while (en.hasMoreElements()) {
                NetworkInterface networks = en.nextElement();
                // 得到每一个网络接口绑定的所有ip
                Enumeration<InetAddress> address = networks.getInetAddresses();
                // 遍历每一个接口绑定的所有ip
                while (address.hasMoreElements()) {
                    InetAddress ip = address.nextElement();
                    if (!ip.isLoopbackAddress() && isIPv4Address(ip.getHostAddress())) {

                        ipaddress = ip.getHostAddress();
                        Log.i(TAG, "本机IP: "+ipaddress);
                        return ipaddress;
                    }
                }
            }
        } catch (SocketException e) {
            Log.e("", "获取本地ip地址失败");
            ipaddress = "";
            e.printStackTrace();
        }

        Log.i(TAG, "本机IP:" + ipaddress);
        return ipaddress;
    }

    public static boolean isIPv4Address(String ip) {
        if(TextUtils.isEmpty(ip)){
            return false;
        }
        return ip.split("\\.").length == 4;
    }
}
