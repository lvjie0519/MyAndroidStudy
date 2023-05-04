package com.example.mymgstudyapp.localnet.sdk.wukongtv;

import android.content.Context;
import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UdpManager {

    /* renamed from: a */
    static UdpManager instance = null;

    /* renamed from: b */
    public static int f14818b = 12305;
    public static int c = 12303;

    /* renamed from: d */
    private DatagramSocket mDatagramSocket;

    /* renamed from: f */
    private int mVersionCode = 0;

    public static UdpManager getInstance() {
        if (instance == null) {
            synchronized (UdpManager.class) {
                if (instance == null) {
                    instance = new UdpManager();
                }
            }
        }
        return instance.startUdpListener();
    }

    private UdpManager startUdpListener() {
        if (this.mDatagramSocket == null) {
            try {
                this.mDatagramSocket = new DatagramSocket(12819);
            } catch (SocketException e) {
            }
            if (this.mDatagramSocket == null) {
                try {
                    this.mDatagramSocket = new DatagramSocket();
                } catch (SocketException e2) {
                }
            }
            if (this.mDatagramSocket != null) {
                new DatagramPacketReceiveThread().start();
            }
        }
        return this;
    }

    public class DatagramPacketReceiveThread extends Thread {

        /* renamed from: a */
        public static final int f14829a = 1024;

        /* renamed from: b */
        byte[] byteArray;
        boolean c;

        private DatagramPacketReceiveThread() {
            this.byteArray = new byte[1024];
            this.c = true;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            byte[] bArr = this.byteArray;
            DatagramPacket datagramPacket = new DatagramPacket(bArr, bArr.length);
            while (true) {
                try {
                    UdpManager.this.mDatagramSocket.receive(datagramPacket);
                    byte[] data = datagramPacket.getData();
                    Log.i("lvjielvjie", datagramPacket.getAddress()+""+datagramPacket.getPort()+""+new String(data));
                } catch (Exception e) {
                }
            }
        }
    }

    public void findDevices(Context context) {
//        InetAddress broadcast;
//        InetAddress a2 = NetworkUtil.a(context);
//        if (a2 == null) {
//            try {
//                broadcast = InetAddress.getByAddress(new byte[]{-1, -1, -1, -1});
//            } catch (UnknownHostException e) {
//                broadcast = null;
//            }
//        } else {
//            broadcast = NetworkUtil.getBroadcast(a2);
//        }
//        if (broadcast != null) {
//            broadcastAddress(broadcast, context);
//        }
    }
}
