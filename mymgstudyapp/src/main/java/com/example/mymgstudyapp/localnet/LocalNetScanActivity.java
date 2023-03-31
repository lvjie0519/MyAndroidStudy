package com.example.mymgstudyapp.localnet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.connectsdk.discovery.provider.ssdp.SSDPClient;
import com.example.mymgstudyapp.R;
import com.example.mymgstudyapp.localnet.sdk.DeviceSearcher;
import com.example.mymgstudyapp.localnet.sdk.DeviceWaitingSearch;
import com.example.mymgstudyapp.localnet.sdk.ScanDeviceTool;
import com.example.mymgstudyapp.localnet.sdk.jmdns.JmDnsManager;
import com.example.mymgstudyapp.localnet.sdk.mgtv.DeviceInfo;
import com.example.mymgstudyapp.localnet.sdk.mgtv.LocalNetDeviceScanManager;
import com.example.mymgstudyapp.localnet.sdk.mgtv.utils.ArpUtil;
import com.example.mymgstudyapp.localnet.sdk.netx.MulticastScanDeviceManager;
import com.example.mymgstudyapp.localnet.sdk.wukongtv.WukongTvJmDnsManager;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;

public class LocalNetScanActivity extends Activity {

    private static final String TAG = "LocalNetScanActivity";

    public static void startActivity(Context context){
        Intent intent = new Intent(context, LocalNetScanActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_net_scan);

//        MulticastScanDeviceManager.getInstance().init(this);
    }

    public void onClickStartScan(View view) {
        startScan();
    }

    private void startScan() {
        new DeviceSearcher() {
            @Override
            public void onSearchStart() {
                Log.i(TAG, "onSearchStart...");
            }

            @Override
            public void onSearchFinish(Set deviceSet) {
                Log.i(TAG, "onSearchFinish..."+deviceSet.size());
            }
        }.start();
    }

    public void onClickStartWaitScan(View view) {
        startWaitScan();
    }

    private void startWaitScan() {
        Log.i(TAG, "startWaitScan...");
        new DeviceWaitingSearch(this, "日灯光", "客厅"){
            @Override
            public void onDeviceSearched(InetSocketAddress socketAddr) {
                Log.i(TAG, "已上线，搜索主机：" + socketAddr.getAddress().getHostAddress()
                        + ":" + socketAddr.getPort());
            }
        }.start();
    }

    public void onClickTest(View view) {
        test();
    }

    private void test1(){

    }

    private void test(){
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;

        if (activeNetworkInfo != null) {
            Log.i(TAG, "getSubtypeName: "+activeNetworkInfo.getSubtypeName());

            if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                WifiManager wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                if (wifiManager == null) {
                    return;
                }

                WifiInfo connectionInfo = wifiManager.getConnectionInfo();
                DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();
                WifiManager.MulticastLock createMulticastLock = wifiManager.createMulticastLock(getClass().getName());
                createMulticastLock.setReferenceCounted(true);
                createMulticastLock.acquire();
            }
        }
    }

    public void onClickRecieveInfo(View view) {
//        MulticastScanDeviceManager.getInstance().receiveMessage();
        MulticastScanDeviceManager.getInstance().receiveMessageLoopAsync();
    }

    public void onClickSendInfo(View view) {
        MulticastScanDeviceManager.getInstance().sendMessageAsync("hello");
    }

    public void onClickPrintInfo(View view) {
//        MulticastScanDeviceManager.getInstance().printScanInfos();
//        MulticastScanDeviceManager.getInstance().test1();

//        Log.i("lvjielvjie", PingUtil.pingIp("192.168.3.80"));

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
////                JmDnsManager.getInstance().init(LocalNetScanActivity.this);
//                WukongTvJmDnsManager.getInstance().init(LocalNetScanActivity.this);
//                WukongTvJmDnsManager.getInstance().rescan();
//
////                LocalNetDeviceScanManager.getInstance().init(LocalNetScanActivity.this);
////                LocalNetDeviceScanManager.getInstance().scanLocalNetDevice();
//            }
//        }).start();

        ScanDeviceTool scanDeviceTool = new ScanDeviceTool();
        scanDeviceTool.scan(null);

//        try {
//            InetAddress address = InetAddress.getByName("192.168.3.164");//[-64, -88, 3, 93]
//            WifiManager wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//            byte[] L3 = L(wifiManager.getDhcpInfo().ipAddress);
//            R(L3, 24, 0);
//            Log.i(TAG, "");
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }
    }

    public void R(byte[] bArr, int i, int i2) {
        int i3 = i / 8;
        int i4 = i % 8;
        bArr[i3] = (byte) ((i2 << (8 - (i4 + 1))) | ((byte) (bArr[i3] & (65407 >> i4) & 255)));
    }

    byte[] L(int i) {
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

    public void onClickStopRecieveInfo(View view) {
        MulticastScanDeviceManager.getInstance().stopRecieveMessage();
//        Executors.newFixedThreadPool(20);
    }

    public void onClickReadArpFile(View view) {
    }

    public void onClickCmdReadArpFile(View view) {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("title");
        builder.create().show();
    }
}