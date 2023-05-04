package com.example.mymgstudyapp.localnet.sdk.wukongtv;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.connectsdk.service.AirPlayService;
import com.connectsdk.service.DIALService;
import com.connectsdk.service.DLNAService;
import com.connectsdk.service.NetcastTVService;
import com.connectsdk.service.RokuService;
import com.connectsdk.service.WebOSTVService;
import com.example.mymgstudyapp.localnet.sdk.wukongtv.deviceprovider.BetterSSDPDiscoveryProvider;
import com.example.mymgstudyapp.localnet.sdk.wukongtv.deviceprovider.SSDPDiscoveryProvider;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;

/**
 * https://github.com/twitwi/AndroidDnssdDemo
 */
public class WukongTvJmDnsManager {
    private static final String TAG = "WukongTvJmDnsManager";

    private static WukongTvJmDnsManager sInstance;
    private boolean mInit = false;

    private JmDNS jmdns = null;
    private ServiceInfo serviceInfo;
    private InetAddress mInetAddress;

    private WifiManager.MulticastLock lock;

    private SSDPDiscoveryProvider mSSDPDiscoveryProvider;
    private BetterSSDPDiscoveryProvider mBetterSSDPDiscoveryProvider;

    private ServiceListener listener = new ServiceListener() {

        @Override
        public void serviceResolved(ServiceEvent ev) {
            String additions = "";
            if (ev.getInfo().getInetAddresses() != null && ev.getInfo().getInetAddresses().length > 0) {
                additions = ev.getInfo().getInetAddresses()[0].getHostAddress();
            }
            notifyUser("Service resolved: " + ev.getInfo().getQualifiedName()
                    + " port:" + ev.getInfo().getPort()
                    + " additions: " + additions);
        }

        @Override
        public void serviceRemoved(ServiceEvent ev) {
            notifyUser("Service removed: " + ev.getName());
        }

        @Override
        public void serviceAdded(ServiceEvent event) {
            // Required to force serviceResolved to be called again (after the first search)
            Log.i(TAG, "serviceAdded call, type: " + event.getType() + ", name: " + event.getName());
            jmdns.requestServiceInfo(event.getType(), event.getName(), 1);
        }
    };

    private WukongTvJmDnsManager() {
    }

    public static WukongTvJmDnsManager getInstance() {
        if (sInstance == null) {
            synchronized (WukongTvJmDnsManager.class) {
                if (sInstance == null) {
                    sInstance = new WukongTvJmDnsManager();
                }
            }
        }

        return sInstance;
    }

    public void init(Context context) {
        Log.i(TAG, "init call, " + mInit);
        if (mInit) {
            return;
        }
        mInit = true;

        initProvider(context);

        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        lock = wifi.createMulticastLock("mylockthereturn");
        lock.setReferenceCounted(true);
        lock.acquire();

        try {
            mInetAddress = getIpAddress(context);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void initProvider(Context context){
        mSSDPDiscoveryProvider = new SSDPDiscoveryProvider(context);
        mSSDPDiscoveryProvider.addDeviceFilter(WebOSTVService.discoveryFilter());
        mSSDPDiscoveryProvider.addDeviceFilter(NetcastTVService.discoveryFilter());
        mSSDPDiscoveryProvider.addDeviceFilter(DLNAService.discoveryFilter());
        mSSDPDiscoveryProvider.addDeviceFilter(DIALService.discoveryFilter());
        mSSDPDiscoveryProvider.addDeviceFilter(RokuService.discoveryFilter());

        mBetterSSDPDiscoveryProvider = new BetterSSDPDiscoveryProvider(context);
        mBetterSSDPDiscoveryProvider.addDeviceFilter(DLNAService.discoveryFilter());
        mBetterSSDPDiscoveryProvider.addDeviceFilter(DIALService.discoveryFilter());
    }

    public void rescan() {
        try {

            if (this.jmdns != null) {
                this.jmdns.close();
                this.jmdns = null;
            }

            Log.i(TAG, "JmDNS.create start...");
            jmdns = JmDNS.create(mInetAddress, "connectsdk");
            jmdns.addServiceListener("_airplay._tcp.local.", listener);
            jmdns.addServiceListener("urn:dial-multiscreen-org:service:dial:1", listener);
            jmdns.addServiceListener("urn:schemas-upnp-org:device:MediaRenderer:1", listener);
            jmdns.addServiceListener("roku:ecp", listener);
            jmdns.addServiceListener("urn:lge-com:service:webos-second-screen:1", listener);
            jmdns.addServiceListener("urn:schemas-upnp-org:device:MediaRenderer:1", listener);
            Log.i(TAG, "JmDNS.create end...");

            mBetterSSDPDiscoveryProvider.start();
            mSSDPDiscoveryProvider.start();
        } catch (IOException e) {
            Log.e(TAG, "error: " + e.toString());
            e.printStackTrace();
            return;
        }
    }

    private void notifyUser(final String msg) {
        Log.i(TAG, "notifyUser call, msg: " + msg);
    }


    public static InetAddress getIpAddress(Context context) throws UnknownHostException {
        int ipAddress = ((WifiManager) context.getSystemService(Context.WIFI_SERVICE)).getConnectionInfo().getIpAddress();
        if (ipAddress == 0) {
            return null;
        }
        return InetAddress.getByAddress(convertIpAddress(ipAddress));
    }

    public static byte[] convertIpAddress(int i) {
        return new byte[]{(byte) (i & 255), (byte) ((i >> 8) & 255), (byte) ((i >> 16) & 255), (byte) ((i >> 24) & 255)};
    }

}
