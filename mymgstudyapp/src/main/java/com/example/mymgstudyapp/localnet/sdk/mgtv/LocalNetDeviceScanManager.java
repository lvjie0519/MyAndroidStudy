package com.example.mymgstudyapp.localnet.sdk.mgtv;

import android.content.Context;
import android.util.Log;

import com.example.mymgstudyapp.localnet.sdk.mgtv.utils.IPUtil;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class LocalNetDeviceScanManager {
    private static final String TAG = "LocalNetDeviceScan";

    private static LocalNetDeviceScanManager sInstance;
    private volatile boolean mInit = false;

    private Context mContext;

    public static LocalNetDeviceScanManager getInstance() {
        if (sInstance == null) {
            synchronized (LocalNetDeviceScanManager.class) {
                if (sInstance == null) {
                    sInstance = new LocalNetDeviceScanManager();
                }
            }
        }

        return sInstance;
    }

    public void init(Context context) {
        if (mInit) {
            return;
        }

        mInit = true;
        mContext = context.getApplicationContext() != null ? context.getApplicationContext() : context;
    }

    public void scanLocalNetDevice() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String startIp = "192.168.3.";
                for(int i=1; i<255; i++){
                    try {
                        String host = startIp + i;
                        InetAddress address = InetAddress.getByName(host);
                        if (address.isReachable(1000)) {
                            Log.i(TAG, "host: " + host + " isReachable...");
                        }
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void pingAllLocalNetIp() {
        Log.i(TAG, "pingAllLocalNetIp...");
        int[] ipRange = IPUtil.getLocalNetworkRange(mContext);

        int start = ipRange[0];
        while (start < ipRange[1]) {
            String host = IPUtil.ipIntToString(start);
            try {
                InetAddress address = InetAddress.getByName(host);
                if (address.isReachable(1000)) {
                    Log.i(TAG, "host: " + host + " isReachable...");
                }
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            start++;
        }
    }

}
