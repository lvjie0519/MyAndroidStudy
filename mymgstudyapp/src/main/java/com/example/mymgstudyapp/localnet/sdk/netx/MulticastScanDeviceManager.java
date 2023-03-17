package com.example.mymgstudyapp.localnet.sdk.netx;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.io.IOException;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteOrder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class MulticastScanDeviceManager {

    private static final String TAG = "MulticastScanDevice";
    private static final int PORT = Integer.parseInt(System.getProperty("net.mdns.port", "5353"));;
    private static final String IP = "224.0.0.251";

    private static MulticastScanDeviceManager sInstance;
    private boolean isInit = false;
    private volatile boolean isLoopRecieve = false;
    private MulticastSocket sendMulticastSocket;
    private WifiManager wifiManager;
    private WifiManager.MulticastLock multicastLock;

    private Map<String, String> scanInfosMap = new HashMap<>();

    private MulticastScanDeviceManager() {
    }

    public void test(Context context) {
        // 2. 获取组播锁
//        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//        multicastLock = wifiManager.createMulticastLock("multicast.test");
//        multicastLock.acquire();

        try {
            // 组播的初始化
            MulticastSocket multicastSocket = new MulticastSocket(PORT);
            InetAddress group = null;
            group = InetAddress.getByName(IP);
            multicastSocket.setLoopbackMode(false);
            multicastSocket.joinGroup(group);

            // 发送组播数据
            String message = "hello";
            DatagramPacket datagramPacket = new DatagramPacket(message.getBytes(), message.length(), group, PORT);
            multicastSocket.send(datagramPacket);

            // 接收组播数据

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static MulticastScanDeviceManager getInstance() {
        if (sInstance == null) {
            synchronized (MulticastScanDeviceManager.class) {
                if (sInstance == null) {
                    sInstance = new MulticastScanDeviceManager();
                }
            }
        }

        return sInstance;
    }

    public void init(Context context) {
        Log.i(TAG, "init start..." + isInit);
        if (isInit) {
            return;
        }

        isInit = true;
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            multicastLock = wifiManager.createMulticastLock("multicast.test");
            multicastLock.setReferenceCounted(true);
            multicastLock.acquire();
            Log.i(TAG, "multicastLock.acquire...");
        }

        // 组播的初始化
        try {
            Log.i(TAG, "组播的初始化...start");
            sendMulticastSocket = new MulticastSocket(PORT);
            InetAddress group = null;
            group = InetAddress.getByName(IP);
            sendMulticastSocket.setLoopbackMode(false);
            sendMulticastSocket.joinGroup(group);
            sendMulticastSocket.setSoTimeout(100000);
            Log.i(TAG, "组播的初始化...end");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessageAsync(String message) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "sendMessageAsync..." + message);
                sendMessageSync(message);
            }
        }).start();

    }

    private void sendMessageSync(String message) {
        try {
            DatagramPacket datagramPacket = new DatagramPacket(message.getBytes(), message.length(), InetAddress.getByName(IP), PORT);
            sendMulticastSocket.send(datagramPacket);
            Log.i(TAG, "sendMessageSync..." + message + " success.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receiveMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MulticastSocket multicastSocket2 = null;
                try {
                    multicastSocket2 = new MulticastSocket(PORT);
                    multicastSocket2.joinGroup(InetAddress.getByName(IP));
                    Log.i(TAG, "construct recieve multicastSocket2 success.");

                    byte[] rev = new byte[2048];
                    DatagramPacket datagramPacket = new DatagramPacket(rev, rev.length);
                    multicastSocket2.receive(datagramPacket);
                    Log.i(TAG, "receive data: " + new String(datagramPacket.getData()));
                    Log.i(TAG, "receive data: getHostName=" + datagramPacket.getAddress().getHostName()
                            + "  getHostAddress=" + datagramPacket.getAddress().getHostAddress());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    public void receiveMessageLoopAsync() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                receiveMessageLoop();
            }
        }).start();
    }

    private void receiveMessageLoop() {
        isLoopRecieve = true;
        MulticastSocket multicastSocket;
        String str;
        int i;

        try {
            MulticastSocket multicastSocket2 = new MulticastSocket(5353);
            multicastSocket2.joinGroup(InetAddress.getByName("224.0.0.251"));

            // !MainActivity.J0.booleanValue()   J0 变量
            while (isLoopRecieve) {
                try {
                    DatagramPacket datagramPacket = new DatagramPacket(new byte[2048], 2048);
                    multicastSocket2.receive(datagramPacket);

//                    Log.i(TAG, "receive data: "+new String(datagramPacket.getData()));
                    Log.i(TAG, "receive data: getHostName=" + datagramPacket.getAddress().getHostName()
                            + "  getHostAddress=" + datagramPacket.getAddress().getHostAddress());
                    scanInfosMap.put(datagramPacket.getAddress().getHostAddress(), datagramPacket.getAddress().getHostName());

                    int i2 = 0;
                    int i3 = 1;
                    byte[] bArr = {datagramPacket.getData()[2], datagramPacket.getData()[3]};
                    int B = convertArr(bArr, 0);
                    int B2 = convertArr(bArr, 12) + convertArr(bArr, 13)
                            + convertArr(bArr, 14) + convertArr(bArr, 15);

                    if (B == 1 && B2 == 0) {
                        TreeMap treeMap = new TreeMap();
                        String str2 = "";
                        String str3 = "";
                        StringBuilder sb = new StringBuilder();
                        String hostAddress = datagramPacket.getAddress().getHostAddress();
                        int intValue = new BigInteger(1, new byte[]{datagramPacket.getData()[6], datagramPacket.getData()[7]}).intValue();
                        int i4 = 12;

                        while (intValue > 0) {
                            byte[] bArr2 = new byte[256];
                            if (i4 > datagramPacket.getData().length) {
                                return;
                            }
                            int i5 = i4;

                            while (datagramPacket.getData()[i5] != 0) {
                                byte[] bArr3 = new byte[i3];
                                bArr3[i2] = datagramPacket.getData()[i5];
                                if (convertArr(bArr3, i2) + convertArr(bArr3, i3) == 0) {
                                    int intValue2 = new BigInteger(bArr3).intValue();
                                    int i6 = i5 + 1;
                                    int i7 = i6;
                                    int i8 = 0;
                                    while (true) {
                                        i = i6 + intValue2;
                                        if (i7 >= i) {
                                            break;
                                        }
                                        bArr2[i8] = datagramPacket.getData()[i7];
                                        i8++;
                                        i7++;
                                    }
                                    String replaceAll = new String(bArr2, 0, intValue2, "ASCII").replaceAll("\\p{Cc}", ".");
                                    if (!str2.equals("")) {
                                        replaceAll = str2 + "." + replaceAll;
                                    }
                                    str2 = replaceAll;
                                    i5 = i;
                                } else {
                                    int intValue3 = new BigInteger(1, new byte[]{datagramPacket.getData()[i5 + 1]}).intValue();
                                    if (str2.equals("")) {
                                        str = (String) treeMap.get(Integer.valueOf(intValue3));
                                    } else {
                                        str = str2 + "." + ((String) treeMap.get(Integer.valueOf(intValue3)));
                                    }
                                    i5 += 2;
                                    str2 = str;
                                }
                                i2 = 0;
                                i3 = 1;
                            }

                            treeMap.put(Integer.valueOf(i4), str2);
                            int i9 = i5 + 2;
                            int intValue4 = new BigInteger(1, new byte[]{datagramPacket.getData()[i9 - 1], datagramPacket.getData()[i9]}).intValue();
                            int i10 = i9 + 2 + 4 + 2;
                            int i11 = 1;
                            int intValue5 = new BigInteger(new byte[]{datagramPacket.getData()[i10 - 1], datagramPacket.getData()[i10]}).intValue();
                            int i12 = i10 + 1;
                            if (intValue4 == 1) {
                                int i13 = i12;
                                while (i13 < i12 + intValue5) {
                                    byte[] bArr4 = new byte[i11];
                                    bArr4[0] = datagramPacket.getData()[i13];
                                    int intValue6 = new BigInteger(i11, bArr4).intValue();
                                    if (sb.toString().equals("")) {
                                        sb = new StringBuilder(String.valueOf(intValue6));
                                    } else {
                                        sb.append(".");
                                        sb.append(intValue6);
                                    }
                                    i13++;
                                    i11 = 1;
                                }
                            }

                            if (intValue4 == 12) {
                                String str4 = "";
                                byte[] bArr5 = new byte[intValue5];
                                int i14 = i12;
                                int i15 = 0;
                                while (true) {
                                    if (i14 >= i12 + intValue5) {
                                        multicastSocket = multicastSocket2;
                                        break;
                                    }
                                    byte[] bArr6 = new byte[1];
                                    multicastSocket = multicastSocket2;
                                    try {
                                        bArr6[0] = datagramPacket.getData()[i12];
                                        String str5 = str4;
                                        if (convertArr(bArr6, 0) + convertArr(bArr6, 1) != 0) {
                                            str4 = (String) treeMap.get(Integer.valueOf(new BigInteger(1, new byte[]{datagramPacket.getData()[i12 + 1]}).intValue()));
                                            intValue5 -= 2;
                                            break;
                                        }
                                        bArr5[i15] = datagramPacket.getData()[i14];
                                        i15++;
                                        i14++;
                                        multicastSocket2 = multicastSocket;
                                        str4 = str5;
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        multicastSocket2 = multicastSocket;
                                    }
                                }
                                str3 = new String(bArr5, 0, intValue5, "ASCII").replaceAll("\\p{Cc}", ".") + str4;
                            } else {
                                multicastSocket = multicastSocket2;
                            }

                            if (str3.length() > 0) {
                                String[] split = str3.split("\\.");
                                str3 = str3.substring(0, 1).equals(".") ? split[1] : split[0];
                            }

                            Log.i(TAG, "str3: " + str3);
//                            synchronized (MainActivity.this.D0.h()) {
//                                if (a(hostAddress) != null && MainActivity.this.D0.b(a(hostAddress)).booleanValue()) {
//                                    MainActivity.this.D0.d(a(hostAddress)).x(str3);
//                                }
//                            }
                            i4 = i12 + intValue5;
                            intValue--;
                            multicastSocket2 = multicastSocket;
                            i2 = 0;
                            i3 = 1;
                        }
                    }

                    multicastSocket = multicastSocket2;

                } catch (Exception e) {
                    e.printStackTrace();
                    multicastSocket = multicastSocket2;
                }

                multicastSocket2 = multicastSocket;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i(TAG, "stop recieve message");
    }

    public Long a(String str) {
        try {
            String[] split = str.split("\\.");
            StringBuilder sb = new StringBuilder();
            int length = split.length;
            for (int i = 0; i < length; i++) {
                String str2 = split[i];
                if (str2.length() == 1) {
                    str2 = "00" + str2;
                }
                if (str2.length() == 2) {
                    str2 = "0" + str2;
                }
                sb.append(str2);
            }
            return Long.valueOf(Long.parseLong(sb.toString()));
        } catch (NumberFormatException unused) {
            return null;
        }
    }


    private int convertArr(byte[] bArr, int i) {
        return (bArr[i / 8] >> (8 - ((i % 8) + 1))) & 1;
    }

    public void stopRecieveMessage() {
        isLoopRecieve = false;
    }


    public void test1(){
        DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();
//        o B2 = geto(dhcpInfo.ipAddress);

        d(dhcpInfo.gateway);
    }

    /**
     *
     * @param num
     * @return  192.168.3.1
     */
    private String d(Integer num) {
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

    /**
     * 看起来像是 通过ip找网络接口， 并保存网络位， 可以得出内网数据
     * @param ipAdress
     * @return
     */
    private o geto(int ipAdress){
        o oVar = null;
        try {
            // 获取本机全部的网络接口
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            // 遍历网络接口
            while (networkInterfaces.hasMoreElements()) {
                // 获取一个网络接口
                NetworkInterface nextElement = networkInterfaces.nextElement();
                // 不是回环接口
                if (!nextElement.isLoopback()) {
                    for (InterfaceAddress interfaceAddress : nextElement.getInterfaceAddresses()) {
                        byte[] ip1 = ip2byteArray(ipAdress);
                        byte[] ip2 = ip2byteArray(ipAdress);
                        byte[] ip3 = ip2byteArray(ipAdress);
                        byte[] ip4 = ip2byteArray(ipAdress);
                        String str = new String(ip2byteArray(ipAdress), "UTF-8");
                        String str2 = new String(interfaceAddress.getAddress().getAddress(), "UTF-8");

                        if (interfaceAddress.getAddress().getAddress().length == 4) {
                            // ipv4 32位地址
                            if (str.equals(str2)) {
                                if (oVar == null) {
                                    oVar = new o();
                                }
                                byte[] bArr = new byte[4];
                                // 返回此地址的网络前缀长度
                                int networkPrefixLength = interfaceAddress.getNetworkPrefixLength();
                                oVar.setNetworkPrefixLength(networkPrefixLength);
                                for (int i2 = 0; i2 < networkPrefixLength; i2++) {
                                    R(bArr, i2, 1);
                                    oVar.k(bArr);
                                }
                                while (networkPrefixLength < 32) {
                                    R(ip1, networkPrefixLength, 0);
                                    R(ip3, networkPrefixLength, 0);
                                    R(ip2, networkPrefixLength, 1);
                                    R(ip4, networkPrefixLength, 1);
                                    networkPrefixLength++;
                                }
                                oVar.h(ip1);
                                oVar.f(ip2);
                                ip3[3] = (byte) (ip3[3] + 1);
                                oVar.j(ip3);
                                ip4[3] = (byte) (ip4[3] - 1);
                                oVar.g(ip4);
                            }
                        } else if (interfaceAddress.getAddress().getAddress().length == 16) {
                            short networkPrefixLength2 = interfaceAddress.getNetworkPrefixLength();
                            if (oVar == null) {
                                oVar = new o();
                            }
                            oVar.i(Integer.valueOf(networkPrefixLength2));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return oVar;
    }

    /**
     * 将ip 整形 转换为byte 数组
     * @param i
     * @return
     */
    byte[] ip2byteArray(int i) {
        byte[] bArr = new byte[4];
        bArr[0] = (byte) (i >>> 24);
        bArr[1] = (byte) (i >>> 16);
        bArr[2] = (byte) (i >>> 8);
        int i2 = 3;
        bArr[3] = (byte) i;
        for (int i3 = 0; i2 > i3; i3++) {
            byte b2 = bArr[i2];
            bArr[i2] = bArr[i3];
            bArr[i3] = b2;
            i2--;
        }
        return bArr;
    }

    public void R(byte[] bArr, int i, int i2) {
        int i3 = i / 8;
        int i4 = i % 8;
        bArr[i3] = (byte) ((i2 << (8 - (i4 + 1))) | ((byte) (bArr[i3] & (65407 >> i4) & 255)));
    }

    public void printScanInfos() {
        Log.i(TAG, "size: " + scanInfosMap.size());
        for (Map.Entry entry : scanInfosMap.entrySet()) {
            Log.i(TAG, "key: " + entry.getKey() + ", value: " + entry.getValue());
        }
    }

    public void clear() {
        multicastLock.release();
    }
}
