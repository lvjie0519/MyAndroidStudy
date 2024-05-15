package com.android.study.example.socket;

import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * @author lvjie
 * @creatTime 2023/4/18
 * @describe 网络相关工具类
 */
public class NetUtils {
    private final static String TAG = "NetUtils";
    // ip 字符串的最小长度(0.0.0.0)
    public static final int MIN_IP_LENGTH = 7;
    public static final int INVALIED_PORT = -1;
    private static final int BEGIN_PORT = 9987;
    private static final int END_PORT = 65535;

    /**
     * 获取一个有效的端口号，即没有被占用的端口号
     * @return
     */
    public static int getAvailablePort() {
        for (int port = BEGIN_PORT; port <= END_PORT; port++) {
            if (isPortAvailable(port)) {
                return port;
            }
        }
        return INVALIED_PORT;
    }

    /**
     * 端口是否有效，即端口是否被占用
     * @param port
     * @return
     */
    public static boolean isPortAvailable(int port) {
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

    /**
     * 获取当前设备ip地址
     * @return
     */
    public static String getCurrentHostAddress() {
        String ipAddress = "";

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

                        ipAddress = ip.getHostAddress();
                        return ipAddress;
                    }
                }
            }
        } catch (SocketException e) {
            Log.e(TAG, "getCurrentHostAddress error, error: " + e.toString());
            ipAddress = "";
        }

        return ipAddress;
    }

    public static boolean isIPv4Address(String ip) {
        if (TextUtils.isEmpty(ip)) {
            return false;
        }
        return ip.split("\\.").length == 4;
    }

    /**
     * 是否为合法的ip, 仅支持ipv4的检查
     *
     * @param ip
     * @return true/false
     */
    public static boolean isValidIp(String ip) {
        if (TextUtils.isEmpty(ip)) {
            return false;
        }

        // 定义正则表达式
        String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";

        return ip.matches(regex);
    }
}
