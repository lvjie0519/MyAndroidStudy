package com.example.mymgstudyapp.localnet.sdk.wukongtv.deviceprovider;

import android.content.Context;
import android.util.Log;
import com.connectsdk.core.Util;
import com.connectsdk.discovery.DiscoveryFilter;
import com.connectsdk.discovery.DiscoveryProvider;
import com.connectsdk.discovery.DiscoveryProviderListener;
import com.connectsdk.discovery.provider.ssdp.SSDPClient;
import com.connectsdk.discovery.provider.ssdp.SSDPDevice;
import com.connectsdk.discovery.provider.ssdp.SSDPPacket;
import com.connectsdk.service.config.ServiceDescription;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: classes2.dex */
public class SSDPDiscoveryProvider implements DiscoveryProvider {
    Context context;
    private Thread notifyThread;
    private Thread responseThread;
    private Timer scanTimer;
    private SSDPClient ssdpClient;
    boolean needToStartSearch = false;
    ConcurrentHashMap<String, ServiceDescription> foundServices = new ConcurrentHashMap<>();
    ConcurrentHashMap<String, ServiceDescription> discoveredServices = new ConcurrentHashMap<>();
    boolean isRunning = false;
    private Runnable mResponseHandler = new Runnable() { // from class: com.connectsdk.discovery.provider.SSDPDiscoveryProvider.3
        @Override // java.lang.Runnable
        public void run() {
            while (SSDPDiscoveryProvider.this.ssdpClient != null) {
                try {
                    SSDPDiscoveryProvider.this.handleSSDPPacket(new SSDPPacket(SSDPDiscoveryProvider.this.ssdpClient.responseReceive()));
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                } catch (RuntimeException e2) {
                    e2.printStackTrace();
                    return;
                }
            }
        }
    };
    private Runnable mRespNotifyHandler = new Runnable() { // from class: com.connectsdk.discovery.provider.SSDPDiscoveryProvider.4
        @Override // java.lang.Runnable
        public void run() {
            while (SSDPDiscoveryProvider.this.ssdpClient != null) {
                try {
                    SSDPDiscoveryProvider.this.handleSSDPPacket(new SSDPPacket(SSDPDiscoveryProvider.this.ssdpClient.multicastReceive()));
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                } catch (RuntimeException e2) {
                    e2.printStackTrace();
                    return;
                }
            }
        }
    };
    private Pattern uuidReg = Pattern.compile("(?<=uuid:)(.+?)(?=(::)|$)");
    private CopyOnWriteArrayList<DiscoveryProviderListener> serviceListeners = new CopyOnWriteArrayList<>();
    List<DiscoveryFilter> serviceFilters = new CopyOnWriteArrayList();

    public boolean containsServicesWithFilter(SSDPDevice sSDPDevice, String str) {
        return true;
    }

    public SSDPDiscoveryProvider(Context context) {
        this.context = context;
    }

    private void openSocket() {
        SSDPClient sSDPClient = this.ssdpClient;
        if (sSDPClient == null || !sSDPClient.isConnected()) {
            try {
                InetAddress ipAddress = Util.getIpAddress(this.context);
                if (ipAddress == null) {
                    return;
                }
                this.ssdpClient = createSocket(ipAddress);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }

    protected SSDPClient createSocket(InetAddress inetAddress) throws IOException {
        return new SSDPClient(inetAddress);
    }

    @Override // com.connectsdk.discovery.DiscoveryProvider
    public void start() {
        if (this.isRunning) {
            return;
        }
        this.isRunning = true;
        openSocket();
        this.scanTimer = new Timer();
        this.scanTimer.schedule(new TimerTask() { // from class: com.connectsdk.discovery.provider.SSDPDiscoveryProvider.1
            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                SSDPDiscoveryProvider.this.sendSearch();
            }
        }, 100L, 10000L);
        this.responseThread = new Thread(this.mResponseHandler);
        this.notifyThread = new Thread(this.mRespNotifyHandler);
        this.responseThread.start();
        this.notifyThread.start();
    }

    public void sendSearch() {
        ArrayList<String> arrayList = new ArrayList();
        long time = new Date().getTime() - 60000;
        for (String str : this.foundServices.keySet()) {
            ServiceDescription serviceDescription = this.foundServices.get(str);
            if (serviceDescription == null || serviceDescription.getLastDetection() < time) {
                arrayList.add(str);
            }
        }
        for (String str2 : arrayList) {
            ServiceDescription serviceDescription2 = this.foundServices.get(str2);
            if (serviceDescription2 != null) {
                notifyListenersOfLostService(serviceDescription2);
            }
            if (this.foundServices.containsKey(str2)) {
                this.foundServices.remove(str2);
            }
        }
        rescan();
    }

    @Override // com.connectsdk.discovery.DiscoveryProvider
    public void stop() {
        this.isRunning = false;
        Timer timer = this.scanTimer;
        if (timer != null) {
            timer.cancel();
            this.scanTimer = null;
        }
        Thread thread = this.responseThread;
        if (thread != null) {
            thread.interrupt();
            this.responseThread = null;
        }
        Thread thread2 = this.notifyThread;
        if (thread2 != null) {
            thread2.interrupt();
            this.notifyThread = null;
        }
        SSDPClient sSDPClient = this.ssdpClient;
        if (sSDPClient != null) {
            sSDPClient.close();
            this.ssdpClient = null;
        }
    }

    @Override // com.connectsdk.discovery.DiscoveryProvider
    public void restart() {
        stop();
        start();
    }

    @Override // com.connectsdk.discovery.DiscoveryProvider
    public void reset() {
        stop();
        this.foundServices.clear();
        this.discoveredServices.clear();
    }

    @Override // com.connectsdk.discovery.DiscoveryProvider
    public void rescan() {
        for (DiscoveryFilter discoveryFilter : this.serviceFilters) {
            final String sSDPSearchMessage = SSDPClient.getSSDPSearchMessage(discoveryFilter.getServiceFilter());
            Timer timer = new Timer();
            for (int i = 0; i < 3; i++) {
                timer.schedule(new TimerTask() { // from class: com.connectsdk.discovery.provider.SSDPDiscoveryProvider.2
                    @Override // java.util.TimerTask, java.lang.Runnable
                    public void run() {
                        try {
                            if (SSDPDiscoveryProvider.this.ssdpClient != null) {
                                SSDPDiscoveryProvider.this.ssdpClient.send(sSDPSearchMessage);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, i * 1000);
            }
        }
    }

    @Override // com.connectsdk.discovery.DiscoveryProvider
    public void addDeviceFilter(DiscoveryFilter discoveryFilter) {
        if (discoveryFilter.getServiceFilter() == null) {
            Log.e(Util.T, "This device filter does not have ssdp filter info");
        } else {
            this.serviceFilters.add(discoveryFilter);
        }
    }

    @Override // com.connectsdk.discovery.DiscoveryProvider
    public void removeDeviceFilter(DiscoveryFilter discoveryFilter) {
        this.serviceFilters.remove(discoveryFilter);
    }

    @Override // com.connectsdk.discovery.DiscoveryProvider
    public void setFilters(List<DiscoveryFilter> list) {
        this.serviceFilters = list;
    }

    @Override // com.connectsdk.discovery.DiscoveryProvider
    public boolean isEmpty() {
        return this.serviceFilters.size() == 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSSDPPacket(SSDPPacket sSDPPacket) {
        String str;
        if (sSDPPacket == null || sSDPPacket.getData().size() == 0 || sSDPPacket.getType() == null) {
            return;
        }
        String str2 = sSDPPacket.getData().get(sSDPPacket.getType().equals(SSDPClient.NOTIFY) ? "NT" : "ST");
        if (str2 == null || SSDPClient.MSEARCH.equals(sSDPPacket.getType()) || !isSearchingForFilter(str2) || (str = sSDPPacket.getData().get("USN")) == null || str.length() == 0) {
            return;
        }
        Matcher matcher = this.uuidReg.matcher(str);
        if (matcher.find()) {
            String group = matcher.group();
            if (SSDPClient.BYEBYE.equals(sSDPPacket.getData().get("NTS"))) {
                ServiceDescription serviceDescription = this.foundServices.get(group);
                if (serviceDescription != null) {
                    this.foundServices.remove(group);
                    notifyListenersOfLostService(serviceDescription);
                    return;
                }
                return;
            }
            String str3 = sSDPPacket.getData().get("LOCATION");
            if (str3 == null || str3.length() == 0) {
                return;
            }
            ServiceDescription serviceDescription2 = this.foundServices.get(group);
            if (serviceDescription2 == null && this.discoveredServices.get(group) == null) {
                serviceDescription2 = new ServiceDescription();
                serviceDescription2.setUUID(group);
                serviceDescription2.setServiceFilter(str2);
                serviceDescription2.setIpAddress(sSDPPacket.getDatagramPacket().getAddress().getHostAddress());
                serviceDescription2.setPort(3001);
                this.discoveredServices.put(group, serviceDescription2);
                getLocationData(str3, group, str2);
            }
            if (serviceDescription2 != null) {
                serviceDescription2.setLastDetection(new Date().getTime());
            }
        }
    }

    public void getLocationData(String str, String str2, String str3) {
        Log.i("WukongTvJmDnsManager", "SSDPDiscoveryProvider getLocationData call, "+str+", "+str2+", "+str3);
//        try {
//            getLocationData(new URL(str), str2, str3);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public void getLocationData(final URL url, final String str, final String str2) {
        Util.runInBackground(new Runnable() { // from class: com.connectsdk.discovery.provider.SSDPDiscoveryProvider.5
            /* JADX WARN: Removed duplicated region for block: B:12:0x001b  */
            @Override // java.lang.Runnable
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            public void run() {
                /*
                    r3 = this;
                    com.connectsdk.discovery.provider.ssdp.SSDPDevice r0 = new com.connectsdk.discovery.provider.ssdp.SSDPDevice     // Catch: org.xml.sax.SAXException -> La javax.xml.parsers.ParserConfigurationException -> Lf java.io.IOException -> L14
                    java.net.URL r1 = r2     // Catch: org.xml.sax.SAXException -> La javax.xml.parsers.ParserConfigurationException -> Lf java.io.IOException -> L14
                    java.lang.String r2 = r3     // Catch: org.xml.sax.SAXException -> La javax.xml.parsers.ParserConfigurationException -> Lf java.io.IOException -> L14
                    r0.<init>(r1, r2)     // Catch: org.xml.sax.SAXException -> La javax.xml.parsers.ParserConfigurationException -> Lf java.io.IOException -> L14
                    goto L19
                La:
                    r0 = move-exception
                    r0.printStackTrace()
                    goto L18
                Lf:
                    r0 = move-exception
                    r0.printStackTrace()
                    goto L18
                L14:
                    r0 = move-exception
                    r0.printStackTrace()
                L18:
                    r0 = 0
                L19:
                    if (r0 == 0) goto L81
                    java.lang.String r1 = r4
                    r0.UUID = r1
                    com.connectsdk.discovery.provider.SSDPDiscoveryProvider r1 = com.connectsdk.discovery.provider.SSDPDiscoveryProvider.this
                    java.lang.String r2 = r3
                    boolean r1 = r1.containsServicesWithFilter(r0, r2)
                    if (r1 == 0) goto L81
                    com.connectsdk.discovery.provider.SSDPDiscoveryProvider r1 = com.connectsdk.discovery.provider.SSDPDiscoveryProvider.this
                    java.util.concurrent.ConcurrentHashMap<java.lang.String, com.connectsdk.service.config.ServiceDescription> r1 = r1.discoveredServices
                    java.lang.String r2 = r4
                    java.lang.Object r1 = r1.get(r2)
                    com.connectsdk.service.config.ServiceDescription r1 = (com.connectsdk.service.config.ServiceDescription) r1
                    if (r1 == 0) goto L81
                    java.lang.String r2 = r3
                    r1.setServiceFilter(r2)
                    java.lang.String r2 = r0.friendlyName
                    r1.setFriendlyName(r2)
                    java.lang.String r2 = r0.modelName
                    r1.setModelName(r2)
                    java.lang.String r2 = r0.modelNumber
                    r1.setModelNumber(r2)
                    java.lang.String r2 = r0.modelDescription
                    r1.setModelDescription(r2)
                    java.lang.String r2 = r0.manufacturer
                    r1.setManufacturer(r2)
                    java.lang.String r2 = r0.applicationURL
                    r1.setApplicationURL(r2)
                    java.util.List<com.connectsdk.discovery.provider.ssdp.Service> r2 = r0.serviceList
                    r1.setServiceList(r2)
                    java.util.Map<java.lang.String, java.util.List<java.lang.String>> r2 = r0.headers
                    r1.setResponseHeaders(r2)
                    java.lang.String r2 = r0.locationXML
                    r1.setLocationXML(r2)
                    java.lang.String r2 = r0.serviceURI
                    r1.setServiceURI(r2)
                    int r0 = r0.port
                    r1.setPort(r0)
                    com.connectsdk.discovery.provider.SSDPDiscoveryProvider r0 = com.connectsdk.discovery.provider.SSDPDiscoveryProvider.this
                    java.util.concurrent.ConcurrentHashMap<java.lang.String, com.connectsdk.service.config.ServiceDescription> r0 = r0.foundServices
                    java.lang.String r2 = r4
                    r0.put(r2, r1)
                    com.connectsdk.discovery.provider.SSDPDiscoveryProvider r0 = com.connectsdk.discovery.provider.SSDPDiscoveryProvider.this
                    com.connectsdk.discovery.provider.SSDPDiscoveryProvider.access$200(r0, r1)
                L81:
                    com.connectsdk.discovery.provider.SSDPDiscoveryProvider r0 = com.connectsdk.discovery.provider.SSDPDiscoveryProvider.this
                    java.util.concurrent.ConcurrentHashMap<java.lang.String, com.connectsdk.service.config.ServiceDescription> r0 = r0.discoveredServices
                    java.lang.String r1 = r4
                    r0.remove(r1)
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: com.connectsdk.discovery.provider.SSDPDiscoveryProvider.AnonymousClass5.run():void");
            }
        }, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyListenersOfNewService(ServiceDescription serviceDescription) {
        for (String str : serviceIdsForFilter(serviceDescription.getServiceFilter())) {
            final ServiceDescription m40clone = serviceDescription.clone();
            m40clone.setServiceID(str);
            Util.runOnUI(new Runnable() { // from class: com.connectsdk.discovery.provider.SSDPDiscoveryProvider.6
                @Override // java.lang.Runnable
                public void run() {
                    Iterator it = SSDPDiscoveryProvider.this.serviceListeners.iterator();
                    while (it.hasNext()) {
                        ((DiscoveryProviderListener) it.next()).onServiceAdded(SSDPDiscoveryProvider.this, m40clone);
                    }
                }
            });
        }
    }

    private void notifyListenersOfLostService(ServiceDescription serviceDescription) {
        for (String str : serviceIdsForFilter(serviceDescription.getServiceFilter())) {
            final ServiceDescription m40clone = serviceDescription.clone();
            m40clone.setServiceID(str);
            Util.runOnUI(new Runnable() { // from class: com.connectsdk.discovery.provider.SSDPDiscoveryProvider.7
                @Override // java.lang.Runnable
                public void run() {
                    Iterator it = SSDPDiscoveryProvider.this.serviceListeners.iterator();
                    while (it.hasNext()) {
                        ((DiscoveryProviderListener) it.next()).onServiceRemoved(SSDPDiscoveryProvider.this, m40clone);
                    }
                }
            });
        }
    }

    public List<String> serviceIdsForFilter(String str) {
        String serviceId;
        ArrayList arrayList = new ArrayList();
        for (DiscoveryFilter discoveryFilter : this.serviceFilters) {
            if (discoveryFilter.getServiceFilter().equals(str) && (serviceId = discoveryFilter.getServiceId()) != null) {
                arrayList.add(serviceId);
            }
        }
        return arrayList;
    }

    public boolean isSearchingForFilter(String str) {
        for (DiscoveryFilter discoveryFilter : this.serviceFilters) {
            if (discoveryFilter.getServiceFilter().equals(str)) {
                return true;
            }
        }
        return false;
    }

    @Override // com.connectsdk.discovery.DiscoveryProvider
    public void addListener(DiscoveryProviderListener discoveryProviderListener) {
        this.serviceListeners.add(discoveryProviderListener);
    }

    @Override // com.connectsdk.discovery.DiscoveryProvider
    public void removeListener(DiscoveryProviderListener discoveryProviderListener) {
        this.serviceListeners.remove(discoveryProviderListener);
    }
}
