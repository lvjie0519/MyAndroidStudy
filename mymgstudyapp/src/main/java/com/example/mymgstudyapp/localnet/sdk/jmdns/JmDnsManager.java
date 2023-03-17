package com.example.mymgstudyapp.localnet.sdk.jmdns;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.io.IOException;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;

/**
 * https://github.com/twitwi/AndroidDnssdDemo
 */
public class JmDnsManager {
    private static final String TAG = "JmDnsManager";
    private static final String TYPE = "_workstation._tcp.local.";

    private static JmDnsManager sInstance;
    private boolean mInit = false;

    private JmDNS jmdns = null;
    private ServiceInfo serviceInfo;

    private WifiManager.MulticastLock lock;

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
            Log.i(TAG, "serviceAdded call, type: "+event.getType()+", name: "+event.getName());
            jmdns.requestServiceInfo(event.getType(), event.getName(), 1);
        }
    };

    private JmDnsManager() {
    }

    public static JmDnsManager getInstance() {
        if (sInstance == null) {
            synchronized (JmDnsManager.class) {
                if (sInstance == null) {
                    sInstance = new JmDnsManager();
                }
            }
        }

        return sInstance;
    }

    public void init(Context context){
        Log.i(TAG, "init call, "+mInit);
        if(mInit){
            return;
        }
        mInit = true;

        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        lock = wifi.createMulticastLock("mylockthereturn");
        lock.setReferenceCounted(true);
        lock.acquire();

        try {
            Log.i(TAG, "JmDNS.create start...");
            jmdns = JmDNS.create();
//            jmdns.addServiceListener("_workstation._tcp.local.", listener);
//            jmdns.addServiceListener("_ipp._tcp.", listener);
            jmdns.addServiceListener("_airplay._tcp.local.", listener);
            jmdns.addServiceListener("urn:dial-multiscreen-org:service:dial:1", listener);
            jmdns.addServiceListener("urn:schemas-upnp-org:device:MediaRenderer:1", listener);
            jmdns.addServiceListener("roku:ecp", listener);
            jmdns.addServiceListener("urn:lge-com:service:webos-second-screen:1", listener);
            jmdns.addServiceListener("urn:schemas-upnp-org:device:MediaRenderer:1", listener);



//            serviceInfo = ServiceInfo.create("_test._tcp.local.", "AndroidTest", 0, "plain test service from android");
//            jmdns.registerService(serviceInfo);

            Log.i(TAG, "JmDNS.create end...");
        } catch (IOException e) {
            Log.e(TAG, "error: "+e.toString());
            e.printStackTrace();
            return;
        }
    }

    private void notifyUser(final String msg) {
        Log.i(TAG, "notifyUser call, msg: "+msg);
    }

}
